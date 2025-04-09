package net.todoApplication.controllers;

import lombok.RequiredArgsConstructor;
import net.todoApplication.dtos.requestDTO.CreateUserRequest;
import net.todoApplication.dtos.requestDTO.AuthRequest;
import net.todoApplication.dtos.responseDTO.AuthResponse;
import net.todoApplication.dtos.responseDTO.CreateUserResponse;
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
    public ResponseEntity<?> register(@RequestBody CreateUserRequest createUserRequest) {
        if (userService.existsByEmail(createUserRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("User already exist");
        }
        CreateUserResponse response = userService.registerUser(createUserRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return userService.findByEmail(authRequest.getEmail())
                .filter(user -> authenticationService.comparePassword(authRequest.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = authenticationService.generateToken(user);
                    return ResponseEntity.ok(new AuthResponse(token));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
