package com.example.parisjanitormsuser.service;

import com.example.parisjanitormsuser.dto.AuthRes;
import com.example.parisjanitormsuser.dto.LoginReq;
import com.example.parisjanitormsuser.dto.RegisterReq;
import com.example.parisjanitormsuser.entity.User;

public interface AuthService {
    AuthRes register(RegisterReq request);
    AuthRes authenticate(LoginReq request);
    public void userSessionCreation(User user);
}
