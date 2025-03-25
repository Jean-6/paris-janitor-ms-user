package com.example.parisjanitormsuser.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long session_id;
    private long expires_at;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;

}
