package com.example.my_auction_rest.payload.request;

import com.example.my_auction_rest.annotation.PasswordMatches;
import com.example.my_auction_rest.annotation.ValidEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignUpRequest {

    @NotBlank(message = "Please, enter your username")
    @Schema(example = "username", minLength = 1)
    private String username;

    @NotBlank(message = "Please, enter your email")
    @ValidEmail(message = "Email must be valid (like \"example@mail.com\")")
    @Schema(example = "example@mail.com")
    private String email;

    @NotBlank(message = "Please, enter your password")
    @Size(min = 8,message = "Password must have at least 8 characters")
    private String password;

    @Schema(description = "Must match the password")
    private String confirmPassword;
}
