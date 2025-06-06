package com.example.parisjanitormsuser.service.impl;

import com.example.parisjanitormsuser.entity.Availability;
import com.example.parisjanitormsuser.entity.Company;
import com.example.parisjanitormsuser.entity.ProviderInfo;
import com.example.parisjanitormsuser.entity.User;
import com.example.parisjanitormsuser.exception.ResourceNotFoundException;
import com.example.parisjanitormsuser.repository.ProviderInfoRepo;
import com.example.parisjanitormsuser.repository.UserRepo;
import com.example.parisjanitormsuser.service.AttachmentServiceClient;
import com.example.parisjanitormsuser.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderInfoRepo providerInfoRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AttachmentServiceClient attachmentServiceClient;


    public ProviderInfo saveProviderInfo(
            Long userId,
            Company company,
            List<String> services,
            List<String> departments,
            List<Availability> availabilities){

        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.setCompany(company);
        providerInfo.setServices(services);
        providerInfo.setDepartments(departments);
        providerInfo.setAvailabilities(availabilities);

        Optional<User> userOptional = Optional.ofNullable(userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found")));

        providerInfo.setUser(userOptional.get());
        return this.providerInfoRepo.save(providerInfo);
    }
}
