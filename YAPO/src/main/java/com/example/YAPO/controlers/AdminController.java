package com.example.YAPO.controlers;

import com.example.YAPO.models.User;
import com.example.YAPO.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('admin')")
@CrossOrigin(origins = "https://localhost:4200/")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String adminHomePage() {
        return "Admin Page";
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody @Valid User user) {
        String role = "admin";
        return userService.registerUser(user, role);
    }
}
