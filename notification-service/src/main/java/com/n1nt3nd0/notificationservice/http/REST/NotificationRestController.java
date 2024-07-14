package com.n1nt3nd0.notificationservice.http.REST;

import com.n1nt3nd0.notificationservice.dto.NotificationDto;
import com.n1nt3nd0.notificationservice.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationRestController {
    private final MailService mailService;
    @PostMapping
    public ResponseEntity<?> sendNotification(@RequestBody NotificationDto notificationDto) {
        mailService.sendMessage(
                notificationDto.getEmail(),
                "Subject" +notificationDto.getEmail(),
                notificationDto.getCONFIRMATION_CODE()
        );
        return ResponseEntity.ok("Mail message sent successfully " + notificationDto.getEmail());
    }
    @GetMapping
    public String getGreeting() {
        return "Hello World";
    }
}

