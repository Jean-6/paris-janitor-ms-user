package com.example.parisjanitormsuser.service;

import com.example.parisjanitormsuser.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    Optional<User> update(Long id,User user);
    Optional<User> delete(Long id);
    User findByEmail(String email);
}
