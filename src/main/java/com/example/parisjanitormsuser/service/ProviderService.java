package com.example.parisjanitormsuser.service;

import com.example.parisjanitormsuser.entity.Availability;
import com.example.parisjanitormsuser.entity.Company;
import com.example.parisjanitormsuser.entity.ProviderInfo;

import java.util.List;

public interface ProviderService {

    ProviderInfo saveProviderInfo(
            Long userId,
            Company company,
            List<String> services,
            List<String> departments,
            List<Availability> availabilities);
}