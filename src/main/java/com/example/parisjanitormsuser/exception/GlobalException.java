package com.example.parisjanitormsuser.exception;



import com.example.parisjanitormsuser.common.ErrorMsg;
import com.example.parisjanitormsuser.dto.ErrorRes;
import com.example.parisjanitormsuser.dto.ResponseWrapper;
import com.example.parisjanitormsuser.security.exception.BadCredentialsException;
import com.example.parisjanitormsuser.security.exception.InvalidDataException;
import com.example.parisjanitormsuser.security.exception.UnauthorizedException;
import com.example.parisjanitormsuser.security.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalException {


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper<Object>> userAlreadyExistsException(UserAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ResponseWrapper.conflict(ErrorMsg.USER_ALREADY_EXISTS));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseWrapper<Object>> badCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseWrapper.badRequest(ErrorMsg.BAD_CREDENTIALS));
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ResponseWrapper<Object>> invalidData(InvalidDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseWrapper.badRequest(ErrorMsg.BAD_CREDENTIALS));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> unauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseWrapper.unauthorized(ErrorMsg.UNAUTHORIZED));
    }
}
