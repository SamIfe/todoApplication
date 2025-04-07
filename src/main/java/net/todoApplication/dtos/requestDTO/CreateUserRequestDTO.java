package net.todoApplication.dtos.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequestDTO {
    private String id;
    private String username;
    private String email;

   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean isAdmin;
    private Date createdAt;
    private Date updatedAt;
}
