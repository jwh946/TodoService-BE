package com.example.todo.controller;

import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.TodoDto;
import com.example.todo.model.TodoEntity;
import com.example.todo.model.UserEntity;
import com.example.todo.security.UserDetailsImpl;
import com.example.todo.service.TodoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TodoDto dto){
        try{
            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setId(null);
            entity.setUserId(userDetails.getUser().getId());

            Optional<TodoEntity> entities = todoService.create(entity);
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).toList();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<TodoEntity> entities = todoService.retrieve(userDetails.getUser().getId());
        List<TodoDto> dtos = entities.stream().map(TodoDto::new).toList();
        ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TodoDto dto){
        try{
            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setUserId(userDetails.getUser().getId());
            Optional<TodoEntity> entities = todoService.updateTodo(entity);
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).toList();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TodoDto dto){
        try{
            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setUserId(userDetails.getUser().getId());
            List<TodoEntity> entities = todoService.delete(entity);
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).toList();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            String error = e.getMessage();
            ResponseDto<String> response = ResponseDto.<String>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
