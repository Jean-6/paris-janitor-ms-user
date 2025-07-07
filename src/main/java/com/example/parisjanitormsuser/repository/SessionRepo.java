package com.example.parisjanitormsuser.repository;


import com.example.parisjanitormsuser.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<Session, Long> {

    @Override
    Optional<Session> findById(Long aLong);
}
