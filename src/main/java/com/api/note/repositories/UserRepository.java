package com.api.note.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.note.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    public boolean existsByEmail(String email);
    public Optional<UserModel> findByEmail(String email);
}
