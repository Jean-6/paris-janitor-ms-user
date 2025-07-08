package com.example.parisjanitormsuser.service;

import com.example.parisjanitormsuser.dto.AuthRes;
import com.example.parisjanitormsuser.dto.LoginRequest;
import com.example.parisjanitormsuser.dto.RegisterRequest;
import com.example.parisjanitormsuser.entity.User;

public interface AuthService {
    AuthRes register(RegisterRequest request);
    AuthRes authenticate(LoginRequest request);
    public void userSessionCreation(User user);
}
