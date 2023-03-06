package com.haris.oauth2.sample.controller;

import com.haris.oauth2.sample.model.dto.UserRequest;
import com.haris.oauth2.sample.model.dto.UserResponse;
import com.haris.oauth2.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signUp(@RequestBody UserRequest userRequest) throws Exception {
        userService.insertUser(userRequest);

        return ResponseEntity.ok(userService.getUserEntityByUsername(userRequest.getUsername()));
    }
}
