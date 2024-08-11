package com.n1nt3nd0.videoservice.service;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.n1nt3nd0.videoservice.exception.DownloadVideoFromGcsException;
import com.n1nt3nd0.videoservice.entity.VideoFile;
import com.n1nt3nd0.videoservice.repository.VideoFileRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final VideoFileRepository videoFileRepository;
    private final Bucket bucket;
    private final Storage storage;

    public void saveNewVideoFileToDataBase(String email,
                                           String videoName,
                                           String videoDesc,
                                           MultipartFile multipartFile) {
        try {
            String url = uploadVideoInGoogleCloud(multipartFile);
            VideoFile build = VideoFile.builder()
                    .videoName(videoName)
                    .description(videoDesc)
                    .videoOwnerEmail(email)
                    .url(url)
                    .build();
            VideoFile savedVideo = videoFileRepository.save(build);
            log.info("Video saved successfully: {}", savedVideo.toString());
        }catch (Exception e){
            log.error("Error while downloadNewVideo IN VideoService: {}", e.getMessage());
            throw new RuntimeException("Error while downloadNewVideo IN VideoService");
        }


    }

    private String uploadVideoInGoogleCloud(MultipartFile multipartFile) {
        try {
            byte[] videoFilesAsBytesArray = FileUtils.readFileToByteArray(converted(multipartFile));
            String videoFileUrl = UUID.randomUUID().toString();
            ;
            Blob savedVideo = bucket.create(videoFileUrl, videoFilesAsBytesArray);
            log.info("Video file saved successfully: {}", savedVideo.toString());
            return videoFileUrl;
        } catch (Exception e) {
            log.error("An error has occurred while saveNewVideoInBucket IN GcpDataUtil: {}", e.getMessage());
            throw new RuntimeException("An error has occurred while saveNewVideoInBucket IN GcpDataUtil");
        }
    }

    public ResponseEntity<StreamingResponseBody> getVideoFromGoogleCloudById(Long id, String rangeHeader) throws IOException {
        VideoFile videoFile = videoFileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Video %s not found.".formatted(id)));
        String url = videoFile.getUrl();
        Blob blob = storage.get(BlobId.of(bucket.getName(), url));
        if (blob == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        StreamingResponseBody responseStream;
        final HttpHeaders responseHeaders = new HttpHeaders();
        try {

            if (rangeHeader == null){
                responseHeaders.add("Content-Type", "video/mp4");
                responseHeaders.add("Content-Length", blob.getSize().toString());

                responseStream = outputStream -> {
                    long position = 0;
                    try (ReadChannel reader = blob.reader()) {
                        reader.seek(position);
                        ByteBuffer bytes = ByteBuffer.allocate(1024 * 16);
                        while (reader.read(bytes) > 0) {
                            bytes.flip();
                            outputStream.write(bytes.array());
                            bytes.clear();
                        }
                        outputStream.flush();
                    }


                };
                return new ResponseEntity<StreamingResponseBody>
                        (responseStream, responseHeaders, HttpStatus.OK);
            }


            String[] ranges = rangeHeader.split("-");
            Long rangeStart = Long.parseLong(ranges[0].substring(6));
            Long rangeEnd;
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            }
            else {
                rangeEnd = blob.getSize() - 1;
            }

            if (blob.getSize() < rangeEnd) {
                rangeEnd = blob.getSize() - 1;
            }

            String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
            responseHeaders.add("Content-Type", "video/mp4");
            responseHeaders.add("Content-Length", contentLength);
            responseHeaders.add("Accept-Ranges", "bytes");
            responseHeaders.add("Content-Range", "bytes" + " " +
                    rangeStart + "-" + rangeEnd + "/" + blob.getSize());
            final Long _rangeEnd = rangeEnd;
            responseStream = outputStream -> {
                long position = rangeStart;
                try (ReadChannel reader = blob.reader()) {
                    reader.seek(position);
                    ByteBuffer bytes = ByteBuffer.allocate(1024 * 16);
                    while (reader.read(bytes) > 0) {
                        bytes.flip();
                        outputStream.write(bytes.array());
                        bytes.clear();
                    }
                    outputStream.flush();
                }
            };

            return new ResponseEntity<StreamingResponseBody>
                    (responseStream, responseHeaders, HttpStatus.PARTIAL_CONTENT);
        }catch (Exception e){
            log.error("Error while getVideo IN Video Service");
            throw new DownloadVideoFromGcsException("Error while getVideo IN Video Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private File converted(MultipartFile multipartFile) {

        try {
            if (multipartFile.getOriginalFilename() == null) {
                throw new org.apache.coyote.BadRequestException("Original file name is null");
            }
            File convertedFile = new File(multipartFile.getOriginalFilename());
            FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
            log.info("Converting multipart file: {}", convertedFile);
            return convertedFile;
        } catch (Exception e) {
            throw new RuntimeException("An error has occurred while converting the file");
        }
    }
    }

