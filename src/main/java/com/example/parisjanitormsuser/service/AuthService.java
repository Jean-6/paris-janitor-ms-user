package com.example.parisjanitormsuser.service;

import com.example.parisjanitormsuser.dto.AuthResponse;
import com.example.parisjanitormsuser.dto.AuthRequest;
import com.example.parisjanitormsuser.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
}
