package com.haris.oauth2.sample.model.dto;

import com.haris.oauth2.sample.model.entity.UserEntity;
import org.springframework.security.core.userdetails.User;

public class CustomUser extends User {
    public CustomUser(UserEntity userEntity) {
        super(userEntity.getUsername(), userEntity.getPassword(), userEntity.getGrantedAuthorities());
    }
}
