package com.example.snackordering.service;

import com.example.snackordering.entity.AccountEntity;
import com.example.snackordering.entity.Branch;
import com.example.snackordering.model.branch.BranchRequest;
import com.example.snackordering.model.branch.BranchResponse;
import com.example.snackordering.repository.AccountRepository;
import com.example.snackordering.repository.BranchRepository;
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
public class BranchService {
    private final Logger LOGGER = LoggerFactory.getLogger(BranchService.class);
    private final BranchRepository branchRepository;
    private final AccountRepository accountRepository;

    public List<BranchResponse> findAll() {
        LOGGER.info("Find all branches");
        List<Branch> branches = branchRepository.findAll();
        if (branches.isEmpty()) {
            LOGGER.warn("No branches were found!");
        }

        return branches.stream()
                .map(this::branchResponseGenerator)
                .collect(Collectors.toList());
    }

    public BranchResponse findById(Integer id) {
        LOGGER.info("Find branch with id " + id);
        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            LOGGER.warn("No branch was found!");
            return null;
        }
        return branch.map(this::branchResponseGenerator).get();
    }

    public BranchResponse save(BranchRequest branchRequest) {
        Branch branch;
        Optional<AccountEntity> account = accountRepository.findById(String.valueOf(branchRequest.getAccountId()));
        if (account.isEmpty()) {
            throw new CustomValidationException(List.of("No account was found!"));
        }

        if (branchRequest.getBranchId() != null) {
            LOGGER.info("Update branch with id " + branchRequest.getBranchId());
            checkExist(branchRequest.getBranchId());
            branch = branchRepository.findById(branchRequest.getBranchId()).get();
            updateBranch(branch, branchRequest);
            branchRepository.save(branch);
        } else {
            LOGGER.info("Create new branch");
            branch = createBranch(branchRequest);
            branchRepository.save(branch);
        }
        return branchResponseGenerator(branch);
    }

    public void delete(Integer id) {
        if (id != null) {
            LOGGER.info("Delete branch with id " + id);
            checkExist(id);
            Branch branch = branchRepository.findById(id).get();
            branchRepository.delete(branch);
        }
    }

    private Branch createBranch(BranchRequest request) {
        Branch branch = new Branch();
        setCommonFields(branch, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        branch.setCreatedBy(authentication.getName());
        return branch;
    }

    private void updateBranch(Branch branch, BranchRequest request) {
        setCommonFields(branch, request);
        AccountEntity account = accountRepository.findById(String.valueOf(request.getAccountId())).get();
        branch.setAccount(account);
    }

    private void setCommonFields(Branch branch, BranchRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        branch.setBranchName(request.getBranchName());
        branch.setAddress(request.getAddress());
        branch.setPhone(request.getPhone());
        branch.setEmail(request.getEmail());
        branch.setDescription(request.getDescription());
        branch.setImage(request.getImage());
        branch.setManager(request.getManager());
        branch.setManagerPhone(request.getManagerPhone());
        branch.setManagerEmail(request.getManagerEmail());
        branch.setManagerAddress(request.getManagerAddress());
        branch.setManagerImage(request.getManagerImage());
        branch.setManagerDescription(request.getManagerDescription());
        branch.setManagerStatus(request.getManagerStatus());
        branch.setManagerOpeningHour(request.getManagerOpeningHour());
        branch.setStatus(request.getStatus());
        branch.setOpeningHour(request.getOpeningHour());
        branch.setLatitude(request.getLatitude());
        branch.setLongitude(request.getLongitude());
        branch.setUpdatedBy(authentication.getName());
        branch.setUpdatedDate(new Date());
    }

    private BranchResponse branchResponseGenerator(Branch branch) {
        BranchResponse branchResponse = new BranchResponse();
        branchResponse.setBranchId(branch.getBranchId());
        branchResponse.setBranchName(branch.getBranchName());
        branchResponse.setAddress(branch.getAddress());
        branchResponse.setPhone(branch.getPhone());
        branchResponse.setEmail(branch.getEmail());
        branchResponse.setDescription(branch.getDescription());
        branchResponse.setImage(branch.getImage());
        branchResponse.setManager(branch.getManager());
        branchResponse.setManagerPhone(branch.getManagerPhone());
        branchResponse.setManagerEmail(branch.getManagerEmail());
        branchResponse.setManagerAddress(branch.getManagerAddress());
        branchResponse.setManagerImage(branch.getManagerImage());
        branchResponse.setManagerDescription(branch.getManagerDescription());
        branchResponse.setManagerStatus(branch.getManagerStatus());
        branchResponse.setManagerOpeningHour(branch.getManagerOpeningHour());
        branchResponse.setStatus(branch.getStatus());
        branchResponse.setOpeningHour(branch.getOpeningHour());
        branchResponse.setLatitude(branch.getLatitude());
        branchResponse.setLongitude(branch.getLongitude());
        branchResponse.setCreatedBy(branch.getCreatedBy());
        branchResponse.setCreatedDate(branch.getCreatedDate());
        branchResponse.setUpdatedDate(branch.getUpdatedDate());
        branchResponse.setUpdatedBy(branch.getUpdatedBy());
        return branchResponse;
    }

    private void checkExist(Integer id) {
        if (branchRepository.findById(id).isEmpty()) {
            LOGGER.error("No branch was found!");
            throw new CustomValidationException(List.of("No branch was found!"));
        }
    }
}