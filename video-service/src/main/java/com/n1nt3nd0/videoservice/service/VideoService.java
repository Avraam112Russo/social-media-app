package com.n1nt3nd0.videoservice.service;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.common.io.ByteStreams;
import com.n1nt3nd0.videoservice.model.VideoFile;
import com.n1nt3nd0.videoservice.repository.VideoFileRepository;
import com.n1nt3nd0.videoservice.util.DataBucketUtil;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final VideoFileRepository videoFileRepository;
    private final DataBucketUtil dataBucketUtil;
    private final Bucket bucket;
    private final Storage storage;
//    public List<VideoFile> uploadFiles(MultipartFile[] files) {
//        log.info("Start file uploading service.");
//        List<VideoFile> videoFiles = new ArrayList<>();
//
//        Arrays.asList(files).forEach(file -> {
//            String originalFileNAme = file.getOriginalFilename();
//            if (originalFileNAme == null){
//                throw new BadRequestException("Original video-file name is null.");
//            }
//            Path path = new File(originalFileNAme).toPath();
//            try {
//                String contentType = Files.probeContentType(path);
//                FileDto fileDto = dataBucketUtil.uploadFile(file, originalFileNAme, contentType);
//                if (fileDto != null){
//                    videoFiles.add(VideoFile.builder()
//                                    .fileName(fileDto.getName())
//                                    .url(fileDto.getMediaLink())
//                            .build());
//                    log.info("File uploaded successfully, file name: {} and file URL: {}", fileDto.getName(), fileDto.getMediaLink());
//                }
//
//            }catch (Exception e){
//                log.error("Error while uploaded file IN FileService: {}", e.getMessage());
//                throw new RuntimeException("Error while uploaded file IN FileService");
//            }
//        });
//        videoFileRepository.saveAll(videoFiles);
//        log.info("File details successfully saved in database");
//        return videoFiles;
//    }

    public void downloadNewVideo(String email,
                                   String videoName,
                                   String videoDesc,
                                   MultipartFile multipartFile) {
        try {
            String url = saveVideoInGoogleCloud(multipartFile);
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

    private String saveVideoInGoogleCloud(MultipartFile multipartFile) {
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

    public ResponseEntity<StreamingResponseBody> getVideoFromGoogleCloudById(Long id) throws IOException {

        VideoFile videoFile = videoFileRepository.findById(id).orElseThrow(() -> new NotFoundException("Video %s not found.".formatted(id)));
        String url = videoFile.getUrl();
        Blob blob = storage.get(BlobId.of(bucket.getName(), url));
        if (blob == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        StreamingResponseBody responseBody = outputStream -> {
            try (ReadChannel reader = blob.reader()) {
                byte[] buffer = new byte[1024];
                int limit;
                while ((limit = reader.read(ByteBuffer.wrap(buffer))) >= 0) {
                    outputStream.write(buffer, 0, limit);
                }
            }catch (Exception e){
                throw new RuntimeException("Error while read blob IN getVideoFromGoogleCloudById");
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4"))
                .body(responseBody);
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

