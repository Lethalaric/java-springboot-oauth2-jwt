package com.haris.oauth2.sample.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haris.oauth2.sample.model.dto.CustomUser;
import com.haris.oauth2.sample.model.dto.UserRequest;
import com.haris.oauth2.sample.model.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class UserService {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Value("${oauth2.secret}")
    private String secret;

    public void insertUser(UserRequest userRequest) throws Exception {
        customUserDetailsService.insertUser(userRequest);
    }
    public UserResponse getUserEntityByUsername(String username) {
        CustomUser customUser = customUserDetailsService.loadUserByUsername(username);

        return UserResponse.from(customUser);
    }

    private String getUsernameFromAuthorization(String authorizationToken) throws Exception {
        if (!authorizationToken.toLowerCase().startsWith("bearer")) {
            throw new Exception("Authorization prefix does not provide 'Bearer'");
        }

        String token = authorizationToken.substring("Bearer ".length());
        String claims = JwtHelper.decode(token).getClaims();
        ObjectMapper objectMapper = new ObjectMapper();
        Map jsonString = objectMapper.readValue(claims, Map.class);

        return jsonString.get("user_name").toString();
    }

    public UserResponse getUserFromAuthorization(String authorizationToken) throws Exception {
        String username = getUsernameFromAuthorization(authorizationToken);

        return getUserEntityByUsername(username);
    }

    public void changePassword(String username, String newPassword) throws Exception {
        customUserDetailsService.changePassword(username, newPassword);
    }

    public void changeUsername(String username, String newUsername) throws Exception {
        customUserDetailsService.changeUsername(username, newUsername);
    }

    public void addRole(String username, String role) throws Exception {
        customUserDetailsService.addRole(username, role);
    }

    public void removeRole(String username, String role) throws Exception {
        customUserDetailsService.removeRole(username, role);
    }

    public void deleteUser(String username) throws Exception {
        customUserDetailsService.deleteUser(username);
    }

    public boolean checkIfUserExists(String username) {
        return customUserDetailsService.checkIfUsernameExists(username);
    }
}
