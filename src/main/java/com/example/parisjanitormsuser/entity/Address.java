package com.example.parisjanitormsuser.entity;



import jakarta.persistence.*;

import java.util.Date;

@Embeddable
public class Address {
    private String address;
    private String city;
    private String country;
    private String zip;
    private Date created_at;
}
