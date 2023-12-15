package com.backend.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.backend.components.JwtUtil;
import com.backend.entity.UserEntity;
import com.backend.exceptions.NotFoundException;
import com.backend.models.CurrentUser;
import com.backend.models.requests.RegisterRequest;
import com.backend.repository.UsersRepository;
import com.backend.service.UserService;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final DefaultBearerTokenResolver resolver = new DefaultBearerTokenResolver();
    private final JwtUtil jwtUtil;
    @Value("${jwt.secret}")
    private String secret;

    private static final String USER_IS_NOT_AUTHORIZED = "User not found!";

    @Override
    public ResponseEntity<JSONObject> registration(RegisterRequest request) {
        try {
            UserEntity user = userRepository.findByUsername(request.getUsername()).orElse(new UserEntity());

            user.setName(request.getName());
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEnabled(true);
            user.setRole(request.getRole());
            userRepository.save(user);
            return new ResponseEntity<>(setResult("User created!"), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Can not register user - " + e.getMessage());
            return new ResponseEntity<>(setResult("Can not register user - " + request.getUsername()), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<JSONObject> login(String principal) {
        try {
            String[] principals = decoder(principal);
            String username = principals[0];
            String password = principals[1];
            UserEntity user = getUserByUsername(username);
            UserDetails userDetails = loadUserDetailsByUser(user);
            CurrentUser currentUser = (CurrentUser) userDetails;
            if (passwordEncoder.matches(password, currentUser.getPassword())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("accessToken", jwtUtil.generateToken(currentUser));
                jsonObject.put("refreshToken", jwtUtil.generateRefreshToken(currentUser));
                jsonObject.put("role", user.getRole());
                return new ResponseEntity<>(jsonObject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(setResult("Пароли не совпадают - " + password), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("Неуспешная авторизация - " + e.getMessage());
            return new ResponseEntity<>(setResult("Ошибка в авторизаций - " + e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    private JSONObject setResult(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        return jsonObject;
    }

    @Override
    public UserDetails loadUserDetailsByUser(UserEntity user) throws UsernameNotFoundException {
        return CurrentUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .authorities(Collections.singleton(user.getRole()))
                .modifiedAt(user.getModifiedAt()).build();
    }

    @Override
    public UserEntity getUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь - %s отсутствует в базе", username)));
    }

    @Override
    public UserEntity.Role getUserRole(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Пользователь - %s отсутствует в базе", userId)));
        return user.getRole();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("Пользователь - %s отсутствует в базе", username)));
        Set<UserEntity.Role> userRole = new HashSet<>();
        userRole.add(user.getRole());
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(userRole.stream().map(UserEntity.Role::toString).toArray(String[]::new))
                .build();
    }

    private String[] decoder(String authorization) {
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            return credentials.split(":", 2);
        } else {
            throw new BadCredentialsException("Укажите логин и пароль!");
        }
    }

    @Override
    public UserEntity getUserByRequest(HttpServletRequest httpServletRequest) {
        return getUserByToken(resolver.resolve(httpServletRequest));
    }

    @Override
    public UserEntity getUserByToken(String token) {
        if ((token == null) || token.isEmpty()) {
            throw new AuthorizationServiceException(USER_IS_NOT_AUTHORIZED);
        }
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secret.getBytes())).build();
        DecodedJWT jwt = JWT.decode(token);
        if (jwt.getExpiresAt().before(new Date())) {
            throw new AuthorizationServiceException("Token has expired");
        }
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        return userRepository.findByUsername(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(USER_IS_NOT_AUTHORIZED);
        });
    }

    @Override
    public UserEntity getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(String.format("User - %s does not exist", userId)));
    }

    private String bcryptPasswordEncoder(String password) {
        return "{bcrypt}" + new BCryptPasswordEncoder().encode(password);
    }
}
