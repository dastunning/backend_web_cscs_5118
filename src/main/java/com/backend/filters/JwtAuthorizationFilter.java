package com.backend.filters;

import com.backend.components.JwtUtil;
import com.backend.models.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Log
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil = new JwtUtil();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().startsWith("/promocode/auth") ||
                request.getServletPath().startsWith("/promocode/registration")
                || request.getServletPath().startsWith(format("/%s", "rest-api-docs")) || request.getServletPath().startsWith(format("/%s", "swagger-ui"))
        ) {
            filterChain.doFilter(request, response);
        } else {
            String token = jwtUtil.extractJwt(request);
            if (!StringUtils.isEmpty(token)) {
                try {

                    SecurityContextHolder.getContext().setAuthentication(jwtUtil.verifyToken(token));
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    log.info("doFilterInternal - " + exception.getMessage());
                    response.setHeader("error", exception.getMessage());
                    response.sendError(UNAUTHORIZED.value());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), ErrorResponse.builder().message(exception.getMessage()).build());
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
