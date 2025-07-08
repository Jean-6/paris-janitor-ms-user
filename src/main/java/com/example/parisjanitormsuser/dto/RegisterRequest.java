package com.example.parisjanitormsuser.dto;


import com.example.parisjanitormsuser.entity.PrivateInfo;
import com.example.parisjanitormsuser.entity.ProfileInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    private ProfileInfo profileInfo;
    @NotBlank
    private PrivateInfo privateInfo;
}
