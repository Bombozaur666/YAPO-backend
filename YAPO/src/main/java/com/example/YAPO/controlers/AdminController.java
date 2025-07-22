package com.example.YAPO.controlers;

import com.example.YAPO.models.User;
import com.example.YAPO.service.UserService;
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
    public ResponseEntity<?> createAdmin(@RequestBody User user){
        String role = "admin";
        User newUser = userService.registerUser(user, role);
        return  newUser.getId() != null ? ResponseEntity.ok(newUser) : ResponseEntity.badRequest().body("There was an error registering the user.\nTry other values");
    }
}
