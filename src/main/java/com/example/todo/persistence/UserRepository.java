package com.example.todo.persistence;

import com.example.todo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
