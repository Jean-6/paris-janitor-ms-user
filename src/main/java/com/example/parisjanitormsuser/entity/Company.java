package com.example.parisjanitormsuser.entity;


import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Company {
    @NotNull(message = "Legal status is required")
    private String legalStatus;
    @NotNull(message = "Business name is required")
    private String businessName;
    @NotNull(message = "siret number is required")
    private String siretNumber;
    @NotNull(message = "Kbis number is required")
    private String kbisUrl;

}
