package com.example.parisjanitormsuser.service;


import com.example.parisjanitormsuser.entity.RefreshToken;
import com.example.parisjanitormsuser.dto.RefreshTokenReq;
import com.example.parisjanitormsuser.dto.RefreshTokenRes;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);
    RefreshToken verifyExpiration(RefreshToken token);
    RefreshTokenRes generateNewToken(RefreshTokenReq request);
    List<RefreshToken> getAllRefreshTokens();
    Optional<RefreshToken> deleteRefreshToken(Long id);
}
