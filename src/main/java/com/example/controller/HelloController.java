package com.example.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
    @GetMapping("/")
    public String hello() {
        return "Hello from CI/CD Pipeline! Build: " +
               System.getenv().getOrDefault("BUILD_NUMBER", "local");
    }

    @GetMapping("/health")
    public String health() {
        return "{\"status\": \"UP\"}";
    }
}
