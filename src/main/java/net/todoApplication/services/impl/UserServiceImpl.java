package net.todoApplication.services.impl;

import lombok.RequiredArgsConstructor;
import net.todoApplication.data.models.User;
import net.todoApplication.data.repositories.UserRepository;
import net.todoApplication.exceptions.UserNotFoundException;
import net.todoApplication.services.interfaces.AuthenticationSrvice;
import net.todoApplication.services.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationSrvice authenticationSrvice;

    @Override
    public User register(User user) {
        user.setPassword(authenticationSrvice.hashPassword(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(String id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());

        if(user.getPassword() != null && ! user.getPassword().isEmpty()) {
            existingUser.setPassword(authenticationSrvice.hashPassword(user.getPassword()));

        }
        return userRepository.save(existingUser);
    }

    @Override
    public void delete(String id) {
        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);


    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
