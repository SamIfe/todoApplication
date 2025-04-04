package net.todoApplication.data.repositories;

import net.todoApplication.data.models.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {
        List<Todo> findByUserId(String userId);

        @Query("{'userId': ?0, 'isCompleted': ?1}")
        List<Todo> findByUserIdAndCompletionStatus(String userId, boolean isCompleted);

        @Query("{'categoryIds': ?0}")
        List<Todo> findByCategoryId(String categoryId);

        @Query("{'title': {$regex: ?0, $options: 'i'}}")
        List<Todo> search(String query);
}
