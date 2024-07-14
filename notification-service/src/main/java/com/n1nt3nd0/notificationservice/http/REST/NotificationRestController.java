package com.n1nt3nd0.notificationservice.http.REST;

import com.n1nt3nd0.notificationservice.dto.NotificationDto;
import com.n1nt3nd0.notificationservice.dto.ResponseDto;
import com.n1nt3nd0.notificationservice.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationRestController {
    private final MailService mailService;
    @PostMapping("/code")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationDto notificationDto) {
        try {
            mailService.sendMessage(
                    notificationDto.getEmail(),
                    "Subject" + notificationDto.getEmail(),
                    notificationDto.getCONFIRMATION_CODE()
            );
            ResponseDto responseDto = ResponseDto.builder()
                    .message("Notification sent to %s successfully".formatted(notificationDto.getEmail()))
                    .status(HttpStatus.OK)
                    .build();
            return ResponseEntity.ok(responseDto);
        }catch (Exception e){
            ResponseDto error_response = ResponseDto.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error while sent email message" + e.getMessage())
                    .build();
            return new ResponseEntity<>(error_response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public String getGreeting() {
        return "Hello World";
    }

}

