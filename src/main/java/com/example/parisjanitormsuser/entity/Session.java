package com.example.parisjanitormsuser.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Data
@Entity
public class Session {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue
    private Long id;
    private Long session_id;
    private long expires_at;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
