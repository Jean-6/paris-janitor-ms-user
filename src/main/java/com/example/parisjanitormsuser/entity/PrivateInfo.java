package com.example.parisjanitormsuser.entity;


import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class PrivateInfo {
    @Email(message = "email format is not valid")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
}
