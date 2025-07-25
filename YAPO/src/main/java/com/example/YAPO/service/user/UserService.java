package com.example.YAPO.service.user;

import com.example.YAPO.models.enums.ErrorList;
import com.example.YAPO.repositories.user.RoleRepo;
import com.example.YAPO.repositories.user.UserRepo;
import com.example.YAPO.models.User.User;
import com.example.YAPO.service.JWTService;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepo userRepo;
    AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final RoleRepo roleRepo;

    public UserService(UserRepo userRepo, AuthenticationManager authenticationManager, JWTService jwtService, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.roleRepo = roleRepo;
    }

    private final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public User registerUser(User user, String role){
        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        user.getRoles().add(roleRepo.findByName(role));
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

    @Transactional
    public void deactivateUser(User user) {
        user.setEnabled(false);
        try{
            userRepo.save(user);
        } catch (DataIntegrityViolationException | ValidationException e) {
            throw new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
        }

    }

    public void banUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException(ErrorList.USER_NOT_FOUND.toString()));
        user.setLocked(true);

        try {
            userRepo.save(user);
        } catch (DataIntegrityViolationException | ValidationException e) {
            throw new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
        }
    }
}
