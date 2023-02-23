package com.haris.oauth2.sample.service;

import com.haris.oauth2.sample.model.dto.UserRequest;
import com.haris.oauth2.sample.model.entity.UserEntity;
import com.haris.oauth2.sample.model.enums.Role;
import com.haris.oauth2.sample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;
    Object[] roles = Arrays.stream(Role.values()).toArray();

    public void insertUser(UserRequest userRequest) throws Exception {
        if (!new HashSet<>(userRequest.getRoles()).containsAll(List.of(roles))) {
            throw new Exception("Roles are not recognized.");
        }

        Collection<GrantedAuthority> authorities = userRequest.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        UserEntity userEntity = UserEntity.builder()
                .withUsername(userRequest.getUsername())
                .withPassword(userRequest.getPassword())
                .withGrantedAuthorities(authorities)
                .build();

        userRepository.save(userEntity);
    }

    public UserEntity getUserEntityByUsername(String username) throws Exception {
        return userRepository.findByUsername(username).orElseThrow(() -> new Exception("User not found"));
    }
}
