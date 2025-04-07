package net.todoApplication.dtos.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class CreateUserResponseDTO {

    private String id;
    private String username;
    private String email;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean isAdmin;
    private Date createdAt;
    private Date updatedAt;
}
