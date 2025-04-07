package net.todoApplication.controllers;

import lombok.RequiredArgsConstructor;
import net.todoApplication.data.models.User;
import net.todoApplication.dtos.requestDTO.CreateUserRequestDTO;
import net.todoApplication.dtos.requestDTO.AuthRequestDTO;
import net.todoApplication.dtos.responseDTO.AuthResponseDTO;
import net.todoApplication.services.interfaces.AuthenticationService;
import net.todoApplication.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequestDTO createUserRequestDTO) {
        if (userService.existsByEmail(createUserRequestDTO.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("User already exist");
        }

        User user = User.builder()
                .userName(createUserRequestDTO.getUsername())
                .email(createUserRequestDTO.getEmail())
                .password(createUserRequestDTO.getPassword())
                .isAdmin(false)
                .build();

        User registeredUser = userService.register(user);

        CreateUserRequestDTO responseUser = CreateUserRequestDTO.builder()
                .id(registeredUser.getUserId())
                .username(registeredUser.getUserName())
                .email(registeredUser.getEmail())
                .isAdmin(registeredUser.isAdmin())
                .createdAt(registeredUser.getCreatedAt())
                .updatedAt(registeredUser.getUpdatedAt())
                .build();

        return new ResponseEntity<>(responseUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
        return userService.findByEmail(authRequest.getEmail())
                .filter(user -> authenticationService.comparePassword(authRequest.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = authenticationService.generateToken(user);
                    return ResponseEntity.ok(new AuthResponseDTO(token));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
