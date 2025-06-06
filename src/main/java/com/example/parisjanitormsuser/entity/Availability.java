package com.example.parisjanitormsuser.entity;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "provider")
    private ProviderInfo provider;

    private String day;

    @ElementCollection
    @CollectionTable(name="timeslots", joinColumns = @JoinColumn(name = "availability_id"))
    private List<TimeSlot> timeSlot;
}





