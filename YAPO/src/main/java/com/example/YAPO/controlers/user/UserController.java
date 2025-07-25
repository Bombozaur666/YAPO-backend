package com.example.YAPO.controlers.user;

import com.example.YAPO.models.User.MyUserDetails;
import com.example.YAPO.models.User.PasswordField;
import com.example.YAPO.models.User.User;
import com.example.YAPO.models.User.UsernameField;
import com.example.YAPO.models.enums.Roles;
import com.example.YAPO.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public User getUser(@AuthenticationPrincipal MyUserDetails userDetails) {
        return userDetails.getUser();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody @Valid User user){
        return  userService.registerUser(user, Roles.ROLE_USER.toString());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        String response = userService.verifyUser(user);
        return !Objects.equals(response, "fail") ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateUser(@AuthenticationPrincipal MyUserDetails userDetails) {
        userService.deactivateUser(userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid UsernameField username) {
        userService.forgotPassword(username.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reactivate-user")
    public ResponseEntity<?> restoreUser(@RequestBody UsernameField usernameField){
        userService.reactivateUser(usernameField.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping( "/enable")
    public ResponseEntity<?> enableUser(@RequestParam String token ){
        userService.enableUser(token);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/reset")
    public ResponseEntity<?> resetUserPassword(@RequestParam String token , @RequestBody @Valid PasswordField  passwordField){
        userService.resetUserPassword(token, passwordField);
        return ResponseEntity.ok().build();
    }
}
