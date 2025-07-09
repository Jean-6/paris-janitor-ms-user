package com.example.parisjanitormsuser.service;

import com.example.parisjanitormsuser.dto.LoginResponse;
import com.example.parisjanitormsuser.dto.LoginRequest;
import com.example.parisjanitormsuser.dto.RegisterRequest;
import com.example.parisjanitormsuser.dto.RegisterResponse;
import com.example.parisjanitormsuser.entity.User;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse authenticate(LoginRequest request);
    public void userSessionCreation(User user);
}
