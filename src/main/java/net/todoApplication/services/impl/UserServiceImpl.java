package net.todoApplication.services.impl;

import lombok.RequiredArgsConstructor;
import net.todoApplication.data.models.User;
import net.todoApplication.data.repositories.UserRepository;
import net.todoApplication.dtos.requestDTO.CreateUserRequest;
import net.todoApplication.dtos.responseDTO.CreateUserResponse;
import net.todoApplication.exceptions.UserAlreadyExistException;
import net.todoApplication.exceptions.UserNotFoundException;
import net.todoApplication.services.interfaces.AuthenticationService;
import net.todoApplication.services.interfaces.UserService;
import net.todoApplication.utils.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @Override
    public CreateUserResponse registerUser(CreateUserRequest registerUser) {
        if(userExist(registerUser.getEmail())){
            throw new UserAlreadyExistException("User already exist");
        }

        User newUser = UserMapper.mapRequestToUser(registerUser);
        newUser.setPassword(authenticationService.hashPassword(registerUser.getPassword()));
        userRepository.save(newUser);
        return UserMapper.mapResponseToUser(newUser);
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
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(String id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        existingUser.setUserName(user.getUserName());
        existingUser.setPassword(user.getPassword());

        if(user.getPassword() != null && ! user.getPassword().isEmpty()) {
            existingUser.setPassword(authenticationService.hashPassword(user.getPassword()));

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
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }


    private boolean userExist(String email){
        return userRepository.existsByEmail(email);
    }
}
