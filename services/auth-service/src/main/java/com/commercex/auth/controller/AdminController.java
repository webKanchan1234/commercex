package com.commercex.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Tag(
        name = "Admin",
        description = "Administrator secured APIs."
)
public class AdminController {

    @GetMapping("/dashboard")
    @Operation(summary = "Admin Dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String dashboard() {

        return "Admin Dashboard";

    }

}
