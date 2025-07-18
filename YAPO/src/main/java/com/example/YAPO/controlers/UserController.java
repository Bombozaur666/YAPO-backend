package com.example.YAPO.controlers;

import com.example.YAPO.models.User;
import com.example.YAPO.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "https://localhost:4200/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        String role = "user";
        return userService.registerUser(user, role);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        return userService.verifyUser(user);
    }
}
