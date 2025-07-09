package com.example.parisjanitormsuser.controller;


import com.example.parisjanitormsuser.dto.ResetPasswordReq;
import com.example.parisjanitormsuser.dto.ResponseWrapper;
import com.example.parisjanitormsuser.entity.User;
import com.example.parisjanitormsuser.repository.UserRepo;
import com.example.parisjanitormsuser.service.UserService;
import com.example.parisjanitormsuser.service.impl.EmailService;
import com.example.parisjanitormsuser.service.impl.JwtForMailSender;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
@Tag(name = "Password Controller", description = "Password management ")
public class PassController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtForMailSender jwtForMailSender;

    @Operation(
            summary = "Send reset password email",
            description = "Generates a token and sends a password reset email to the user if the email exists in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reset email sent successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapper.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Email address not found in the system",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapper.class))
            )
    })
    @PostMapping(value = "/send-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<String>> forgotPassword(@RequestParam String email, HttpServletRequest httpServletRequest) throws MessagingException {

        log.debug("Send email for reset password");

        if (userService.findByEmail(email).isPresent()) {
            String token = jwtForMailSender.generateResetToken(email);
            emailService.sendResetEmail(email, token);
            return ResponseEntity.ok()
                    .body(ResponseWrapper.ok("Reset email sent", httpServletRequest.getRequestURI(), null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseWrapper.error(null, "Email not found", HttpStatus.NOT_FOUND.value(), httpServletRequest.getRequestURI()));
        }
    }


    @Operation(
            summary = "Reset user password",
            description = "Resets the password of a user by extracting email from token and updating credentials."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapper.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or token format",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapper.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found with given email",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseWrapper.class))
            )
    })
    @PostMapping(value = "/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<String>> resetPassword(@RequestBody ResetPasswordReq request, HttpServletRequest httpServletRequest) {

        log.info("Reset password");
        log.debug("token : " + request.getToken() + "/" + request.getNewPassword());
        String email = jwtForMailSender.extractEmail(request.getToken());
        log.debug("email : " + email);
        Optional<User> optionalUser = userService.findByEmail(email);
        if (userService.updateUserPassword(optionalUser, request.getNewPassword())) {
            return ResponseEntity.ok(ResponseWrapper.ok("Mot de passe mis à jour avec succès", httpServletRequest.getRequestURI(), null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseWrapper.error(null, "Utilisateur non trouve", HttpStatus.NOT_FOUND.value(), httpServletRequest.getRequestURI()));
        }
    }
}

