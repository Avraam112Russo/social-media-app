package com.n1nt3nd0.authservice.feignClient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "email-service", url = "http://localhost:8083/")
public interface EmailServiceFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/notification")
    String getGreeting();
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/notification/code")
    ResponseEntity<ResponseDto> sendMessageToNotificationService(@RequestBody NotificationDto dto);
}
