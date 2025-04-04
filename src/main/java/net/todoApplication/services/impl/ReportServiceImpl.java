package net.todoApplication.services.impl;

import lombok.RequiredArgsConstructor;
import net.todoApplication.data.models.Category;
import net.todoApplication.data.models.Todo;
import net.todoApplication.data.repositories.CategoryRepository;
import net.todoApplication.data.repositories.TodoRepository;
import net.todoApplication.data.repositories.UserRepository;
import net.todoApplication.services.interfaces.ReportService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {


    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Map<String, Object> generateUserActivityReport(Date startDate, Date endDate) {
        Map<String, Object> report = new HashMap<>();

        long totalUsers = userRepository.count();

        Criteria criteria = Criteria.where("createdAt").gte(startDate).lte(endDate);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("userId").count().as("count"),
                Aggregation.project("count").and("userId").previousOperation()
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(agg, "todos", Map.class);

        report.put("totalUsers", totalUsers);
        report.put("reportPeriod", Map.of("startDate", startDate, "endDate", endDate));
        report.put("userActivity", results.getMappedResults());

        return report;
    }

    @Override
    public Map<String, Object> generateCompletionRateReport(String userId, Date startDate, Date endDate) {
        Map<String, Object> report = new HashMap<>();

        List<Todo> userTodos = todoRepository.findByUserId(userId);

        List<Todo> todosInRange = userTodos.stream()
                .filter(todo -> todo.getCreatedAt().after(startDate) && todo.getCreatedAt().before(endDate))
                .toList();

        long totalTodos = todosInRange.size();
        long completedTodos = todosInRange.stream().filter(Todo::isCompleted).count();
        double completionRate = totalTodos > 0 ? (double) completedTodos / totalTodos * 100 : 0;

        List<Todo> completedWithDueDate = todosInRange.stream()
                .filter(todo -> todo.isCompleted() && todo.getDueDate() != null)
                .toList();

        OptionalDouble avgCompletionDays = completedWithDueDate.stream()
                .mapToLong(todo -> {
                    long dueTime = todo.getDueDate().getTime();
                    long createTime = todo.getCreatedAt().getTime();
                    return (dueTime - createTime) / (1000 * 60 * 60 * 24); // Convert to days
                })
                .average();

        report.put("userId", userId);
        report.put("reportPeriod", Map.of("startDate", startDate, "endDate", endDate));
        report.put("totalTodos", totalTodos);
        report.put("completedTodos", completedTodos);
        report.put("completionRate", completionRate);
        report.put("averageCompletionDays", avgCompletionDays.orElse(0));

        return report;
    }

    @Override
    public Map<String, Object> generateCategoryDistributionReport(String userId) {
        Map<String, Object> report = new HashMap<>();

        List<Todo> userTodos = todoRepository.findByUserId(userId);

        List<String> userCategoryIds = categoryRepository.findByUserId(userId)
                .stream()
                .map(Category::getId)
                .toList();

        Map<String, Long> todoCountByCategory = new HashMap<>();

        for (String categoryId : userCategoryIds) {
            long count = userTodos.stream()
                    .filter(todo -> todo.getCategoryIds() != null && todo.getCategoryIds().contains(categoryId))
                    .count();
            todoCountByCategory.put(categoryId, count);
        }

        long uncategorizedCount = userTodos.stream()
                .filter(todo -> todo.getCategoryIds() == null || todo.getCategoryIds().isEmpty())
                .count();

        report.put("userId", userId);
        report.put("categoryCounts", todoCountByCategory);
        report.put("uncategorizedCount", uncategorizedCount);
        report.put("totalTodos", userTodos.size());

        return report;
    }
}