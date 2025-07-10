package net.todoApplication.controllers;

import lombok.RequiredArgsConstructor;
import net.todoApplication.data.models.Category;
import net.todoApplication.data.models.User;
import net.todoApplication.dtos.requestDTO.CreateCategoryRequest;
import net.todoApplication.dtos.responseDTO.CreateCategoryResponse;
import net.todoApplication.services.interfaces.CategoryService;
import net.todoApplication.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        CreateCategoryResponse createdCategory = categoryService.createCategory(createCategoryRequest);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CreateCategoryRequest>> getUserCategories() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        List<CreateCategoryRequest> categories = categoryService.findByUser(getUserIdFromEmail(user.getUsername())).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateCategoryRequest> getCategoryById(@PathVariable String id) {
        return categoryService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateCategoryRequest> updateCategory(@PathVariable String id, @RequestBody CreateCategoryRequest createCategoryRequest) {
        Category category = convertToEntity(createCategoryRequest);
        Category updatedCategory = categoryService.update(id, category);
        return ResponseEntity.ok(convertToDTO(updatedCategory));
    }

    @PostMapping("/{categoryId}/todos/{todoId}")
    public ResponseEntity<Void> assignTodoToCategory(@PathVariable String categoryId, @PathVariable String todoId) {
        categoryService.assignTodo(categoryId, todoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{categoryId}/todos/{todoId}")
    public ResponseEntity<Void> removeTodoFromCategory(@PathVariable String categoryId, @PathVariable String todoId) {
        categoryService.removeTodo(categoryId, todoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CreateCategoryRequest convertToDTO(Category category) {
        return CreateCategoryRequest.builder()
                .id(category.getCategoryId())
                .name(category.getName())
                .color(category.getColor())
                .createdAt(category.getCreatedAt())
                .userId(category.getUserId())
                .build();
    }

    private Category convertToEntity(CreateCategoryRequest createCategoryRequest) {
        return Category.builder()
                .userId(createCategoryRequest.getUserId())
                .name(createCategoryRequest.getName())
                .color(createCategoryRequest.getColor())
                .userId(createCategoryRequest.getUserId())
                .build();
    }

    private String getUserIdFromEmail(String email) {
        return userService.findByEmail(email)
                .map(User::getUserId)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
