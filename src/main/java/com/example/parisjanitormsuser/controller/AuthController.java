package com.example.parisjanitormsuser.controller;
import com.example.parisjanitormsuser.dto.AuthRequest;
import com.example.parisjanitormsuser.dto.AuthResponse;
import com.example.parisjanitormsuser.dto.RegisterRequest;
import com.example.parisjanitormsuser.dto.ResponseWrapper;
import com.example.parisjanitormsuser.security.exception.InvalidDataException;
import com.example.parisjanitormsuser.security.exception.UnauthorizedException;
import com.example.parisjanitormsuser.security.exception.UserAlreadyExistsException;
import com.example.parisjanitormsuser.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping(value="/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<AuthResponse>> register(@RequestBody RegisterRequest request) {
        try{
            AuthResponse result = authService.register(request);
            return ResponseEntity.ok(new ResponseWrapper<>(true,"registration success",result));
        }catch(UserAlreadyExistsException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseWrapper<>(false,ex.getMessage(),null));
        }catch(InvalidDataException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>(false,ex.getMessage(),null));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper<>(false,ex.getMessage(),null));

        }
    }

    @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        try{
            AuthResponse result = authService.authenticate(request);
            return ResponseEntity.ok(new ResponseWrapper<>(true,"authentication success",result));
        }catch (BadCredentialsException e){
            ResponseWrapper<AuthResponse> result = new ResponseWrapper<>(false,e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(result);
        }catch (UnauthorizedException e){
            ResponseWrapper<AuthResponse> result = new ResponseWrapper<>(false,e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(result);
        }
    }

    @PostMapping(value="/logout",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        // Supprimer  la session et l'authentification
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ResponseWrapper<>(true,"Deconnexion résussie",null));
    }

}
