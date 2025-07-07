package com.example.parisjanitormsuser.service;

import com.example.parisjanitormsuser.entity.Session;


import java.util.List;
import java.util.Optional;

public interface SessionService {
    Session save(Session session);
    Optional<Session> findById(Long id);
    List<Session> findAll();
    void deleteSessionById(Long id);
}
