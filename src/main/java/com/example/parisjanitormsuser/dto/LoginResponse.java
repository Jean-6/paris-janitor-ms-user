package com.example.parisjanitormsuser.dto;


import com.example.parisjanitormsuser.entity.ProfileInfo;
import com.example.parisjanitormsuser.security.enums.TokenType;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginResponse {
    private  Long id;
    private String email;
    private ProfileInfo profileInfo;
    //private List<String> roles;
    private String accessToken;
    private String refreshToken;
    private TokenType tokenType;
}
