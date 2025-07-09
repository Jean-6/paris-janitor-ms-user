package com.example.parisjanitormsuser.controller;

import com.example.parisjanitormsuser.dto.*;
import com.example.parisjanitormsuser.service.AuthService;
import com.example.parisjanitormsuser.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Gestion de l'authentification")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private SessionService sessionService;

    @Operation(summary = "Register a new user", description = "Allows a user to create a new account by providing firstname, lastname, email, and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data provided",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
    })
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        log.debug("register route");
        RegisterResponse registerResponse = authService.register(request);
        return ResponseEntity.ok(ResponseWrapper.ok("registration success",registerResponse));
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
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        log.debug("login route");
        LoginResponse result = authService.authenticate(request);
        return ResponseEntity.ok(ResponseWrapper.ok("login success",httpServletRequest.getRequestURI(), result ));
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
    @PostMapping(value = "/logout/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<String>> logout(@PathVariable Long id, HttpServletRequest request) {

        log.debug("logout route");
        // Don't create session if it not exists
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        sessionService.revokeSessionById(id);
        SecurityContextHolder.clearContext();
        log.info("User successfully logged out");
        return ResponseEntity.ok(ResponseWrapper.ok("successful disconnection", request.getRequestURI()));

    }

}
