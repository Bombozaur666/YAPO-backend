package com.example.YAPO.controlers;

import com.example.YAPO.models.User;
import com.example.YAPO.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
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
