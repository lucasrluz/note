package com.api.note.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_note")
public class NoteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID noteId;

    @Column(nullable = false, unique = false)
    public String title;

    @Column(nullable = false, unique = false)
    public String content;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    public UserModel userModel;

    public NoteModel(String title, String content, UserModel userModel) {
        this.title = title;
        this.content = content;
        this.userModel = userModel;
    }

    public NoteModel() {};
}
