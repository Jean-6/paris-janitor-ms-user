package com.example.parisjanitormsuser.entity;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String preferences_key;
    private String preferences_value;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id",referencedColumnName = "id")
    @JsonBackReference
    private User user;


}
