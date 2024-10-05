package com.example.snackordering.controller;

import com.example.snackordering.model.ResponseModel.ResponseDTO;
import com.example.snackordering.model.branchFood.BranchFoodRequest;
import com.example.snackordering.model.branchFood.BranchFoodResponse;
import com.example.snackordering.service.BranchFoodService;
import com.example.snackordering.util.ResponseUtil;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branchFood")
@RequiredArgsConstructor
@Validated
public class BranchFoodController {
    private final BranchFoodService branchFoodService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<BranchFoodResponse> result = branchFoodService.findAll();
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable Integer id) {
        BranchFoodResponse result = branchFoodService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> update(@Valid @RequestBody BranchFoodRequest request) {
        BranchFoodResponse result = branchFoodService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@Valid @RequestBody BranchFoodRequest request) {
        BranchFoodResponse result = branchFoodService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Integer id) {
        branchFoodService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}