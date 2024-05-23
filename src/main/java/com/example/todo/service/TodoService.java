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

    private final TodoRepository todoRepository;

    public Optional<TodoEntity> create(final TodoEntity entity) {
        validate(entity);
        return Optional.of(todoRepository.save(entity));
    }

    public List<TodoEntity> retrieve(final String userId) {
        return todoRepository.findByUserId(userId);
    }

    public Optional<TodoEntity> update(final TodoEntity entity) {
        validate(entity);
        if(todoRepository.findById(entity.getId()).isPresent()){
            todoRepository.save(entity);
        } else{
            throw new RuntimeException("Unknown id");
        }

        return todoRepository.findById(entity.getId());
    }

    public Optional<TodoEntity> updateTodo(final TodoEntity entity){
        validate(entity);

        final Optional<TodoEntity> original = todoRepository.findById(entity.getId());
        original.ifPresent(todo -> {
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            todoRepository.save(todo);
        });

        return todoRepository.findById(entity.getId());
    }

    public String delete(final String id){
        if(todoRepository.findById(id).isPresent()){
            todoRepository.deleteById(id);
        } else{
            throw new RuntimeException("id does not exist");
        }
        return "Deleted";
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
