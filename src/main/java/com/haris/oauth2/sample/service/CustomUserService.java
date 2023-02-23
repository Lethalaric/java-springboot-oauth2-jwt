package com.haris.oauth2.sample.service;

import com.haris.oauth2.sample.model.dto.CustomUser;
import com.haris.oauth2.sample.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private UserService userService;

    @Override
    public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserEntity userEntity = userService.getUserEntityByUsername(username);
            return new CustomUser(userEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
