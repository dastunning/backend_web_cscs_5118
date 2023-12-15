package com.backend.components;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.backend.models.CurrentUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtUtil {
    private String secret = "123456_Ab170321";
    private Long jwtExpirationInMs = 172800000L;
    private Long refreshExpirationDateInMs = 31556952000L;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(Long jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    @Value("${jwt.refreshExpirationDateInMs}")
    public void setRefreshExpirationDateInMs(Long refreshExpirationDateInMs) {
        this.refreshExpirationDateInMs = refreshExpirationDateInMs;
    }

    public String generateToken(CurrentUser currentUser) {
        List<String> roles = currentUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return JWT.create()
                .withSubject(currentUser.getUsername())
                .withClaim("roles", roles)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public String generateRefreshToken(CurrentUser currentUser) {
        return JWT.create()
                .withSubject(currentUser.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public UsernamePasswordAuthenticationToken verifyToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secret.getBytes())).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String userName = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (roles != null) {
            Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        }

        return new UsernamePasswordAuthenticationToken(userName, null, authorities);
    }

    public String extractJwt(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        return "";
    }

}
