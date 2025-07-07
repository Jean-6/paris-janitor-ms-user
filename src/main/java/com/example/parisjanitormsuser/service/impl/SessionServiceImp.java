package com.example.parisjanitormsuser.service.impl;

import com.example.parisjanitormsuser.entity.Session;
import com.example.parisjanitormsuser.repository.SessionRepo;
import com.example.parisjanitormsuser.exception.NotFoundException;
import com.example.parisjanitormsuser.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SessionServiceImp implements SessionService {

    @Autowired
    private SessionRepo sessionRepo;


    @Override
    public Session save(Session session) {
        return sessionRepo.save(session);
    }

    @Override
    public Optional<Session> findById(Long id) {
        return Optional.ofNullable(sessionRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Session not found with id " + id)));
    }

    @Override
    public List<Session> findAll() {
        return sessionRepo.findAll();
    }


    @Override
    public void deleteSessionById(Long id) {
        if(!sessionRepo.existsById(id)){
            throw new NotFoundException("Session id not found");
        }
        sessionRepo.deleteById(id);
    }

}
