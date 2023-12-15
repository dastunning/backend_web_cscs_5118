package com.backend.models.requests;

import com.backend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String name;
    @NotBlank(message = "Please, provide username")
    private String username;
    @NotBlank(message = "Please, provide password")
    private String password;

    private UserEntity.Role role;
}

