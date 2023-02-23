package com.haris.oauth2.sample.controller;

import com.haris.oauth2.sample.model.dto.UserRequest;
import com.haris.oauth2.sample.model.dto.UserResponse;
import com.haris.oauth2.sample.model.entity.UserEntity;
import com.haris.oauth2.sample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) throws Exception {
        userService.insertUser(userRequest);

        return ResponseEntity.ok(UserResponse.from(userService.getUserEntityByUsername(userRequest.getUsername())));
    }
}
