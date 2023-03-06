package com.haris.oauth2.sample.controller;

import com.haris.oauth2.sample.model.dto.UserChangeRequest;
import com.haris.oauth2.sample.model.dto.UserResponse;
import com.haris.oauth2.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/user-info")
    public ResponseEntity<UserResponse> userInfo(@RequestHeader String authorization) throws Exception {
        return ResponseEntity.ok(userService.getUserFromAuthorization(authorization));
    }

    @GetMapping("/get-user/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(userService.getUserEntityByUsername(username));
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changeUserPassword(@RequestBody UserChangeRequest userChangeRequest) throws Exception {

        if (UserChangeRequest.checkUsernameIsNullOrEmpty(userChangeRequest)) {
            throw new Exception("Username cannot be null");
        }

        if (UserChangeRequest.checkPasswordIsNullOrEmpty(userChangeRequest)) {
            throw new Exception("Password cannot be null");
        }

        userService.changePassword(userChangeRequest.getUsername(), userChangeRequest.getPassword());

        return ResponseEntity.ok(String.format("Password for user '%s' has been changed", userChangeRequest.getUsername()));
    }

    @PutMapping("/add-role")
    public ResponseEntity<String> addUserRole(@RequestBody UserChangeRequest userChangeRequest) throws Exception {

        if (UserChangeRequest.checkUsernameIsNullOrEmpty(userChangeRequest)) {
            throw new Exception("Username cannot be null");
        }

        if (UserChangeRequest.checkRoleIsNullOrEmpty(userChangeRequest)) {
            throw new Exception("Role cannot be null");
        }

        userService.addRole(userChangeRequest.getUsername(), userChangeRequest.getRole());

        return ResponseEntity.ok("Role has been added");
    }

    @PutMapping("/remove-role")
    public ResponseEntity<String> removeUserRole(@RequestBody UserChangeRequest userChangeRequest) throws Exception {
        if (UserChangeRequest.checkUsernameIsNullOrEmpty(userChangeRequest)) {
            throw new Exception("Username cannot be null");
        }

        if (UserChangeRequest.checkRoleIsNullOrEmpty(userChangeRequest)) {
            throw new Exception("Password cannot be null");
        }

        userService.removeRole(userChangeRequest.getUsername(), userChangeRequest.getRole());

        return ResponseEntity.ok("Role has been removed");
    }

    @PutMapping("/change-username/{username}")
    public ResponseEntity<String> changeUsername(@PathVariable String username, @RequestBody UserChangeRequest userChangeRequest) throws Exception {
        if (UserChangeRequest.checkUsernameIsNullOrEmpty(userChangeRequest)) {
            throw new Exception("New username cannot be null");
        }
        userService.changeUsername(username, userChangeRequest.getUsername());

        return ResponseEntity.ok("Username has been changed");
    }

    @GetMapping("/check-user-exists/{username}")
    public ResponseEntity<Boolean> checkIfUserExists(@PathVariable String username) {
        return ResponseEntity.ok(userService.checkIfUserExists(username));
    }

    @DeleteMapping("/delete-user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) throws Exception {
        userService.deleteUser(username);

        return ResponseEntity.ok("User has been deleted");
    }

}
