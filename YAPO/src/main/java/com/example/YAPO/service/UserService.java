package com.example.YAPO.service;

import com.example.YAPO.repositories.UserRepo;
import com.example.YAPO.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public User registerUser(User user, String role) {
        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(role);
        return userRepo.save(user);
    }

    public String verifyUser(User user) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                user.getPassword())
                );
        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());
        return "fail";
    }
}
