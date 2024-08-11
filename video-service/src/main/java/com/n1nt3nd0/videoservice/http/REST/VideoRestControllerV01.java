package com.n1nt3nd0.videoservice.http.REST;

import com.n1nt3nd0.videoservice.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;

@RestController
@RequestMapping("/api/video-service")
@RequiredArgsConstructor
@Slf4j
public class VideoRestControllerV01 {
    private final VideoService videoService;
    @PostMapping(value = "/new-video")
    public ResponseEntity<?> saveNewVideo(@RequestParam("email")String email,
                                          @RequestParam("videoName")String videoName,
                                          @RequestParam(value = "desc", required = false)String desc,
                                          @RequestParam("file") MultipartFile multipartFile
                                          ){
        if (desc == null){
            desc = "Default description";
        }
        videoService.saveNewVideoFileToDataBase(email, videoName, desc, multipartFile);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/video")
    public ResponseEntity<StreamingResponseBody> getVideoById(
            @RequestParam Long id,
            @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
        return videoService.getVideoFromGoogleCloudById(id, rangeHeader);
    }
}
