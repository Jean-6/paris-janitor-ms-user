package com.example.parisjanitormsuser.entity;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

@Data
@Embeddable
public class ProfileInfo {
    private String firstname;
    private String lastname;
    @Embedded
    private Address address;

}
