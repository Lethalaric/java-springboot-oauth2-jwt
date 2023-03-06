package com.haris.oauth2.sample.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserChangeRequest {

    private String password;

    private String username;

    private String role;

    public static boolean checkUsernameIsNullOrEmpty(UserChangeRequest userChangeRequest) {
        return StringUtils.isBlank(userChangeRequest.username);
    }

    public static boolean checkPasswordIsNullOrEmpty(UserChangeRequest userChangeRequest) {
        return StringUtils.isBlank(userChangeRequest.password);
    }

    public static boolean checkRoleIsNullOrEmpty(UserChangeRequest userChangeRequest) {
        return StringUtils.isBlank(userChangeRequest.role);
    }
}
