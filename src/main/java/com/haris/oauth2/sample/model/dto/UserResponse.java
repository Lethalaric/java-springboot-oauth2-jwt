package com.haris.oauth2.sample.model.dto;

import com.haris.oauth2.sample.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class UserResponse {

    private String username;
    private String[] roles;

    public static UserResponse from(CustomUser customUser) {
        String[] roles = customUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).toArray(new String[0]);
        return UserResponse
                .builder()
                .withUsername(customUser.getUsername())
                .withRoles(roles)
                .build();
    }
}
