package net.todoApplication.services.interfaces;

import net.todoApplication.data.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService{

    User register(User user);
    Optional<User> findById(String userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);
    List<User> findAll();
    User update(String id, User user);
    void delete(String id);
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
}
