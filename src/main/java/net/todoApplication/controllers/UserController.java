package net.todoApplication.controllers;

import lombok.RequiredArgsConstructor;
import net.todoApplication.data.models.User;
import net.todoApplication.dtos.requestDTO.CreateUserRequestDTO;
import net.todoApplication.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<CreateUserRequestDTO>> getAllUsers() {
        List<CreateUserRequestDTO> users = userService.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateUserRequestDTO> getUserById(@PathVariable String id) {
        return userService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/profile")
    public ResponseEntity<CreateUserRequestDTO> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return userService.findByEmail(email)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateUserRequestDTO> updateUser(@PathVariable String id, @RequestBody CreateUserRequestDTO createUserRequestDTO) {
        User user = convertToEntity(createUserRequestDTO);
        User updatedUser = userService.update(id, user);
        return ResponseEntity.ok(convertToDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CreateUserRequestDTO convertToDTO(User user) {
        return CreateUserRequestDTO.builder()
                .id(user.getUserId())
                .username(user.getUserName())
                .email(user.getEmail())
                .isAdmin(user.isAdmin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private User convertToEntity(CreateUserRequestDTO createUserRequestDTO) {
        return User.builder()
                .userId(createUserRequestDTO.getId())
                .userName(createUserRequestDTO.getUsername())
                .email(createUserRequestDTO.getEmail())
                .isAdmin(createUserRequestDTO.isAdmin())
                .build();
    }
}
