package net.todoApplication.data.models;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "users")
public class User {
    @Id
    private String userId;

    private String userName;

    @Indexed(unique = true)
    private String email;

    private String password;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    private boolean isAdmin;
}
