package com.example.parisjanitormsuser.repository;

import com.example.parisjanitormsuser.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    @NonNull
    Optional<User> findById(@NonNull  Long userId);
    boolean existsByPrivateInfoEmail(String email);
    Optional<User> findByPrivateInfoEmail(String username);
}
