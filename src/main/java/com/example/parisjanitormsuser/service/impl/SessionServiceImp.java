package com.example.parisjanitormsuser.service.impl;

import com.example.parisjanitormsuser.entity.Session;
import com.example.parisjanitormsuser.repository.SessionRepo;
import com.example.parisjanitormsuser.exception.NotFoundException;
import com.example.parisjanitormsuser.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public void revokeSessionById(Long id) {
        Optional<Session> optionalSession = sessionRepo.findById(id);
        if(optionalSession.isPresent()){
            Session session = optionalSession.get();
            session.revoked();
            session.deactivated();
            sessionRepo.save(session);
        }else{
            throw new NotFoundException("Session id not found");
        }
    }

    @Override
    public void deleteSessionById(Long id) {
        Optional<Session> optionalSession = sessionRepo.findById(id);
        if(optionalSession.isPresent()){
            sessionRepo.deleteById(id);
        }else{
            throw new NotFoundException("Session id not found");
        }

    }

    @Scheduled(fixedRate = 60_000)
    @Override
    public void revokeExpiredSessions() {
        List<Session> sessions = sessionRepo.findAll();
        for(Session s : sessions){
            if(!s.isRevoked() && Instant.now().isAfter(s.getExpires_at())){
                s.revoked();
                s.deactivated();
                sessionRepo.save(s);
            }
        }
    }

}
