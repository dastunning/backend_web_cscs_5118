package com.backend.models.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TokenResponse {
    private String accessToken;
}
