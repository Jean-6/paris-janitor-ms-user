package com.example.parisjanitormsuser.repository;

import com.example.parisjanitormsuser.entity.ChangeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepo extends JpaRepository<ChangeStatus,Long> {
}
