package com.example.snackordering.controller;

import com.example.snackordering.model.ResponseModel.ResponseDTO;
import com.example.snackordering.model.food.FoodRequest;
import com.example.snackordering.model.food.FoodResponse;
import com.example.snackordering.service.FoodService;
import com.example.snackordering.util.ResponseUtil;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
@Validated
public class FoodController {
    private final FoodService foodService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<FoodResponse> result = foodService.findAll();
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable Integer id) {
        FoodResponse result = foodService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping(consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO> update(
            @RequestParam("foodName") String foodName,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("ingredients") String ingredients,
            @RequestParam("isAvailable") boolean isAvailable,
            @RequestParam("image") MultipartFile image) {

        FoodRequest request = new FoodRequest();
        request.setFoodName(foodName);
        request.setDescription(description);
        request.setPrice(price);
        request.setCategoryId(categoryId);
        request.setIngredients(ingredients);
        request.setAvailable(isAvailable);

        FoodResponse result = foodService.save(request, image);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO> create(
            @RequestParam("foodName") String foodName,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("ingredients") String ingredients,
            @RequestParam("isAvailable") boolean isAvailable,
            @RequestParam("image") MultipartFile image) {

        FoodRequest request = new FoodRequest();
        request.setFoodName(foodName);
        request.setDescription(description);
        request.setPrice(price);
        request.setCategoryId(categoryId);
        request.setIngredients(ingredients);
        request.setAvailable(isAvailable);

        FoodResponse result = foodService.save(request, image);
        return ResponseUtil.getObject(result, HttpStatus.CREATED, "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Integer id) {
        foodService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}
