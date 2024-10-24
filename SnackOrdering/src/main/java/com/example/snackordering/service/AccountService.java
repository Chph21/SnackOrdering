package com.example.snackordering.service;

import com.example.snackordering.entity.AccountEntity;
import com.example.snackordering.entity.Branch;
import com.example.snackordering.model.account.AccountRequest;
import com.example.snackordering.model.account.AccountResponse;
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
public class AccountService {
    private final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final BranchRepository branchRepository;

    public List<AccountResponse> findAll() {
        LOGGER.info("Find all accounts");
        List<AccountEntity> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            LOGGER.warn("No accounts were found!");
        }

        return accounts.stream()
                .map(this::accountResponseGenerator)
                .collect(Collectors.toList());
    }

    public AccountResponse findById(Long id) {
        LOGGER.info("Find account with id " + id);
        Optional<AccountEntity> account = accountRepository.findById(String.valueOf(id));
        if (account.isEmpty()) {
            LOGGER.warn("No account was found!");
            return null;
        }
        return account.map(this::accountResponseGenerator).get();
    }

    public AccountResponse findByPhone(String phone) {
        LOGGER.info("Find account with phone number: " + phone);
        Optional<AccountEntity> account = accountRepository.findByPhone(phone);
        if (account.isEmpty()) {
            LOGGER.warn("No account was found with phone number: " + phone);
            return null;
        }
        return account.map(this::accountResponseGenerator).get();
    }

    public AccountResponse save(AccountRequest accountRequest) {
        AccountEntity account;
//        if (accountRequest.getBranchId() != null) {
//            Optional<Branch> branch = branchRepository.findById(accountRequest.getBranchId());
//            if (branch.isEmpty()) {
//                LOGGER.error("Branch with id " + accountRequest.getBranchId() + " not found!");
//                throw new CustomValidationException(List.of("Branch not found!"));
//            }
//        }

        if (accountRequest.getAccountId() != null) {
            LOGGER.info("Update account with id " + accountRequest.getAccountId());
            checkExist(accountRequest.getAccountId());
            account = accountRepository.findById(String.valueOf(accountRequest.getAccountId())).get();
            updateAccount(account, accountRequest);
            accountRepository.save(account);
        } else {
            LOGGER.info("Create new account");
            account = createAccount(accountRequest);
            accountRepository.save(account);
        }
        return accountResponseGenerator(account);
    }

    public void delete(Long id) {
        if (id != null) {
            LOGGER.info("Delete account with id " + id);
            checkExist(id);
            AccountEntity account = accountRepository.findById(String.valueOf(id)).get();
            accountRepository.delete(account);
        }
    }

    private AccountEntity createAccount(AccountRequest request) {
        AccountEntity account = new AccountEntity();
        setCommonFields(account, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        account.setCreatedBy(authentication.getName());
        return account;
    }

    private void updateAccount(AccountEntity account, AccountRequest request) {
        setCommonFields(account, request);
    }

    private void setCommonFields(AccountEntity account, AccountRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setPhone(request.getPhone());
        account.setAddress(request.getAddress());
        account.setBirthday(request.getBirthday());
        account.setEmail(request.getEmail());
        account.setUpdatedBy(authentication.getName());
    }

    private AccountResponse accountResponseGenerator(AccountEntity account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountId(account.getAccountId());
        accountResponse.setFirstName(account.getFirstName());
        accountResponse.setLastName(account.getLastName());
        accountResponse.setPhone(account.getPhone());
        accountResponse.setAddress(account.getAddress());
        accountResponse.setBirthday(account.getBirthday());
        accountResponse.setEmail(account.getEmail());
        accountResponse.setCreatedBy(account.getCreatedBy());
        accountResponse.setCreatedDate(account.getCreatedDate());
        accountResponse.setUpdatedDate(account.getUpdatedDate());
        accountResponse.setUpdatedBy(account.getUpdatedBy());
        return accountResponse;
    }

    private void checkExist(Long id) {
        if (accountRepository.findById(String.valueOf(id)).isEmpty()) {
            LOGGER.error("No account was found!");
            throw new CustomValidationException(List.of("No account was found!"));
        }
    }
}
