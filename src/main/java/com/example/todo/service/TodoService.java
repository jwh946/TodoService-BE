package com.example.todo.service;

import com.example.todo.model.TodoEntity;
import com.example.todo.persistence.TodoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository repository;

    public Optional<TodoEntity> create(final TodoEntity entity) {
        validate(entity);
        return Optional.of(repository.save(entity));
    }

    public List<TodoEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }

    public Optional<TodoEntity> update(final TodoEntity entity) {
        validate(entity);
        if(repository.findById(entity.getId()).isPresent()){
            repository.save(entity);
        } else{
            throw new RuntimeException("Unknown id");
        }

        return repository.findById(entity.getId());
    }

    public Optional<TodoEntity> updateTodo(final TodoEntity entity){
        validate(entity);

        final Optional<TodoEntity> original = repository.findById(entity.getId());
        original.ifPresent(todo -> {
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            repository.save(todo);
        });

        return repository.findById(entity.getId());
    }

    public List<TodoEntity> delete(final TodoEntity entity) {
        if (repository.existsById(entity.getId())) {
            repository.deleteById(entity.getId());
        } else {
            throw new RuntimeException("Entity does not exist");
        }
        return repository.findByUserId(entity.getUserId());
    }

    public void validate(final TodoEntity entity){
        if(entity == null){
            log.warn("Entity can not be null");
            throw new RuntimeException("Entity can not be null");
        }
        if(entity.getUserId() == null){
            log.warn("Unknown user");
            throw new RuntimeException("Unknown user");
        }
    }
}
