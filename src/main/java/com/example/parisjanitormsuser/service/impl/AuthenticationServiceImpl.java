package com.example.parisjanitormsuser.service.impl;

import com.example.parisjanitormsuser.dto.AuthRequest;
import com.example.parisjanitormsuser.dto.AuthResponse;
import com.example.parisjanitormsuser.dto.RegisterRequest;
import com.example.parisjanitormsuser.entity.User;
import com.example.parisjanitormsuser.repository.UserRepository;
import com.example.parisjanitormsuser.security.enums.TokenType;
import com.example.parisjanitormsuser.security.exception.InvalidDataException;
import com.example.parisjanitormsuser.security.exception.UnauthorizedException;
import com.example.parisjanitormsuser.security.exception.UserAlreadyExistsException;
import com.example.parisjanitormsuser.security.jwt.JwtService;
import com.example.parisjanitormsuser.service.AuthenticationService;
import com.example.parisjanitormsuser.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    public boolean isValidRequest(RegisterRequest request) {

        if (request == null) {
            return false;
        }

        // Vérification des champs obligatoires
        if (request.getFirstname() == null || request.getFirstname().isBlank()) return false;
        if (request.getLastname() == null || request.getLastname().isBlank()) return false;
        if (request.getEmail() == null || request.getEmail().isBlank()) return false;
        if (request.getPassword() == null || request.getPassword().isBlank()) return false;
        if (request.getRole() == null) return false;

        /* Email and Password validation to manage from frontend
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailRegex, request.getEmail())) {
            return false;
        }
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        if (!Pattern.matches(passwordRegex, request.getPassword())) {
            return false;
        }*/

        return true;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Cet utilisateur existe déjà");
        }
        if (!isValidRequest((request))) {
            throw new InvalidDataException("Les données fournies ne sont pas valides.");
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        user = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        return AuthResponse.builder()
                .accessToken(jwt)
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .tokenType(TokenType.BEARER.name())
                .build();
    }


    @Override
    public AuthResponse authenticate(AuthRequest request) {

        try{
            if (!ValidationUtils.isLoginValid(request.getEmail(),request.getPassword())) {
                throw new BadCredentialsException("Les données fournies ne sont pas valides.");
            }
            // Retrieve user before authentication
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UnauthorizedException("Email ou mot de passe incorrect."));
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            // Token generation
            String jwt = jwtService.generateToken(user);
            String refreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();

            // Response building
            return AuthResponse.builder()
                    .accessToken(jwt)
                    .refreshToken(refreshToken)
                    .email(user.getEmail())
                    .id(user.getId())
                    .tokenType(TokenType.BEARER.name())
                    .build();
        } catch (BadCredentialsException ex){
            throw new UnauthorizedException(ex.getMessage());
        }
    }
}
