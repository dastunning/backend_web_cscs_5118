package com.backend.config;

import com.backend.filters.JwtAuthorizationFilter;
import com.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static java.lang.String.format;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Configuration
//@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecureConfig {

    private final UserService userService;
    @Value("${springdoc.api-docs.path}")
    private String restApiDocPath;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                JwtAuthorizationFilter filter = new JwtAuthorizationFilter();
        http = http.cors().and().csrf().disable();
        http.authorizeHttpRequests(requests -> requests
                        .requestMatchers(new AntPathRequestMatcher("/web-backend/public/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/web-backend/api/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/web-backend/auth/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/web-backend/api/posts/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher(format("/web-backend%s/**", restApiDocPath))).permitAll()
                .requestMatchers(new AntPathRequestMatcher(format("/web-backend%s/**", swaggerPath))).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .exceptionHandling()
                .accessDeniedHandler((request, response, e) -> {
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            "Sorry, you don't have the required permission to access this resource.");
                })
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
