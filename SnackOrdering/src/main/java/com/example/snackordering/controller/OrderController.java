package com.example.snackordering.controller;

import com.example.snackordering.model.ResponseModel.ResponseDTO;
import com.example.snackordering.model.order.OrderRequest;
import com.example.snackordering.model.order.OrderResponse;
import com.example.snackordering.service.OrderService;
import com.example.snackordering.util.ResponseUtil;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<OrderResponse> result = orderService.findAll();
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable Integer id) {
        OrderResponse result = orderService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> update(@Valid @RequestBody OrderRequest request) {
        OrderResponse result = orderService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@Valid @RequestBody OrderRequest request) {
        OrderResponse result = orderService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Integer id) {
        orderService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}