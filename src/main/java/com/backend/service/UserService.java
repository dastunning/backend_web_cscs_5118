package com.backend.service;

import com.backend.entity.UserEntity;
import com.backend.models.requests.RegisterRequest;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends UserDetailsService {
    ResponseEntity<JSONObject> registration(RegisterRequest request);

    ResponseEntity<JSONObject> login(String principal);

    UserDetails loadUserDetailsByUser(UserEntity user) throws UsernameNotFoundException;

    UserEntity getUserByUsername(String username) throws UsernameNotFoundException;

    UserEntity.Role getUserRole(Long userId);

    UserEntity getUserByRequest(HttpServletRequest httpServletRequest);

    UserEntity getUserByToken(String token);

    UserEntity getUserById(Long userId);
}
