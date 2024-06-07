package com.example.todo.controller;

import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.TodoDto;
import com.example.todo.model.TodoEntity;
import com.example.todo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(originPatterns ="*")
@RestController
@RequestMapping("todo")
public class TodoController {
    @Autowired
    private TodoService service;

    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDto dto) {
        try {
            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setId(null);
            entity.setUserId(userId);

            List<TodoEntity> entities = service.create(entity);

            List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());
            log.info("Log:entities => dtos OK!");

            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();
            log.info("Log:responsedto OK!");

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping
    public ResponseEntity<?> retrieveTodo(@AuthenticationPrincipal String userId) {
        List<TodoEntity> entities = service.retrieve(userId);
        List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());
        ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }
    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDto dto) {
        try {
            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setUserId(userId);

            List<TodoEntity> entities = service.update(entity);
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDto dto) {
        try {
            TodoEntity entity = TodoDto.toEntity(dto);
            entity.setUserId(userId);

            List<TodoEntity> entities = service.delete(entity);
            List<TodoDto> dtos = entities.stream().map(TodoDto::new).collect(Collectors.toList());
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @DeleteMapping("/batch")
    public ResponseEntity<?> deleteTodos(@AuthenticationPrincipal String userId, @RequestBody List<String> todoIds) {
        try {
            // 서비스 레이어에서 여러 항목을 삭제하는 메서드를 호출하고 삭제된 항목들을 받아옵니다.
            List<TodoEntity> deletedEntities = service.deleteBatch(userId, todoIds);

            // 삭제된 항목들을 DTO로 변환합니다.
            List<TodoDto> deletedDtos = deletedEntities.stream()
                    .map(TodoDto::new)
                    .collect(Collectors.toList());

            // 응답 DTO를 생성합니다.
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().data(deletedDtos).build();

            // 정상적인 응답을 반환합니다.
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 예외가 발생한 경우, 예외 메시지를 응답에 담아서 반환합니다.
            String error = e.getMessage();
            ResponseDto<TodoDto> response = ResponseDto.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}