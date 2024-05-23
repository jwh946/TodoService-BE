package com.example.todo.persistence;

import com.example.todo.model.TodoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<TodoEntity, String> {

    @Query("SELECT t FROM TodoEntity t WHERE t.userId = ?1")
    List<TodoEntity> findByUserId(String userId);
}
