//package net.todoApplication.utils;
//
//import net.todoApplication.data.models.User;
//import net.todoApplication.dtos.requestDTO.CreateUserRequestDTO;
//import net.todoApplication.dtos.responseDTO.CreateUserResponseDTO;
//
//public class UserMapper {
//    public static void mapRequestToUser(CreateUserRequestDTO userRequestDTO){
//        User newUser = User.builder()
//                .userName(userRequestDTO.getUsername())
//                .email(userRequestDTO.getEmail())
//                .password(userRequestDTO.getPassword())
//                .isAdmin(false)
//                .build();
//    }
//
//    public static CreateUserResponseDTO mapToResponse(CreateUserRequestDTO userRequestDTO){
//        CreateUserResponseDTO userResponse = new CreateUserResponseDTO()
//                userResponse.userId(userRequestDTO.getId())
//                .userName(userRequestDTO.getUsername())
//                .email(userRequestDTO.getEmail())
//                .isAdmin(false)
//                .createdAt(userRequestDTO.getCreatedAt())
//                .updatedAt(userRequestDTO.getUpdatedAt())
//                .build();
//        return userResponse;
//    }
//}
