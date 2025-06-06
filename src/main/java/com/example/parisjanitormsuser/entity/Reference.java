package com.example.parisjanitormsuser.entity;


import jakarta.persistence.*;

@Entity
public class Reference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String platform; // Airbnb, Booking, etc.
    private int numberOfPropertiesManaged;
    private String comment;
}
