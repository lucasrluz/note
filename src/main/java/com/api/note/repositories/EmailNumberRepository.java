package com.api.note.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.note.models.EmailNumberModel;

@Repository
public interface EmailNumberRepository extends JpaRepository<EmailNumberModel, UUID> {
    
}
