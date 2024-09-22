package com.example.snackordering.service;

import com.example.snackordering.entity.Category;
import com.example.snackordering.entity.Food;
import com.example.snackordering.model.category.CategoryRequest;
import com.example.snackordering.model.category.CategoryResponse;
import com.example.snackordering.repository.CategoryRepository;
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
public class CategoryService {

    private final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> findAll() {
        LOGGER.info("Find all categorys");
        List<Category> categorys = categoryRepository.findAll();
        if (categorys.isEmpty()) {
            LOGGER.warn("No category was found!");
        }
        return categorys.stream()
                .map(this::categoryResponseGenerator)
                .collect(Collectors.toList());
    }

    public CategoryResponse findById(Integer id) {
        LOGGER.info("Find category with id " + id);
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            LOGGER.warn("No category was found!");
            return null;
        }
        return category.map(this::categoryResponseGenerator).get();
    }

    public CategoryResponse save(CategoryRequest categoryRequest) {
        Category category;

        if (categoryRequest.getCategoryId() != null) {
            LOGGER.info("Update category with id " + categoryRequest.getCategoryId());
            checkExist(categoryRequest.getCategoryId());
            category = categoryRepository.findById(categoryRequest.getCategoryId()).get();
            updateCategory(category, categoryRequest);
            categoryRepository.save(category);
        } else {
            LOGGER.info("Create new category");
            category = createCategory(categoryRequest);
            categoryRepository.save(category);
        }
        return categoryResponseGenerator(category);
    }

    public void delete(Integer id) {
        if (id != null) {
            LOGGER.info("Delete category with id " + id);
            checkExist(id);
            Category category = categoryRepository.findById(id).get();
            categoryRepository.delete(category);
        }
    }

    private Category createCategory(CategoryRequest request) {
        Category category = new Category();
        updateCategory(category, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        category.setCreatedBy(authentication.getName());
        category.setCreatedDate(new Date());
        return category;
    }

    private void updateCategory(Category category, CategoryRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        category.setDescription(request.getDescription());
        category.setUpdatedBy(authentication.getName());
    }

    private CategoryResponse categoryResponseGenerator(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryId(category.getCategoryId());
        categoryResponse.setDescription(category.getDescription());
        categoryResponse.setCreatedBy(category.getCreatedBy());
        categoryResponse.setCreatedDate(category.getCreatedDate());
        categoryResponse.setUpdatedBy(category.getUpdatedBy());
        categoryResponse.setUpdatedDate(category.getUpdatedDate());
        categoryResponse.setFoodIds(
                category.getFoods()
                        .stream()
                        .map(Food::getFoodId)
                        .collect(Collectors.toList()));
        return categoryResponse;
    }

    private void checkExist(Integer id) {
        if (categoryRepository.findById(id).isEmpty()) {
            LOGGER.error("No category was found!");
            throw new CustomValidationException(List.of("No category was found!"));
        }
    }
}
