package com.api.note.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

import jakarta.persistence.Column;

@Entity
@Table(name = "tb_email_number")
public class EmailNumberModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID emailNumberId;
    
    @Column(nullable = false)
    public int number;

    public EmailNumberModel() {}

    public EmailNumberModel(int number) {
        this.number = number;
    }

    public EmailNumberModel(UUID emailNumberId, int number) {
        this.emailNumberId = emailNumberId;
        this.number = number;
    }
}
