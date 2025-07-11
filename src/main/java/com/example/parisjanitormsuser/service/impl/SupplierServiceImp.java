package com.example.parisjanitormsuser.service.impl;
import com.example.parisjanitormsuser.entity.*;
import com.example.parisjanitormsuser.exception.ResourceNotFoundException;
import com.example.parisjanitormsuser.repository.StatusRepo;
import com.example.parisjanitormsuser.service.SupplierService;
import com.example.parisjanitormsuser.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SupplierServiceImp implements SupplierService {

    //@Autowired
    //private Supplier supplier;
    @Autowired
    private UserService userService;
    @Autowired
    private StatusRepo statusRepo;

    public com.example.parisjanitormsuser.entity.Supplier saveProviderInfo(
            Long userId,
            Company company,
            List<String> services,
            List<String> departments,
            List<TimeSlot> timeSlots) {

        /*User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.debug("user id : "+ user.getId());
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.setUser(user);

        providerInfo.setCompany(company);
        providerInfo.setServices(services);
        providerInfo.setDepartments(departments);
        providerInfo.setAvailabilities(availabilities);

        user.setProviderInfo(providerInfo);

        return this.providerInfoRepo.save(providerInfo);*/
    return null;
    }

    @Override
    public ChangeStatus saveInfo(Long userId, Company company, List<String> services, List<String> departments, List<TimeSlot> timeSlots) {


        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Supplier supplier= new Supplier();
        supplier.setCompany(company);
        supplier.setServices(services);
        supplier.setDepartments(departments);
        supplier.setTimeSlots(timeSlots);

        ChangeStatus changeStatus= new ChangeStatus();
        changeStatus.setUser(user);
        changeStatus.setSupplier(supplier);
        changeStatus.setStatus(Status.PENDING);

        return statusRepo.save(changeStatus);
    }
}
