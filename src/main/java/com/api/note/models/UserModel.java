package com.api.note.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_user")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID userId;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String password;

    public UserModel(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserModel() {}
}
