package com.example.parisjanitormsuser.controller;


import com.example.parisjanitormsuser.dto.RefreshTokenReq;
import com.example.parisjanitormsuser.dto.RefreshTokenRes;
import com.example.parisjanitormsuser.dto.ResponseWrapper;
import com.example.parisjanitormsuser.entity.RefreshToken;
import com.example.parisjanitormsuser.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/refresh-token")
@RequiredArgsConstructor
public class TokenController {


    @Autowired
    private RefreshTokenService refreshTokenService;


    @PostMapping(value="/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<RefreshTokenRes>> refreshToken(@RequestBody RefreshTokenReq request) {
        RefreshTokenRes refreshTokenRes = refreshTokenService.generateNewToken(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseWrapper<>(true,"",refreshTokenRes));

    }

    @GetMapping(value="/",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<List<RefreshToken>>> getAllTokens() {
        List< RefreshToken> refreshTokenList = refreshTokenService.getAllRefreshTokens();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseWrapper<>(true,"",refreshTokenList));
    }

    @DeleteMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper<Optional<RefreshToken>>> deleteRefreshToken(@PathVariable("id") Long id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseWrapper<>(true,"",refreshTokenService.deleteRefreshToken(id)));

    }
}
