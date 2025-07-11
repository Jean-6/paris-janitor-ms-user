package com.example.parisjanitormsuser.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Reference {
    private String platform; // Airbnb, Booking, etc.
    private int numberOfPropertiesManaged;
    private String comment;
}
