package com.example.snackordering.service;

import com.example.snackordering.entity.Category;
import com.example.snackordering.entity.Food;
import com.example.snackordering.model.food.FoodRequest;
import com.example.snackordering.model.food.FoodResponse;
import com.example.snackordering.repository.CategoryRepository;
import com.example.snackordering.repository.FoodRepository;
import com.example.snackordering.util.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final Logger LOGGER = LoggerFactory.getLogger(FoodService.class);
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;

    public List<FoodResponse> findAll() {
        LOGGER.info("Find all foods");
        List<Food> foods = foodRepository.findAll();
        if (foods.isEmpty()) {
            LOGGER.warn("No foods were found!");
        }

        return foods.stream()
                .map(this::foodResponseGenerator)
                .collect(Collectors.toList());
    }

    public FoodResponse findById(Integer id) {
        LOGGER.info("Find food with id " + id);
        Optional<Food> food = foodRepository.findById(id);
        if (food.isEmpty()) {
            LOGGER.warn("No food was found!");
            return null;
        }
        return food.map(this::foodResponseGenerator).get();
    }

    public FoodResponse save(FoodRequest foodRequest) {
        Food food;
        Optional<Category> category = categoryRepository.findById(foodRequest.getCategoryId());
        if (category.isEmpty()) {
            throw new CustomValidationException(List.of("No category was found!"));
        }

        if (foodRequest.getFoodId() != null) {
            LOGGER.info("Update food with id " + foodRequest.getFoodId());
            checkExist(foodRequest.getFoodId());
            food = foodRepository.findById(foodRequest.getFoodId()).get();
            updateFood(food, foodRequest);
            foodRepository.save(food);
        } else {
            LOGGER.info("Create new food");
            food = createFood(foodRequest, category.get());
            foodRepository.save(food);
        }
        return foodResponseGenerator(food);
    }

    public void delete(Integer id) {
        if (id != null) {
            LOGGER.info("Delete food with id " + id);
            checkExist(id);
            Food food = foodRepository.findById(id).get();
            foodRepository.delete(food);
        }
    }

    private Food createFood(FoodRequest request, Category category) {
        Food food = new Food();
        setCommonFields(food, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        food.setCreatedBy(authentication.getName());
        food.setCategory(category);
        return food;
    }

    private void updateFood(Food food, FoodRequest request) {
        setCommonFields(food, request);
        Category category = categoryRepository.findById(request.getCategoryId()).get();
        food.setCategory(category);
    }

    private void setCommonFields(Food food, FoodRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        food.setDescription(request.getDescription());
        food.setPrice(request.getPrice());
        food.setImageURL(request.getImageURL());
        food.setIngredients(request.getIngredients());
        food.setFoodName(request.getFoodName());
        food.setAvailable(!request.isAvailable());
        food.setUpdatedBy(authentication.getName());
        food.setUpdatedDate(new Date());
    }

    private FoodResponse foodResponseGenerator(Food food) {
        FoodResponse foodResponse = new FoodResponse();
        foodResponse.setFoodId(food.getFoodId());
        foodResponse.setFoodName(food.getFoodName());
        foodResponse.setDescription(food.getDescription());
        foodResponse.setPrice(food.getPrice());
        foodResponse.setImageURL(food.getImageURL());
        foodResponse.setIngredients(food.getIngredients());
        foodResponse.setAvailable(food.isAvailable());
        foodResponse.setCategoryId(food.getCategory().getCategoryId());
        foodResponse.setCreatedBy(food.getCreatedBy());
        foodResponse.setCreatedDate(food.getCreatedDate());
        foodResponse.setUpdatedDate(food.getUpdatedDate());
        foodResponse.setUpdatedBy(food.getUpdatedBy());
        return foodResponse;
    }

    private void checkExist(Integer id) {
        if (foodRepository.findById(id).isEmpty()) {
            LOGGER.error("No food was found!");
            throw new CustomValidationException(List.of("No food was found!"));
        }
    }
}
