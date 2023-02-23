package com.haris.oauth2.sample.model.dto;

import com.haris.oauth2.sample.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class UserResponse {

    private String username;
    private String password;

    public static UserResponse from(UserEntity userEntity) {
        return UserResponse
                .builder()
                .withUsername(userEntity.getUsername())
                .withPassword(userEntity.getPassword())
                .build();
    }
}
