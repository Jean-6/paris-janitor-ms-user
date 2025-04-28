package com.example.parisjanitormsuser.entity;


import com.example.parisjanitormsuser.security.enums.Role;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    public ProfileInfo(String username, Role role) {}
}
