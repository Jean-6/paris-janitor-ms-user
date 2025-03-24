package com.example.parisjanitormsuser.service.impl;

import com.example.parisjanitormsuser.entity.User;
import com.example.parisjanitormsuser.repository.UserRepo;
import com.example.parisjanitormsuser.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImp implements UserService {

    private final UserRepo userRepo;

    public UserServiceImp(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> update(Long id,User user) {
        Optional<User> userOpt = userRepo.findById(id);
        if(userOpt.isPresent()) {
            return Optional.of(userRepo.save(user));
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> delete(Long id) {
        Optional<User> userOpt = userRepo.findById(id);
        userOpt.ifPresent(userRepo::delete);
        return userOpt;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }
}
