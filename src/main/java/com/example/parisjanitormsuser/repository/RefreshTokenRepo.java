package com.example.parisjanitormsuser.repository;



import com.example.parisjanitormsuser.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Long> {


    Optional<RefreshToken> findByToken(String token);

}