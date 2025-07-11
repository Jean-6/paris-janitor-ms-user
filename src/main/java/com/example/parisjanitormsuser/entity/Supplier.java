package com.example.parisjanitormsuser.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;


@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {

    @Embedded
    private Company company;

    @ElementCollection
    @CollectionTable(name="services")
    @Column(name="services")
    private List<String> services;

    @ElementCollection
    @CollectionTable(name="departments")
    @Column(name="departments")
    private List<String> departments;

    @ElementCollection
    @Column(name="availabilities")
    private List<TimeSlot> timeSlots;

    @ElementCollection
    @Column(name="references")
    private List<Reference> references;

    @ElementCollection
    @CollectionTable(name="service_rate", joinColumns = @JoinColumn(name="provider_id"))
    @MapKeyColumn(name="service_name")
    @Column(name="rate")
    private Map<String, Double> serviceRates;

}
