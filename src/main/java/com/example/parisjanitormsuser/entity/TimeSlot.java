package com.example.parisjanitormsuser.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.Date;


@Data
@Embeddable
public class TimeSlot{
    private String day;
    private Date startDate;
    private Date endDate;
}