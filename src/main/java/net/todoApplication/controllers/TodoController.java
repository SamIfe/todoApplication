package net.todoApplication.controllers;

import lombok.RequiredArgsConstructor;
import net.todoApplication.data.models.Todo;
import net.todoApplication.dtos.TodoDTO;
import net.todoApplication.services.interfaces.TodoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoDTO> createTodo(@RequestBody TodoDTO todoDTO) {
        Todo todo = convertToEntity(todoDTO);
        Todo createdTodo = todoService.create(todo);
        return new ResponseEntity<>(convertToDTO(createdTodo), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TodoDTO>> getUserTodos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        List<TodoDTO> todos = todoService.findByUser(getUserIdFromEmail(user.getUsername())).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDTO> getTodoById(@PathVariable String id) {
        return todoService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TodoDTO>> getTodosByCategory(@PathVariable String categoryId) {
        List<TodoDTO> todos = todoService.findByCategory(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(todos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TodoDTO>> searchTodos(@RequestParam String query) {
        List<TodoDTO> todos = todoService.search(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(todos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDTO> updateTodo(@PathVariable String id, @RequestBody TodoDTO todoDTO) {
        Todo todo = convertToEntity(todoDTO);
        Todo updatedTodo = todoService.update(id, todo);
        return ResponseEntity.ok(convertToDTO(updatedTodo));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TodoDTO> markTodoComplete(@PathVariable String id) {
        Todo completedTodo = todoService.markComplete(id);
        return ResponseEntity.ok(convertToDTO(completedTodo));
    }

    @PatchMapping("/{id}/due-date")
    public ResponseEntity<TodoDTO> setTodoDueDate(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dueDate) {
        Todo updatedTodo = todoService.setDueDate(id, dueDate);
        return ResponseEntity.ok(convertToDTO(updatedTodo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TodoDTO convertToDTO(Todo todo) {
        return TodoDTO.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .isCompleted(todo.isCompleted())
                .dueDate(todo.getDueDate())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .userId(todo.getUserId())
                .categoryIds(todo.getCategoryIds())
                .build();
    }

    private Todo convertToEntity(TodoDTO todoDTO) {
        return Todo.builder()
                .id(todoDTO.getId())
                .title(todoDTO.getTitle())
                .description(todoDTO.getDescription())
                .isCompleted(todoDTO.isCompleted())
                .dueDate(todoDTO.getDueDate())
                .userId(todoDTO.getUserId())
                .categoryIds(todoDTO.getCategoryIds())
                .build();
    }

    private String getUserIdFromEmail(String email) {
        return "user-id";
    }
}
