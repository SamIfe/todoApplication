package net.todoApplication.dtos.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class CreateUserResponse {

    private String userId;
    private String userName;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean isAdmin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
