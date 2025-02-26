package com.example.parisjanitormsuser.service.impl;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtForMailSender {

   //@Value("${application.security.jwt.secret-key}")
   //private String secretKey;
   private final String SECRET_KEY = "4b50dcc5-b6c9-49a9-af2c-8d7d8a416e3b";

    public String generateResetToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ 60 * 60 * 1000)) // Expiration dans 10 min
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
        .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)//Use Jws instead of Jwt to parse full JWT by including signature
                .getBody()
                .getSubject();
    }

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(this.SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }


}
