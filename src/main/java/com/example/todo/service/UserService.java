package com.example.todo.service;

import com.example.todo.dto.UserDTO;
import com.example.todo.model.UserEntity;
import com.example.todo.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity signup(UserDTO userDto) {
        String username = userDto.getUsername();
        String email = userDto.getEmail();
        String password = passwordEncoder.encode(userDto.getPassword());

        if (repository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }

        UserEntity user = UserEntity.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
        return repository.save(user);
    }
}
