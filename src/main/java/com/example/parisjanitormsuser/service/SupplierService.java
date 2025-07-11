package com.example.parisjanitormsuser.service;

import com.example.parisjanitormsuser.entity.ChangeStatus;
import com.example.parisjanitormsuser.entity.Company;
import com.example.parisjanitormsuser.entity.TimeSlot;

import java.util.List;

public interface SupplierService {

    ChangeStatus saveInfo(
            Long userId,
            Company company,
            List<String> services,
            List<String> departments,
            List<TimeSlot> timeSlots);
}