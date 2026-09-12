package com.commercex.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
@Tag(
        name = "Customer",
        description = "Customer secured APIs."
)
public class CustomerController {

    @Operation(summary = "Customer Profile")
    @GetMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String profile() {

        return "Customer Profile";

    }

}
