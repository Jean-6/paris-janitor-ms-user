package com.example.parisjanitormsuser.repository;

import com.example.parisjanitormsuser.entity.ProviderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderInfoRepo extends JpaRepository<ProviderInfo, Long> {


}
