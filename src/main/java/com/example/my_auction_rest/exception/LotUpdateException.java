package com.example.my_auction_rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LotUpdateException extends RuntimeException {
    public LotUpdateException(String message) {
        super(message);
    }
}
