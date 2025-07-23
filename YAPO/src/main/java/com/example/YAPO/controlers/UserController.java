package com.example.YAPO.controlers;

import com.example.YAPO.models.User;
import com.example.YAPO.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user){
        String role = "user";
        return  userService.registerUser(user, role);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid User user) {
        String response = userService.verifyUser(user);
        return !Objects.equals(response, "fail") ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
