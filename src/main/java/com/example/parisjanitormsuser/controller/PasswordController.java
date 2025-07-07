package com.example.parisjanitormsuser.controller;


import com.example.parisjanitormsuser.dto.ResponseWrapper;
import com.example.parisjanitormsuser.entity.User;
import com.example.parisjanitormsuser.repository.UserRepo;
import com.example.parisjanitormsuser.service.impl.EmailService;
import com.example.parisjanitormsuser.service.impl.JwtForMailSender;
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
public class PasswordController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtForMailSender jwtForMailSender;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping(value = "/forgot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<String>> forgotPassword(@RequestBody Map<String, String> request, HttpServletRequest httpServletRequest) throws MessagingException {

        //Verifier le champ email
        log.debug("forgotPassword");
        Optional<User> user = userRepo.findByPrivateInfoEmail(request.get("email"));
        if (user.isPresent()) {
            String token = jwtForMailSender.generateResetToken(request.get("email"));
            emailService.sendResetEmail(request.get("email"), token);
            return ResponseEntity.ok().body(ResponseWrapper.ok(null, "Reset email sent", httpServletRequest.getRequestURI()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseWrapper.error(null, "Email not found", HttpStatus.NOT_FOUND.value(), httpServletRequest.getRequestURI()));
        }
    }

    @PostMapping(value = "/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<String>> resetPassword(@RequestBody Map<String, String> request, HttpServletRequest httpServletRequest) {

        log.info("===reset password===");
        log.debug("token : " + request.get("token") + "/" + request.get("newPassword"));

        String email = jwtForMailSender.extractEmail(request.get("token"));
        log.info("email : " + email);
        Optional<User> userOptional = userRepo.findByPrivateInfoEmail(email);
        log.info("userOptional : " + userOptional.toString());

        if (request.get("token").isBlank() || request.get("newPassword").isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseWrapper.error(null, "Invalid token", HttpStatus.BAD_REQUEST.value(), httpServletRequest.getRequestURI()));
        }
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getPrivateInfo().setPassword(passwordEncoder.encode(request.get("newPassword")));
            userRepo.save(user);
            return ResponseEntity.ok(ResponseWrapper.ok(null, "Mot de passe mis à jour avec succès", httpServletRequest.getRequestURI()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseWrapper.error(null, "Utilisateur non trouve", HttpStatus.NOT_FOUND.value(), httpServletRequest.getRequestURI()));
        }
    }

}


