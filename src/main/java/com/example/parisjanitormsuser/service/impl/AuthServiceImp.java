package com.example.parisjanitormsuser.service.impl;

import com.example.parisjanitormsuser.common.ErrorMsg;
import com.example.parisjanitormsuser.dto.LoginReq;
import com.example.parisjanitormsuser.dto.AuthRes;
import com.example.parisjanitormsuser.dto.RegisterReq;
import com.example.parisjanitormsuser.entity.PrivateInfo;
import com.example.parisjanitormsuser.entity.ProfileInfo;
import com.example.parisjanitormsuser.entity.Session;
import com.example.parisjanitormsuser.entity.User;
import com.example.parisjanitormsuser.repository.SessionRepo;
import com.example.parisjanitormsuser.repository.UserRepo;
import com.example.parisjanitormsuser.security.enums.Role;
import com.example.parisjanitormsuser.security.enums.TokenType;
import com.example.parisjanitormsuser.security.exception.InvalidDataException;
import com.example.parisjanitormsuser.security.exception.UnauthorizedException;
import com.example.parisjanitormsuser.security.exception.UserAlreadyExistsException;
import com.example.parisjanitormsuser.security.jwt.JwtService;
import com.example.parisjanitormsuser.service.AuthService;
import com.example.parisjanitormsuser.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
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
    private SessionRepo sessionRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthRes register(RegisterReq request) {

        log.debug("register : {}", request.toString());

        if (userRepo.existsByPrivateInfoEmail(request.getPrivateInfo().getEmail())) {
            throw new UserAlreadyExistsException(ErrorMsg.USER_ALREADY_EXISTS);
        }
        if (!ValidationUtils.isRegistrationValid(
                request.getProfileInfo().getUsername(),
                request.getPrivateInfo().getEmail(),
                request.getPrivateInfo().getPassword())){
            throw new InvalidDataException(ErrorMsg.INVALID_DATA);
        }
        var user = User.builder()
                .profileInfo(
                        new ProfileInfo(request.getProfileInfo().getUsername(),Role.USER))
                .privateInfo(
                        new PrivateInfo(request.getPrivateInfo().getEmail(),passwordEncoder.encode(request.getPrivateInfo().getPassword())))
                        .build();

        log.debug("user to save : "+user.toString());
        user = userRepo.save(user);
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        var roles = user.getProfileInfo().getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        return AuthRes.builder()
                .id(user.getId())
                .accessToken(jwt)
                .email(user.getPrivateInfo().getEmail())
                .profileInfo(user.getProfileInfo())
                .refreshToken(refreshToken.getToken())
                .tokenType(TokenType.BEARER)
                .build();
    }

    public void userSessionCreation(User user){
        //Creation de session
        Session session = new Session();
        session.setUser(user);
        session.setCreatedAt(Instant.now());
        session.setExpires_at(Instant.now().plus(1,ChronoUnit.HOURS));
        sessionRepo.save(session);
    }

    @Override
    public AuthRes authenticate(LoginReq request) {

        log.debug("login : "+ request.toString());
        try{
            if (!ValidationUtils.isLoginValid(request.getEmail(),request.getPassword())) {
                throw new BadCredentialsException(ErrorMsg.INVALID_DATA);
            }
            // Retrieve user before authentication
            var user = userRepo.findByPrivateInfoEmail(request.getEmail())
                    .orElseThrow(() -> {
                        log.error("user not found");
                        return new UnauthorizedException(ErrorMsg.BAD_CREDENTIALS);
                    });
            log.debug(user.toString());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            log.debug("login 1: "+ user.toString());
            // Session creation
            userSessionCreation(user);
            // Token generation
            String jwt = jwtService.generateToken(user);
            String refreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();
            // Response building
            return AuthRes.builder()
                    .id(user.getId())
                    .email(user.getPrivateInfo().getEmail())
                    .profileInfo(user.getProfileInfo())
                    .accessToken(jwt)
                    .refreshToken(refreshToken)
                    .tokenType(TokenType.BEARER)
                    .build();
        } catch (BadCredentialsException ex){
            throw new UnauthorizedException(ex.getMessage());
        }
    }
}
