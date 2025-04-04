package com.example.parisjanitormsuser.service;

import com.example.parisjanitormsuser.dto.LoginResponse;
import com.example.parisjanitormsuser.dto.LoginRequest;
import com.example.parisjanitormsuser.dto.RegisterRequest;

public interface AuthService {
    LoginResponse register(RegisterRequest request);
    LoginResponse authenticate(LoginRequest request);
}
