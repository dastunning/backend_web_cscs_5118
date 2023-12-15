package com.backend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.backend.components.JwtUtil;
import com.backend.entity.UserEntity;
import com.backend.exceptions.UnsupportedJwtException;
import com.backend.models.CurrentUser;
import com.backend.models.requests.RegisterRequest;
import com.backend.models.response.AuthResponse;
import com.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = "Auth")
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    @Value("${jwt.secret}")
    private String secret;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<JSONObject> register(@Validated @RequestBody RegisterRequest request) {
        log.info("RegisterRequest - " + request.toString());
        return userService.registration(request);
    }

    @PostMapping("/login")
    public ResponseEntity<JSONObject> login(HttpServletRequest request){
        log.info("LoginRequest - " + request.toString());
        return userService.login(request.getHeader("authorization"));

    }

    @SneakyThrows
    @PostMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.extractJwt(request);
        if (!StringUtils.isEmpty(refreshToken)) {
            try{
                JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secret.getBytes())).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String userName = decodedJWT.getSubject();
                UserEntity user = userService.getUserByUsername(userName);
                CurrentUser currentUser = (CurrentUser) userService.loadUserDetailsByUser(user);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        AuthResponse.builder().accessToken(jwtUtil.generateToken(currentUser)).refreshToken(refreshToken).build());
            }catch (Exception exception){
                throw new ValidationException("Token is wrong or expired");
            }
        }else {
            throw new UnsupportedJwtException("Refresh token is missing!");
        }
    }
}
