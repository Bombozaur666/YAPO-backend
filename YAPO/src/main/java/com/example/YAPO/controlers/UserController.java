package com.example.YAPO.controlers;

import com.example.YAPO.models.User;
import com.example.YAPO.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "https://localhost:4200/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public User getUser(HttpServletRequest request) {
        return userService.getUserByUsername(request.getUserPrincipal().getName());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        String role = "user";
        User newUser = userService.registerUser(user, role);
        return  newUser.getId() != null ? ResponseEntity.ok(newUser) : ResponseEntity.badRequest().body("There was an error registering the user.\nTry other values");
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        return userService.verifyUser(user);
    }
}
