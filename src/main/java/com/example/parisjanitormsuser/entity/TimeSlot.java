package com.example.parisjanitormsuser.entity;

import jakarta.persistence.Embeddable;

import java.util.Date;

@Embeddable
public class TimeSlot{
    private Date startDate;
    private Date endDate;
}