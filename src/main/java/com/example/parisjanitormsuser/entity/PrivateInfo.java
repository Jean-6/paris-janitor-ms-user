package com.example.parisjanitormsuser.entity;


import jakarta.persistence.Embeddable;
import lombok.Data;


@Data
@Embeddable
public class PrivateInfo {
    private String email;
    private String password;
}
