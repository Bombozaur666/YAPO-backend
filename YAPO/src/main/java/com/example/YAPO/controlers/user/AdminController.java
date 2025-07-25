package com.example.YAPO.controlers.user;

import com.example.YAPO.models.User.User;
import com.example.YAPO.models.enums.Roles;
import com.example.YAPO.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
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
        return userService.registerUser(user, Roles.ROLE_ADMIN.toString());
    }

    @PostMapping("/ban-user/{userId}")
    public ResponseEntity<?> banUser(@PathVariable Long userId) {
        userService.banUser(userId);
        return ResponseEntity.ok().build();
    }

}
