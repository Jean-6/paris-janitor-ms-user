package com.example.parisjanitormsuser.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderInfo {


    @Getter
    @Setter
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private Company company;

    @ElementCollection
    @CollectionTable(name="services", joinColumns = @JoinColumn(name="provider_id"))
    @Column(name="services")
    private List<String> services;

    @ElementCollection
    @CollectionTable(name="departments",joinColumns = @JoinColumn(name="provider_id"))
    @Column(name="departments")
    private List<String> departments;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Availability> availabilities;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "provider_id")
    private List<Reference> references;

    @ElementCollection
    @CollectionTable(name="service_rate", joinColumns = @JoinColumn(name="provider_id"))
    @MapKeyColumn(name="service_name")
    @Column(name="rate")
    private Map<String, Double> serviceRates;

}
