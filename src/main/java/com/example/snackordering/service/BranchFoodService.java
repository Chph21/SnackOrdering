package com.example.snackordering.service;

import com.example.snackordering.entity.Branch;
import com.example.snackordering.entity.BranchFood;
import com.example.snackordering.entity.Food;
import com.example.snackordering.model.branchFood.BranchFoodRequest;
import com.example.snackordering.model.branchFood.BranchFoodResponse;
import com.example.snackordering.repository.BranchFoodRepository;
import com.example.snackordering.repository.BranchRepository;
import com.example.snackordering.repository.FoodRepository;
import com.example.snackordering.util.CustomValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchFoodService {
    private final Logger LOGGER = LoggerFactory.getLogger(BranchFoodService.class);
    private final BranchFoodRepository branchFoodRepository;
    private final BranchRepository branchRepository;
    private final FoodRepository foodRepository;

    public List<BranchFoodResponse> findAll() {
        LOGGER.info("Find all branch foods");
        List<BranchFood> branchFoods = branchFoodRepository.findAll();
        if (branchFoods.isEmpty()) {
            LOGGER.warn("No branch foods were found!");
        }

        return branchFoods.stream()
                .map(this::branchFoodResponseGenerator)
                .collect(Collectors.toList());
    }

    public BranchFoodResponse findById(Integer id) {
        LOGGER.info("Find branch food with id " + id);
        Optional<BranchFood> branchFood = branchFoodRepository.findById(id);
        if (branchFood.isEmpty()) {
            LOGGER.warn("No branch food was found!");
            return null;
        }
        return branchFood.map(this::branchFoodResponseGenerator).get();
    }

    public BranchFoodResponse save(BranchFoodRequest branchFoodRequest) {
        BranchFood branchFood;
        Optional<Branch> branch = branchRepository.findById(branchFoodRequest.getBranchId());
        Optional<Food> food = foodRepository.findById(branchFoodRequest.getFoodId());
        if (branch.isEmpty() || food.isEmpty()) {
            throw new CustomValidationException(List.of("No branch or food was found!"));
        }

        if (branchFoodRequest.getBranchFoodId() != null) {
            LOGGER.info("Update branch food with id " + branchFoodRequest.getBranchFoodId());
            checkExist(branchFoodRequest.getBranchFoodId());
            branchFood = branchFoodRepository.findById(branchFoodRequest.getBranchFoodId()).get();
            updateBranchFood(branchFood, branchFoodRequest);
            branchFoodRepository.save(branchFood);
        } else {
            LOGGER.info("Create new branch food");
            branchFood = createBranchFood(branchFoodRequest, branch.get(), food.get());
            branchFoodRepository.save(branchFood);
        }
        return branchFoodResponseGenerator(branchFood);
    }

    public void delete(Integer id) {
        if (id != null) {
            LOGGER.info("Delete branch food with id " + id);
            checkExist(id);
            BranchFood branchFood = branchFoodRepository.findById(id).get();
            branchFoodRepository.delete(branchFood);
        }
    }

    private BranchFood createBranchFood(BranchFoodRequest request, Branch branch, Food food) {
        BranchFood branchFood = new BranchFood();
        setCommonFields(branchFood, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        branchFood.setCreatedBy(authentication.getName());
        branchFood.setBranch(branch);
        branchFood.setFood(food);
        return branchFood;
    }

    private void updateBranchFood(BranchFood branchFood, BranchFoodRequest request) {
        setCommonFields(branchFood, request);
        Branch branch = branchRepository.findById(request.getBranchId()).get();
        Food food = foodRepository.findById(request.getFoodId()).get();
        branchFood.setBranch(branch);
        branchFood.setFood(food);
    }

    private void setCommonFields(BranchFood branchFood, BranchFoodRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        branchFood.setQuantity(request.getQuantity());
        branchFood.setUpdatedBy(authentication.getName());
    }

    private BranchFoodResponse branchFoodResponseGenerator(BranchFood branchFood) {
        BranchFoodResponse branchFoodResponse = new BranchFoodResponse();
        branchFoodResponse.setBranchFoodId(branchFood.getBranchFoodId());
        branchFoodResponse.setBranchId(branchFood.getBranch().getBranchId());
        branchFoodResponse.setFoodId(branchFood.getFood().getFoodId());
        branchFoodResponse.setQuantity(branchFood.getQuantity());
        branchFoodResponse.setCreatedBy(branchFood.getCreatedBy());
        branchFoodResponse.setCreatedDate(branchFood.getCreatedDate());
        branchFoodResponse.setUpdatedDate(branchFood.getUpdatedDate());
        branchFoodResponse.setUpdatedBy(branchFood.getUpdatedBy());
        return branchFoodResponse;
    }

    private void checkExist(Integer id) {
        if (branchFoodRepository.findById(id).isEmpty()) {
            LOGGER.error("No branch food was found!");
            throw new CustomValidationException(List.of("No branch food was found!"));
        }
    }
}