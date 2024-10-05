package com.example.snackordering.controller;

import com.example.snackordering.model.ResponseModel.ResponseDTO;
import com.example.snackordering.model.account.AccountRequest;
import com.example.snackordering.model.account.AccountResponse;
import com.example.snackordering.service.AccountService;
import com.example.snackordering.util.ResponseUtil;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Validated
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<AccountResponse> result = accountService.findAll();
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable Long id) {
        AccountResponse result = accountService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> update(@Valid @RequestBody AccountRequest request) {
        AccountResponse result = accountService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@Valid @RequestBody AccountRequest request) {
        AccountResponse result = accountService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}