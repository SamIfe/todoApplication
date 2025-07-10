package net.todoApplication.utils;

import net.todoApplication.data.models.Category;
import net.todoApplication.dtos.requestDTO.CreateCategoryRequest;
import net.todoApplication.dtos.responseDTO.CreateCategoryResponse;

import java.time.LocalDateTime;
import java.util.Random;

public class CategoryMapper {
    public static Category mapRequestToCategory(CreateCategoryRequest categoryRequest){
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setColor(categoryRequest.getColor());
        category.setUserId(categoryRequest.getUserId());
        category.setCreatedAt(LocalDateTime.now());
        return category;
    }

    public static CreateCategoryResponse mapToCategoryResponse(Category response){
        CreateCategoryResponse categoryResponse = new CreateCategoryResponse();
        categoryResponse.setMessage("Category successfully created");
        categoryResponse.setCreatedAt(LocalDateTime.now());
        return categoryResponse;


    }
    private static String createUserId(){
        Random random = new Random();
        return "SCAT" + random.nextInt(10000) + "ECG";

    }
}
