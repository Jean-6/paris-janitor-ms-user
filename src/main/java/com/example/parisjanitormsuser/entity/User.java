package com.example.parisjanitormsuser.entity;

import com.example.parisjanitormsuser.security.enums.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Builder
@AllArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Embedded
    private Address address;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Preference> preferences;
    /*@ManyToMany @JoinTable(name = "user_status", joinColumns = @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="status_id")) private Set<Status> status=new HashSet<>();*/
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Session> sessions;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RefreshToken> refreshTokens;

    public User() {
        this.role = Role.USER;
    }

    /*@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role==null){
            throw new IllegalStateException("Le rôle de l'utilisateur n'est pas défini !");
        }
        return role.getAuthorities();
    }*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return (this.role != null) ? role.getAuthorities() : Role.USER.getAuthorities();
    }


    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
