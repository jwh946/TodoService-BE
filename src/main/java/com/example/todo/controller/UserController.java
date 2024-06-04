package com.example.todo.controller;

import com.example.todo.dto.UserDTO;
import com.example.todo.model.UserEntity;
import com.example.todo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService service;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDto, BindingResult bindingResult) {

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(!fieldErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("signup error");
        }

        UserEntity user = service.signup(userDto);
        UserDTO responseUserDTO = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        return ResponseEntity.ok().body(responseUserDTO);
    }
}
