package com.example.parisjanitormsuser.service.impl;
import com.example.parisjanitormsuser.entity.Availability;
import com.example.parisjanitormsuser.entity.Company;
import com.example.parisjanitormsuser.entity.ProviderInfo;
import com.example.parisjanitormsuser.entity.User;
import com.example.parisjanitormsuser.exception.ResourceNotFoundException;
import com.example.parisjanitormsuser.repository.ProviderInfoRepo;
import com.example.parisjanitormsuser.service.ProviderService;
import com.example.parisjanitormsuser.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProviderServiceImp implements ProviderService {

    @Autowired
    private ProviderInfoRepo providerInfoRepo;
    @Autowired
    private UserService userService;

    public ProviderInfo saveProviderInfo(
            Long userId,
            Company company,
            List<String> services,
            List<String> departments,
            List<Availability> availabilities) {

        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.debug("user id : "+ user.getId());
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.setUser(user);

        providerInfo.setCompany(company);
        providerInfo.setServices(services);
        providerInfo.setDepartments(departments);
        providerInfo.setAvailabilities(availabilities);

        user.setProviderInfo(providerInfo);

        return this.providerInfoRepo.save(providerInfo);
    }
}
