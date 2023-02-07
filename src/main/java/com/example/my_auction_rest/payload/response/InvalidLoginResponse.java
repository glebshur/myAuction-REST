package com.example.my_auction_rest.payload.response;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {

    private String email;
    private String password;

    public InvalidLoginResponse(){
        email = "Invalid email";
        password = "Invalid password";
    }
}
