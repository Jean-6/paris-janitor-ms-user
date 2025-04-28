package com.example.parisjanitormsuser.repository;

import com.example.parisjanitormsuser.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByPrivateInfoEmail(String email);
    Optional<User> findByPrivateInfoEmail(String username);

}
