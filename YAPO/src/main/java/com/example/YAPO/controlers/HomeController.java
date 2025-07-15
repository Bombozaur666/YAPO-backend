package com.example.YAPO.controlers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
public class HomeController {
    @GetMapping("/")
    public String sayHello() {
        return "Hello World";
    }
}
