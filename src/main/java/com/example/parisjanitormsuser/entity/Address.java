package com.example.parisjanitormsuser.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

//@Data
//@Entity
@Embeddable
public class Address {
    private String address;
    private String city;
    private String country;
    private String zip;
    private Date created_at;
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_id", referencedColumnName = "id")
    //private User user;




}
