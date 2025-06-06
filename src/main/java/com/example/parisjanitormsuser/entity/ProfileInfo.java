package com.example.parisjanitormsuser.entity;


import com.example.parisjanitormsuser.security.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ProfileInfo {
    @NotBlank(message = "username is required")
    private String username;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Embedded
    private Address address;

}
