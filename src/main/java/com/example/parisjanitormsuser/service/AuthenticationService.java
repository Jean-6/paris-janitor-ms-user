package com.example.parisjanitormsuser.service;

import com.example.parisjanitormsuser.dto.AuthenticationRequest;
import com.example.parisjanitormsuser.dto.AuthenticationResponse;
import com.example.parisjanitormsuser.dto.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
