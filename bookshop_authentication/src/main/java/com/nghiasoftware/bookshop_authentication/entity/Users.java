package com.nghiasoftware.bookshop_authentication.entity;

import com.nghiasoftware.bookshop_authentication.enumable.StatusUser;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "users")
@Getter
@Setter
public class Users {

    @Id
    private String id;

    private String email;
    private String password;
    private int attemp;

    @Enumerated(EnumType.STRING)
    private StatusUser status;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
