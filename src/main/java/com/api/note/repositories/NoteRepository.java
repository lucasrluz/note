package com.api.note.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.note.models.NoteModel;
import com.api.note.models.UserModel;

@Repository
public interface NoteRepository extends JpaRepository<NoteModel, UUID> {
    public List<NoteModel> findByTitleAndUserModel(String title, UserModel userModel);
}
