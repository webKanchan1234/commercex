package com.commercex.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        System.out.println("Health Controller Hit");
        return "Auth Service Running";
    }

}
