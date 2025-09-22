package com.nghiasoftware.bookshop_authentication.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "role")
    private List<Users> users;
}
