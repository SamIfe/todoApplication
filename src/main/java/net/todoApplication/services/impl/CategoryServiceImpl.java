package net.todoApplication.services.impl;

import lombok.RequiredArgsConstructor;
import net.todoApplication.data.models.Category;
import net.todoApplication.data.models.Todo;
import net.todoApplication.data.repositories.CategoryRepository;
import net.todoApplication.data.repositories.TodoRepository;
import net.todoApplication.dtos.requestDTO.CreateCategoryRequest;
import net.todoApplication.dtos.responseDTO.CreateCategoryResponse;
import net.todoApplication.exceptions.CategoryNotFoundException;
import net.todoApplication.exceptions.TodoTaskNotFoundException;
import net.todoApplication.services.interfaces.CategoryService;
import net.todoApplication.utils.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;

    private final TodoRepository todoRepository;


    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest category) {
        Category todoCategory = CategoryMapper.mapRequestToCategory(category);
        categoryRepository.save(todoCategory);
        return CategoryMapper.mapToCategoryResponse(todoCategory);
    }

    @Override
    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> findByUser(String userId) {
        return categoryRepository.findByUserId(userId);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category update(String id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(()->new CategoryNotFoundException("Category not found"));

        existingCategory.setName(category.getName());
        existingCategory.setColor(category.getColor());

        return categoryRepository.save(category);
    }

    @Override
    public void delete(String id) {
        categoryRepository.deleteById(id);

    }

    @Override
    public void assignTodo(String categoryId, String todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(()-> new TodoTaskNotFoundException("Todo task not found"));

        List<String> categoryIds = todo.getCategoryIds();
        if (categoryIds == null){
            categoryIds = new ArrayList<>();
        }
        if(!categoryIds.contains(categoryId)){
            categoryIds.add(categoryId);
            todo.setCategoryIds(categoryIds);
            todoRepository.save(todo);
        }


    }

    @Override
    public void removeTodo(String categoryId, String todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(()-> new TodoTaskNotFoundException("Todo task not found"));

        List<String> categoryIds = todo.getCategoryIds();
        if(categoryIds !=null && categoryIds.contains(categoryId)) {
            categoryIds.remove(categoryId);
            todo.setCategoryIds(categoryIds);
            todoRepository.save(todo);

        }

    }
}
