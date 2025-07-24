package com.example.YAPO.service;

import com.example.YAPO.models.enums.ErrorList;
import com.example.YAPO.repositories.UserRepo;
import com.example.YAPO.models.User;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;
    AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserService(UserRepo userRepo, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    private final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public User registerUser(User user, String role){
        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(role);
        try {
            user = userRepo.save(user);
        }
        catch (DataIntegrityViolationException e) {
            throw new ValidationException(ErrorList.USERNAME_OR_EMAIL_ALREADY_IN_USE.toString());
        } catch (ValidationException e) {
            throw new ValidationException(ErrorList.VALIDATION_ERROR.toString());
        }
        return user;
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

    public User getUserByUsername(String name) {
        return userRepo.findByUsername(name);
    }
}
