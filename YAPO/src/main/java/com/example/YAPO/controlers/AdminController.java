package com.example.YAPO.controlers;

import com.example.YAPO.models.User;
import com.example.YAPO.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('admin')")
@CrossOrigin(origins = "https://localhost:4200/")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String adminHomePage() {
        return "Admin Page";
    }

    @PostMapping("/create-admin")
    public User createAdmin(@RequestBody User user) {
        String role = "admin";
        return userService.registerUser(user, role);
    }
}
