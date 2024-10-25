package com.example.snackordering;

import com.example.snackordering.entity.AccountEntity;
import com.example.snackordering.entity.AuthEntity;
import com.example.snackordering.enums.AccountRole;
import com.example.snackordering.repository.AccountRepository;
import com.example.snackordering.repository.AuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@AllArgsConstructor
public class SnackOrderingApplication implements CommandLineRunner {

    private AccountRepository accountRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {

        SpringApplication.run(SnackOrderingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String adminEmail = "admin@gmail.com";
        if (accountRepository.findByEmail(adminEmail).isEmpty()) {
            AccountEntity account = AccountEntity.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .email(adminEmail)
                    .phone("+84123456789")
                    .build();

            AuthEntity auth = AuthEntity.builder()
                    .phone("+84123456789")
                    .password(passwordEncoder.encode("admin"))
                    .role(AccountRole.ADMIN)
                    .isEnable(true)
                    .isBlocked(false)
                    .account(account)
                    .build();
            account.setAuth(auth);

            accountRepository.save(account);
            authRepository.save(auth);
            System.out.println("Admin account created.");
        } else {
            System.out.println("Admin account already exists.");
        }
        String userEmail = "user@gmail.com";
        if (accountRepository.findByEmail(userEmail).isEmpty()) {
            AccountEntity account = AccountEntity.builder()
                    .firstName("User")
                    .lastName("User")
                    .email(userEmail)
                    .phone("+84987654321")
                    .build();

            AuthEntity auth = AuthEntity.builder()
                    .phone("+84987654321")
                    .password(passwordEncoder.encode("user"))
                    .role(AccountRole.USER)
                    .isEnable(true)
                    .isBlocked(false)
                    .account(account)
                    .build();
            account.setAuth(auth);

            accountRepository.save(account);
            authRepository.save(auth);
            System.out.println("User account created.");
        } else {
            System.out.println("User account already exists.");
        }
    }

}
