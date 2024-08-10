package com.n1nt3nd0.social_media_app.http.Rest;

import com.n1nt3nd0.social_media_app.dto.request.UserCreateEditDto;
import com.n1nt3nd0.social_media_app.dto.response.UserReadDto;
import com.n1nt3nd0.social_media_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-service")
//@RefreshScope // host_application:port_application/actuator/refresh POST method
public class UserRestController {
    private final UserService userService;
    @Value("${greeting}")
    private String greeting;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> saveUser(@RequestBody @Valid UserCreateEditDto dto){
        UserReadDto userReadDto = userService.saveUser(dto);
        return new ResponseEntity<>(userReadDto, HttpStatus.CREATED);
    }
    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getUserById(@PathVariable("id") long id){
        UserReadDto userById = userService.getUserById(id);
        return ResponseEntity.ok(userById);
    }
    @PutMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody @Valid UserCreateEditDto dto){
        UserReadDto userReadDto = userService.updateUserById(id, dto);
        return ResponseEntity.ok(userReadDto);
    }
//    @GetMapping("/greeting")
//    public ResponseEntity<?> getGreeting(){
//        return ResponseEntity.ok(greeting);
//    }
    @GetMapping("/hello")
    public ResponseEntity<?> getGreeting(){
        return ResponseEntity.ok("Hello from user-service.");
    }
}
