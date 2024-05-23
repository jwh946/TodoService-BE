package com.example.todo.controller;

import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.TodoDto;
import com.example.todo.model.TodoEntity;
import com.example.todo.service.TodoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDto dto){
        try{
            log.info("Log: crateTodo entrance");

            TodoEntity entity = TodoDto.toEntity(dto);
            log.info("Log: dto => entity ok");

            entity.setUserId("temporary-userid");
            Optional<TodoEntity> entities = todoService.create(entity);
            log.info("Log: service.create ok");

            List<TodoDto> dtos = entities.stream().map(TodoDto::new).toList();
            log.info("Log: entities => dtos ok");

            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
                    .data(dtos)
                    .build();
            log.info("Log: response ok");

            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
                    .error(error)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(){
        String temporaryUserId = "temporary-userid";
        List<TodoEntity> entities = todoService.retrieve(temporaryUserId);
        List<TodoDto> dtos = entities.stream().map(TodoDto::new).toList();
        ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
                .data(dtos)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/update")
    public ResponseEntity<?> update(@RequestBody TodoDto dto){
        try{
            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setUserId("temporary-user");
            Optional<TodoEntity> entities = todoService.update(entity);
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).toList();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
                    .error(error)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDto dto){
        try{
            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setUserId("temporary-user");
            Optional<TodoEntity> entities = todoService.updateTodo(entity);
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).toList();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
                    .data(dtos)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder()
                    .error(error)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody TodoDto dto){
        try{
            List<String> message = new ArrayList<>();
            String msg = todoService.delete(dto.getId());
            message.add(msg);

            ResponseDto<String> response = ResponseDto.<String>builder()
                    .data(message)
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDto<String> response = ResponseDto.<String>builder()
                    .error(error)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
