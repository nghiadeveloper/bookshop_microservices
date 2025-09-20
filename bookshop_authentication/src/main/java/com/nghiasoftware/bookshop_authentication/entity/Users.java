package com.nghiasoftware.bookshop_authentication.entity;

import com.nghiasoftware.bookshop_authentication.enumable.StatusUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString();
    }
}
