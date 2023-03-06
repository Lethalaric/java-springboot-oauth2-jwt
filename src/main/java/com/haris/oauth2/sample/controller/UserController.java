package com.haris.oauth2.sample.controller;

import com.haris.oauth2.sample.model.dto.UserChangeRequest;
import com.haris.oauth2.sample.model.dto.UserResponse;
import com.haris.oauth2.sample.model.entity.UserEntity;
import com.haris.oauth2.sample.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user-info")
    public ResponseEntity<UserResponse> userInfo(@RequestHeader String authorization) throws Exception {
        return ResponseEntity.ok(userService.getUserFromAuthorization(authorization));
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestHeader String authorization, @RequestBody UserChangeRequest userChangeRequest) throws Exception {
        if (UserChangeRequest.checkPasswordIsNullOrEmpty(userChangeRequest)) {
            throw new Exception("Password cannot be null");
        }

        UserResponse userResponse = userService.getUserFromAuthorization(authorization);
        userService.changePassword(userResponse.getUsername(), userChangeRequest.getPassword());

        return ResponseEntity.ok("Password has been changed");
    }


}
