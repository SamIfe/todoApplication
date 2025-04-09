package net.todoApplication.utils;

import lombok.AllArgsConstructor;
import net.todoApplication.data.models.User;
import net.todoApplication.dtos.requestDTO.CreateUserRequest;
import net.todoApplication.dtos.responseDTO.CreateUserResponse;
import net.todoApplication.services.interfaces.AuthenticationService;

import java.time.LocalDateTime;
import java.util.Random;

@AllArgsConstructor
public class UserMapper{
    public static User mapRequestToUser(CreateUserRequest userRequestDTO){
        User newUser = new User();
                newUser.setUserName(userRequestDTO.getUsername());
                newUser.setEmail(userRequestDTO.getEmail());
                newUser.setPassword(userRequestDTO.getPassword());
                newUser.setCreatedAt(LocalDateTime.now());
                newUser.setUpdatedAt(LocalDateTime.now());
                newUser.setUserId(generateUserId());

                return newUser;
    }
    public static CreateUserResponse mapResponseToUser(User user){
        CreateUserResponse response = new CreateUserResponse();
        response.setUserId(user.getUserId());
        response.setUserName(user.getUserName());
        response.setEmail(user.getEmail());
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());

        return response;
    }
    private static String generateUserId(){
        Random random = new Random();
        return "TDA" + random.nextInt(10000) + "ATS";
    }
}