package com.example.parisjanitormsuser.controller;
import com.example.parisjanitormsuser.dto.*;
import com.example.parisjanitormsuser.security.exception.InvalidDataException;
import com.example.parisjanitormsuser.security.exception.UnauthorizedException;
import com.example.parisjanitormsuser.security.exception.UserAlreadyExistsException;
import com.example.parisjanitormsuser.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Gestion de l'authentification")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "Register a new user", description = "Allows a user to create a new account by providing information such as firstname, lastname, email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = ""),
            @ApiResponse(responseCode = "404", description = "",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping(value="/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<AuthRes>> register(@RequestBody RegisterReq request) {
        try{
            AuthRes result = authService.register(request);
            //tester le retour de result avec le return
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

    @Operation(summary = "Login user", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = ""),
            @ApiResponse(responseCode = "404", description = "",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<AuthRes>> login(@Valid @RequestBody LoginReq request) {
        try{
            AuthRes result = authService.authenticate(request);
            //tester le retour de result avec le return
            return ResponseEntity.ok(new ResponseWrapper<>(true,"authentication success",result));
        }catch (BadCredentialsException e){
            ResponseWrapper<AuthRes> result = new ResponseWrapper<>(false,e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(result);
        }catch (UnauthorizedException e){
            ResponseWrapper<AuthRes> result = new ResponseWrapper<>(false,e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(result);
        }
    }

    @Operation(summary = "Logout user", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = ""),
            @ApiResponse(responseCode = "404", description = "",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping(value="/logout",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<String>> logout(HttpServletRequest request, HttpServletResponse response) {

        try{
            // Don't create session if it not exists
            HttpSession session = request.getSession(false);
            if(session != null){
                session.invalidate();
            }
            SecurityContextHolder.clearContext();
            log.info("User successfully logged out");
            return ResponseEntity.ok(new ResponseWrapper<>(true,"logout success",null));
        } catch(IllegalStateException ex){
            log.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseWrapper<>(false,"Session already invalidate",null));
        } catch(Exception e){
            log.error("Unexpected error during logout", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseWrapper<>(false,"Error during disconnection",null));
        }
    }

}
