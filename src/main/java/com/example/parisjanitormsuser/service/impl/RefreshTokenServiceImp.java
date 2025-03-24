package com.example.parisjanitormsuser.service.impl;


import com.example.parisjanitormsuser.entity.RefreshToken;
import com.example.parisjanitormsuser.dto.RefreshTokenReq;
import com.example.parisjanitormsuser.dto.RefreshTokenRes;
import com.example.parisjanitormsuser.entity.User;
import com.example.parisjanitormsuser.repository.RefreshTokenRepo;
import com.example.parisjanitormsuser.repository.UserRepo;
import com.example.parisjanitormsuser.security.enums.TokenType;
import com.example.parisjanitormsuser.security.exception.TokenException;
import com.example.parisjanitormsuser.security.jwt.JwtService;
import com.example.parisjanitormsuser.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImp implements RefreshTokenService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private JwtService jwtService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;



    @Override
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken refreshToken = RefreshToken.builder()
                .revoked(false)
                .user(user)
                .token(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()))
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();
        return refreshTokenRepo.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token == null){
            log.error("Token is null");
            throw new TokenException(null, "Token is null");
        }
        if(token.getExpiryDate().compareTo(Instant.now()) < 0 ){
            refreshTokenRepo.delete(token);
            throw new TokenException(token.getToken(), "Refresh token was expired. Please make a new authentication request");
        }
        return token;
    }

    @Override
    public RefreshTokenRes generateNewToken(RefreshTokenReq request) {
        User user = refreshTokenRepo.findByToken(request.getRefreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new TokenException(request.getRefreshToken(),"Refresh token does not exist"));

        String token = jwtService.generateToken(user);
        return RefreshTokenRes.builder()
                .accessToken(token)
                .refreshToken(request.getRefreshToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    @Override
    public List<RefreshToken> getAllRefreshTokens() {
        return refreshTokenRepo.findAll();
    }

    @Override
    public Optional<RefreshToken> deleteRefreshToken(Long id) {
        Optional<RefreshToken> refreshToken = refreshTokenRepo.findById(id);
        refreshToken.ifPresent(refreshTokenRepo::delete);
        return refreshToken;
    }
}
