package com.commercex.auth;

import com.commercex.auth.entity.Role;
import com.commercex.auth.entity.User;
import com.commercex.auth.entity.UserStatus;
import com.commercex.auth.enums.RoleName;
import com.commercex.auth.repository.RoleRepository;
import com.commercex.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataFactory {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public User createVerifiedCustomer() {

        User user = User.builder()
                .firstName("Kanchan")
                .lastName("Kumar")
                .email("kanchan@test.com")
                .password(passwordEncoder.encode("Password@123"))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .status(UserStatus.ACTIVE)
                .failedAttempts(0)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public User createDisabledCustomer() {

        User user = User.builder()
                .firstName("Disabled")
                .lastName("User")
                .email("disabled@test.com")
                .phone("9876543211")
                .password(passwordEncoder.encode("Password@123"))
                .status(UserStatus.ACTIVE)

                // User not verified
                .enabled(false)

                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .failedAttempts(0)

                .build();

        Role customerRole = roleRepository
                .findByName(String.valueOf(RoleName.ROLE_CUSTOMER))
                .orElseThrow(() ->
                        new RuntimeException("Customer role not found"));

        user.getRoles().add(customerRole);

        return userRepository.save(user);
    }

}
