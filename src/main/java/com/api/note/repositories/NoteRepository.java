package com.api.note.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.note.models.NoteModel;

@Repository
public interface NoteRepository extends JpaRepository<NoteModel, UUID> {
    public boolean existsByTitle(String title);
}
