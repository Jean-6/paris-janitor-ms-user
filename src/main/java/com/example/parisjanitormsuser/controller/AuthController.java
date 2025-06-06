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

    @Operation(summary = "Register a new user", description = "Allows a user to create a new account by providing firstname, lastname, email, and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful",
                    content = @Content(schema = @Schema(implementation = AuthRes.class))),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data provided",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
    })
    @PostMapping(value="/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<AuthRes>> register(@RequestBody RegisterReq request) {
        log.debug("register route");
        try{
            AuthRes authRes = authService.register(request);
            return ResponseEntity.ok(ResponseWrapper.ok(authRes,"registration success"));
        }catch(UserAlreadyExistsException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ResponseWrapper.conflict(ex.getMessage()));
        }catch(InvalidDataException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseWrapper.badRequest(ex.getMessage()));
        }catch (Exception ex){
            log.error("Error when registration : {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseWrapper.internalServerError(ex.getMessage()));

        }
    }

    @Operation(summary = "Login user", description = "Authenticate user credentials and return authentication details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials provided",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
    })
    @PostMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<AuthRes>> login(@Valid @RequestBody LoginReq request) {
        log.debug("login route");
        try{
            AuthRes result = authService.authenticate(request);
            return ResponseEntity.ok(ResponseWrapper.ok(result,"login success"));
        }catch (BadCredentialsException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseWrapper.badRequest(ex.getMessage()));
        }catch (UnauthorizedException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseWrapper.badRequest(ex.getMessage()));
        }
    }

    @Operation(summary = "Logout user", description = "Invalidate the user's session and clear security context.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Session already invalidated",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error during logout",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
    })
    @PostMapping(value="/logout",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<String>> logout(HttpServletRequest request, HttpServletResponse response) {

        log.debug("logout route");
        try{
            // Don't create session if it not exists
            HttpSession session = request.getSession(false);
            if(session != null){
                session.invalidate();
            }
            SecurityContextHolder.clearContext();
            log.info("User successfully logged out");
            return ResponseEntity.ok(ResponseWrapper.ok("","registration success"));
        } catch(IllegalStateException ex){
            log.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseWrapper.badRequest(ex.getMessage()));
        } catch(Exception ex){
            log.error("Unexpected error during logout", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseWrapper.internalServerError(ex.getMessage()));
        }

    }

}
