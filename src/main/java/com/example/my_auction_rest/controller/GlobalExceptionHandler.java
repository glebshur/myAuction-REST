package com.example.my_auction_rest.controller;

import com.example.my_auction_rest.exception.*;
import com.example.my_auction_rest.service.LotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


// Controller that handles custom exceptions
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({BetCreateException.class, BetDeleteException.class, ImageUploadException.class,
            LotCreateException.class, LotDeleteException.class, LotUpdateException.class,
            UserCreateException.class, UserUpdateException.class, WinningBetCreateException.class})
    public ResponseEntity<Object> handle400CustomException(Exception ex, WebRequest request) {
        LOG.error(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({BetNotFoundException.class, ImageNotFoundException.class, LotNotFoundException.class,
            UserNotFoundException.class})
    public ResponseEntity<Object> handle404CustomException(Exception ex, WebRequest request) {
        LOG.error(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request){
        LOG.error(ex.getMessage());
        return new ResponseEntity<>(
                "You do not have enough authority to go to this url", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
}
