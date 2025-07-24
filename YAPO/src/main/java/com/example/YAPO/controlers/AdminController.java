package com.example.YAPO.controlers;

import com.example.YAPO.models.User;
import com.example.YAPO.models.enums.Roles;
import com.example.YAPO.service.UserService;
import jakarta.validation.Valid;
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
    public User createAdmin(@RequestBody @Valid User user) {
        return userService.registerUser(user, Roles.admin.toString());
    }
}
