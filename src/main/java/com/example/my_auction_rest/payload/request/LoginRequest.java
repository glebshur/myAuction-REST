package com.example.my_auction_rest.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @NotBlank(message = "Email cannot be blank")
    @Schema(example = "test@email.com", minLength = 1)
    private String email;
    @NotBlank(message = "Password cannot be blank")
    @Schema(example = "password", minLength = 1)
    private String password;
}
