package com.example.inventorymanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/all-error")
public class ErrorController {
    @GetMapping("/403")
    public String getForbiddenPage() {
        return "error/403";
    }
}
