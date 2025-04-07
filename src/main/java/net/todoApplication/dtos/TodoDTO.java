package net.todoApplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoDTO {

    private String id;
    private String title;
    private String description;
    private boolean isCompleted;
    private Date dueDate;
    private Date createdAt;
    private Date updatedAt;
    private String userId;
    private List<String> categoryIds;
}
