package com.haris.oauth2.sample.model.dto;

import com.haris.oauth2.sample.model.entity.UserEntity;
import com.haris.oauth2.sample.model.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomUser extends User {
    public CustomUser(UserEntity userEntity) {
        super(userEntity.getUsername(), userEntity.getPassword(), Stream.of(userEntity.getGrantedAuthorities()).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}
