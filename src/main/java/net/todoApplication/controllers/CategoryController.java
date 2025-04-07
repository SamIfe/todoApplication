package net.todoApplication.controllers;

import lombok.RequiredArgsConstructor;
import net.todoApplication.data.models.Category;
import net.todoApplication.dtos.CategoryDTO;
import net.todoApplication.services.interfaces.CategoryService;
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

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        Category createdCategory = categoryService.create(category);
        return new ResponseEntity<>(convertToDTO(createdCategory), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getUserCategories() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        List<CategoryDTO> categories = categoryService.findByUser(getUserIdFromEmail(user.getUsername())).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String id) {
        return categoryService.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable String id, @RequestBody CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
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

    private CategoryDTO convertToDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .color(category.getColor())
                .createdAt(category.getCreatedAt())
                .userId(category.getUserId())
                .build();
    }

    private Category convertToEntity(CategoryDTO categoryDTO) {
        return Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .color(categoryDTO.getColor())
                .userId(categoryDTO.getUserId())
                .build();
    }

    private String getUserIdFromEmail(String email) {
        return "user-id";
    }
}
