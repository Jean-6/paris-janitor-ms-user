package com.example.parisjanitormsuser.dto;


import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthResponse {
    private  Long id;
    private String email;
    private List<String> roles;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
}
