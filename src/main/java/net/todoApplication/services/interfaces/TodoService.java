package net.todoApplication.services.interfaces;

import net.todoApplication.data.models.Todo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TodoService {

    Todo create(Todo todo);
    Optional<Todo> findById(String id);
    List<Todo> findByUser(String userId);
    List<Todo> findByCategory(String categoryId);
    List<Todo> findAll();
    Todo update(String id, Todo todo);
    void delete(String id);
    Todo markComplete(String id);
    Todo setDueDate(String id, Date date);
    List<Todo> search(String query);
}
