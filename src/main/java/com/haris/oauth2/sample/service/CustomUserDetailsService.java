package com.haris.oauth2.sample.service;

import com.haris.oauth2.sample.model.dto.CustomUser;
import com.haris.oauth2.sample.model.dto.UserRequest;
import com.haris.oauth2.sample.model.entity.UserEntity;
import com.haris.oauth2.sample.model.enums.Role;
import com.haris.oauth2.sample.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    List<String> recognizedRoles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toList());

    @Override
    public CustomUser loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserEntity userEntity = getUserEntityByUsername(username);
            return new CustomUser(userEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insertUser(UserRequest userRequest) throws Exception {

        if (checkIfUsernameExists(userRequest.getUsername())) {
            throw new Exception("Username already exists, pick another one");
        }

        List<String> authorities = new ArrayList<> (
                List.of(new SimpleGrantedAuthority(Role.ROLE_USER.toString()).getAuthority()));
        if ( userRequest.getRoles() != null) {
            if (!checkIfRoleValid(userRequest.getRoles())) {
                throw new Exception("Roles are not recognized.");
            }

            authorities.addAll(
                    userRequest
                            .getRoles()
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .map(SimpleGrantedAuthority::getAuthority)
                            .collect(Collectors.toList()));
        }

        UserEntity userEntity = UserEntity.builder()
                .withUsername(userRequest.getUsername())
                .withPassword(passwordEncoder.encode(userRequest.getPassword()))
                .withGrantedAuthorities(authorities.toArray(new String[0]))
                .build();

        userRepository.save(userEntity);
    }

    public UserEntity getUserEntityByUsername(String username) throws Exception {
        return userRepository.findByUsername(username).orElseThrow(() -> new Exception("User not found"));
    }

    public boolean checkIfUsernameExists(String username) {
        try {
            getUserEntityByUsername(username);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public void changePassword(String username, String newPassword) throws Exception {
        UserEntity userEntity = getUserEntityByUsername(username);

        String encryptedPassword = passwordEncoder.encode(newPassword);
        userEntity.setPassword(encryptedPassword);
        userRepository.save(userEntity);
    }

    public void addRole(String username, String role) throws Exception {
        UserEntity userEntity = getUserEntityByUsername(username);

        List<String> listOfRole = List.of(role);

        if (!checkIfRoleValid(listOfRole)) {
            throw new Exception("Roles are not recognized.");
        }

        if (checkIfRoleExistsInData(userEntity, listOfRole)) {
            throw new Exception("Role already exists in data");
        }

        List<String> grantedAuthorities = new ArrayList<>(List.of(userEntity.getGrantedAuthorities()));
        grantedAuthorities.add(role);
        String[] roles = grantedAuthorities.toArray(new String[0]);
        userEntity.setGrantedAuthorities(roles);
        userRepository.save(userEntity);
    }

    public void removeRole(String username, String role) throws Exception {
        UserEntity userEntity = getUserEntityByUsername(username);

        List<String> listOfRole = List.of(role);

        if (!checkIfRoleValid(listOfRole)) {
            throw new Exception("Roles are not recognized.");
        }

        if (!checkIfRoleExistsInData(userEntity, listOfRole)) {
            throw new Exception("Role does not exists in data");
        }

        List<String> roles = new ArrayList<>(List.of(userEntity.getGrantedAuthorities()));
        roles.remove(role);

        userEntity.setGrantedAuthorities(roles.toArray(new String[0]));
        userRepository.save(userEntity);
    }

    private boolean checkIfRoleValid(List<String> roles) {
        return CollectionUtils.containsAny(recognizedRoles, roles);
    }

    private boolean checkIfRoleExistsInData(UserEntity userEntity, List<String> role) {
        return CollectionUtils.containsAny(Arrays.asList(userEntity.getGrantedAuthorities()), role);
    }

    public void changeUsername(String username, String newUsername) throws Exception {
        if (username.equalsIgnoreCase(newUsername)) {
            throw new Exception("Do not bother change to the same username");
        }

        if (checkIfUsernameExists(newUsername)) {
            throw new Exception("Username already exists, pick a new one");
        }

        UserEntity userEntity = getUserEntityByUsername(username);

        userEntity.setUsername(newUsername);
        userRepository.save(userEntity);
    }

    public void deleteUser(String username) throws Exception {
        if (!checkIfUsernameExists(username)) {
            throw new Exception("Username does not exists");
        }

        UserEntity userEntity = getUserEntityByUsername(username);

        userRepository.deleteById(userEntity.getId());
    }
}
