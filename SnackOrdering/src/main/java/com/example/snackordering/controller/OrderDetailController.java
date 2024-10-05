package com.example.snackordering.controller;

import com.example.snackordering.model.ResponseModel.ResponseDTO;
import com.example.snackordering.model.orderDetail.OrderDetailRequest;
import com.example.snackordering.model.orderDetail.OrderDetailResponse;
import com.example.snackordering.service.OrderDetailService;
import com.example.snackordering.util.ResponseUtil;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderDetail")
@RequiredArgsConstructor
@Validated
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<OrderDetailResponse> result = orderDetailService.findAll();
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable Integer id) {
        OrderDetailResponse result = orderDetailService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> update(@Valid @RequestBody OrderDetailRequest request) {
        OrderDetailResponse result = orderDetailService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@Valid @RequestBody OrderDetailRequest request) {
        OrderDetailResponse result = orderDetailService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Integer id) {
        orderDetailService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}