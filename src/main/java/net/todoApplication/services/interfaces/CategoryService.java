package net.todoApplication.services.interfaces;

import net.todoApplication.data.models.Category;
import net.todoApplication.dtos.requestDTO.CreateCategoryRequest;
import net.todoApplication.dtos.responseDTO.CreateCategoryResponse;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CreateCategoryResponse createCategory(CreateCategoryRequest category);
    Optional<Category> findById(String id);
    List<Category> findByUser(String userId);
    List<Category> findAll();
    Category update(String id, Category category);
    void delete(String id);
    void assignTodo(String categoryId, String todoId);
    void removeTodo(String categoryId, String todoId);
}
