package com.example.my_auction_rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BetDeleteException extends RuntimeException {
    public BetDeleteException(String message) {
        super(message);
    }
}
