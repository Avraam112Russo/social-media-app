package com.n1nt3nd0.videoservice.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.ws.rs.BadRequestException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import net.bytebuddy.utility.RandomString;
import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
public class DataBucketUtil {

//
//    @Value("${gcp.config.file}")
//    private String gcpConfigFile;
//    @Value("${gcp.project.id}")
//    private String gcpProjectId;
//    @Value("${gcp.bucket.id}")
//    private String gcpBucketId;
//    @Value("${gcp.dir.name}")
//    private String gcpDirectoryName;
//
//
//
//    @SneakyThrows
//    public FileDto uploadFile(MultipartFile multipartFile, String originalFileNAme, String contentType) {
//      log.info("Start file uploading process on GCS");
//
//
//        try{
//
//            byte[] fileData = FileUtils.readFileToByteArray(convert(multipartFile));
//            InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();
//            StorageOptions storageOptions = StorageOptions.newBuilder()
//                    .setProjectId(gcpProjectId)
//                    .setCredentials(GoogleCredentials.fromStream(inputStream))
//                    .build();
//            Storage storage = storageOptions.getService();
//            Bucket bucket = storage.get(gcpBucketId, Storage.BucketGetOption.fields());
//            RandomString id = new RandomString(6, ThreadLocalRandom.current());
//            Blob blob = bucket.create(gcpDirectoryName + "/" + originalFileNAme + "-"
//            + id.nextString() + checkFileExtension(originalFileNAme), fileData, contentType);
//            if (blob != null){
//                log.info("File successfully upload to GCS");
//                return FileDto.builder()
//                        .name(blob.getName())
//                        .mediaLink(blob.getMediaLink())
//                        .build();
//            }
//        }catch (Exception e){
//            log.error("An error occurred while uploading data: {}", e.getMessage());
//            throw new RuntimeException("An error occurred while uploading data: ");
//        }
//            throw new RuntimeException("An error occurred while uploading data: ");
//    }
//
//
//    private File convert(MultipartFile multipartFile) {
//        try {
//            if (multipartFile.getOriginalFilename() == null){
//                throw new BadRequestException("Original file name is null.");
//            }
//            File convertedFile = new File(multipartFile.getOriginalFilename());
//            FileOutputStream outputStream = new FileOutputStream(convertedFile);
//            outputStream.write(multipartFile.getBytes());
//            outputStream.close();
//            log.info("Converting multipart file: {}", convertedFile);
//            return convertedFile;
//        } catch (IOException e) {
//            throw new RuntimeException("An error has occurred while converting file");
//        }
//    }
//    private String checkFileExtension(String originalFileNAme) {
//        if (originalFileNAme != null && originalFileNAme.contains(".")){
////            String [] fileExtensions = {".jpeg", ".png", ".pdf", ".mp3"};
//            String [] fileExtensions = {".mp4"};
//            for (String extension : fileExtensions){
//                if (originalFileNAme.endsWith(extension)){
//                    log.info("Accepted file type: {}", extension);
//                    return extension;
//                }
//            }
//        }
//            log.error("Not a permitted file type.");
//            throw new RuntimeException("Not a permitted file type.");
//    }
}
