package com.example.parisjanitormsuser.controller;

import com.example.parisjanitormsuser.dto.AuthRequest;
import com.example.parisjanitormsuser.dto.AuthResponse;
import com.example.parisjanitormsuser.dto.RegisterRequest;
import com.example.parisjanitormsuser.dto.ResultWrapper;
import com.example.parisjanitormsuser.security.exception.InvalidDataException;
import com.example.parisjanitormsuser.security.exception.UnauthorizedException;
import com.example.parisjanitormsuser.security.exception.UserAlreadyExistsException;
import com.example.parisjanitormsuser.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping(value="/register", produces ="application/json")
    public ResponseEntity<ResultWrapper<AuthResponse>> register( @RequestBody RegisterRequest request) {
        try{
            AuthResponse result = authenticationService.register(request);
            return ResponseEntity.ok(new ResultWrapper<>(true,"registration success",result));
        }catch(UserAlreadyExistsException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResultWrapper<>(false,ex.getMessage(),null));
        }catch(InvalidDataException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResultWrapper<>(false,ex.getMessage(),null));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResultWrapper<>(false,ex.getMessage(),null));

        }
    }

    @PostMapping(value="/login", produces ="application/json")
    public ResponseEntity<ResultWrapper<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {

        try{
            AuthResponse result = authenticationService.authenticate(request);
            return ResponseEntity.ok(new ResultWrapper<>(true,"authentication success",result));
        }catch (BadCredentialsException e){
            ResultWrapper<AuthResponse> result = new ResultWrapper<>(false,e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(result);
        }catch (UnauthorizedException e){
            ResultWrapper<AuthResponse> result = new ResultWrapper<>(false,e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(result);
        }
    }

}
