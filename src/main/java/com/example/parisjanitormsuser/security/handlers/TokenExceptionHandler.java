package com.example.parisjanitormsuser.security.handlers;

import com.example.parisjanitormsuser.dto.ResponseWrapper;
import com.example.parisjanitormsuser.security.exception.TokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class TokenExceptionHandler {


    @ExceptionHandler(value = TokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseWrapper<String>> handleRefreshTokenException(TokenException ex, HttpServletRequest request){
        ResponseWrapper<String> responseWrapper = ResponseWrapper.error(
                null,
                "Invalid Token" + ex.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                request.getRequestURI()
        );
        return  ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(responseWrapper);
    }
}
