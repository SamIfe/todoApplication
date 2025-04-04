package net.todoApplication.services.impl;

import lombok.RequiredArgsConstructor;
import net.todoApplication.data.models.Todo;
import net.todoApplication.data.repositories.TodoRepository;
import net.todoApplication.exceptions.TodoApplicationException;
import net.todoApplication.exceptions.TodoTaskNotFoundException;
import net.todoApplication.services.interfaces.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;


    @Override
    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public Optional<Todo> findById(String id) {
        return todoRepository.findById(id);
    }

    @Override
    public List<Todo> findByUser(String userId) {
        return todoRepository.findByUserId(userId);
    }

    @Override
    public List<Todo> findByCategory(String categoryId) {
        return todoRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo update(String id, Todo todo) {
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(()-> new TodoTaskNotFoundException("todo not found"));

        todo.setCompleted(true);
        return todoRepository.save(todo);
    }

    @Override
    public void delete(String id) {
        todoRepository.deleteById((id));

    }

    @Override
    public Todo markComplete(String id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(()-> new TodoTaskNotFoundException("todo not found"));
        todo.setCompleted(true);
        return todoRepository.save(todo);
    }

    @Override
    public Todo setDueDate(String id, Date date) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(()-> new TodoTaskNotFoundException("Todo task not found"));

        todo.setDueDate(date);
        return todoRepository.save(todo);
    }

    @Override
    public List<Todo> search(String query) {
        return todoRepository.search(query);
    }
}
