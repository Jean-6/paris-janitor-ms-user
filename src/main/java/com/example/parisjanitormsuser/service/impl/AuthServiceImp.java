package com.example.parisjanitormsuser.service.impl;

import com.example.parisjanitormsuser.common.ErrorMsg;
import com.example.parisjanitormsuser.dto.AuthRequest;
import com.example.parisjanitormsuser.dto.AuthResponse;
import com.example.parisjanitormsuser.dto.RegisterRequest;
import com.example.parisjanitormsuser.entity.User;
import com.example.parisjanitormsuser.repository.UserRepo;
import com.example.parisjanitormsuser.security.enums.TokenType;
import com.example.parisjanitormsuser.security.exception.InvalidDataException;
import com.example.parisjanitormsuser.security.exception.UnauthorizedException;
import com.example.parisjanitormsuser.security.exception.UserAlreadyExistsException;
import com.example.parisjanitormsuser.security.jwt.JwtService;
import com.example.parisjanitormsuser.service.AuthService;
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
public class AuthServiceImp implements AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;


    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepo.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(ErrorMsg.USER_ALREADY_EXISTS);
        }

        if (!ValidationUtils.isRegistrationValid(request.getFirstname(),request.getLastname(),request.getEmail(),request.getPassword())) {
            throw new InvalidDataException(ErrorMsg.INVALID_DATA);
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        user = userRepo.save(user);
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
                throw new BadCredentialsException(ErrorMsg.INVALID_DATA);
            }
            // Retrieve user before authentication
            var user = userRepo.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UnauthorizedException(ErrorMsg.BAD_CREDENTIALS));
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
