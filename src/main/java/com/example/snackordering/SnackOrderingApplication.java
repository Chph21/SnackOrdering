package com.example.snackordering;

import com.example.snackordering.entity.AccountEntity;
import com.example.snackordering.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class SnackOrderingApplication implements CommandLineRunner {

    private AccountRepository accountRepository;

    public static void main(String[] args) {

        SpringApplication.run(SnackOrderingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String adminEmail = "admin@example.com";
        if (accountRepository.findByEmail(adminEmail).isEmpty()) {
            AccountEntity adminAccount = new AccountEntity();
            adminAccount.setEmail(adminEmail);
            adminAccount.setFirstName("Admin");
            adminAccount.setLastName("User");
            adminAccount.setPhone("1234567890");
            adminAccount.setAddress("Admin Address");
            adminAccount.setCreatedBy("system");
            adminAccount.setUpdatedBy("system");
            // Set other necessary fields and default values
            accountRepository.save(adminAccount);
            System.out.println("Admin account created.");
        } else {
            System.out.println("Admin account already exists.");
        }
    }

}
