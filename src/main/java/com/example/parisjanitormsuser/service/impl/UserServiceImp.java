package com.example.parisjanitormsuser.service.impl;

import com.example.parisjanitormsuser.entity.User;
import com.example.parisjanitormsuser.repository.UserRepo;
import com.example.parisjanitormsuser.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public Optional<User> findByEmail(String email) {
        return userRepo.findByPrivateInfoEmail(email);
    }

    @Override
    public boolean updateUserPassword(Optional<User> optionalUser , String newPassword) {

        if(optionalUser.isPresent()){
            log.debug("User is present to update password ");
            User user = optionalUser.get();
            user.getPrivateInfo().setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
            return  true;
        }
        log.debug("User is not present to update password ");
        return false;
    }

}
