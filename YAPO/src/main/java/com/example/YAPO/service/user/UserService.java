package com.example.YAPO.service.user;

import com.example.YAPO.models.User.VerificationToken;
import com.example.YAPO.models.enums.ErrorList;
import com.example.YAPO.repositories.user.RoleRepo;
import com.example.YAPO.repositories.user.UserRepo;
import com.example.YAPO.models.User.User;
import com.example.YAPO.repositories.user.VerificationTokenRepo;
import com.example.YAPO.service.EmailService;
import com.example.YAPO.service.JWTService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepo userRepo;
    AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final RoleRepo roleRepo;
    private final VerificationTokenRepo verificationTokenRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, AuthenticationManager authenticationManager, JWTService jwtService, RoleRepo roleRepo, VerificationTokenRepo verificationTokenRepo, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.roleRepo = roleRepo;
        this.verificationTokenRepo = verificationTokenRepo;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user, String role){

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(roleRepo.findByName(role));
        try {
            user = userRepo.save(user);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(ErrorList.USERNAME_OR_EMAIL_ALREADY_IN_USE.toString());
        } catch (ValidationException e) {
            throw new ValidationException(ErrorList.VALIDATION_ERROR.toString());
        }

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        try {
            verificationTokenRepo.save(verificationToken);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(ErrorList.USERNAME_OR_EMAIL_ALREADY_IN_USE.toString());
        }

        String link = "http://localhost:8080/user/confirm?token=" + token;

        try {
            emailService.sendConfirmationEmail(user.getEmail(), "Account Activation", link);
        } catch (MessagingException e) {
           throw new RuntimeException(ErrorList.ERROR_DURING_SENDING_MAIL.toString());
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

    @Transactional
    public void deactivateUser(User user) {
        user.setEnabled(false);
        try{
            userRepo.save(user);
        } catch (DataIntegrityViolationException | ValidationException e) {
            throw new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
        }

    }

    @Transactional
    public void banUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException(ErrorList.USER_NOT_FOUND.toString()));
        user.setLocked(true);

        try {
            userRepo.save(user);
        } catch (DataIntegrityViolationException | ValidationException e) {
            throw new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
        }
    }

    public void reactivateUser(User user) {
        User _user = userRepo.findByUsername(user.getUsername());
        if (_user == null) {throw new RuntimeException(ErrorList.USER_NOT_FOUND.toString());}

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(_user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        try {
            verificationTokenRepo.save(verificationToken);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(ErrorList.USERNAME_OR_EMAIL_ALREADY_IN_USE.toString());
        }

        String link = "http://localhost:8080/user/?token=" + token;

        try {
            emailService.sendConfirmationEmail(user.getEmail(), "Account Reactivation", link);
        } catch (MessagingException e) {
            throw new RuntimeException(ErrorList.ERROR_DURING_SENDING_MAIL.toString());
        }
    }

    public void forgotPassword(@Valid User user) {
    }

    @Transactional
    public void enableUser(String token) {
        Optional<VerificationToken> optional = verificationTokenRepo.findByToken(token);
        if(optional.isEmpty()){ throw new RuntimeException(ErrorList.INVALID_TOKEN.toString()); }

        VerificationToken verificationToken =optional.get();
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(ErrorList.TOKEN_EXPIRED.toString());
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        try {
            userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
        } catch (ValidationException e) {
            throw new ValidationException(ErrorList.VALIDATION_ERROR.toString());
        }

        try {
            verificationTokenRepo.delete(verificationToken);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(ErrorList.UNEXPECTED_ERROR_DURING_DELETE.toString());
        }
    }

    @Transactional
    public void resetUserPassword(String token, User user) {
        Optional<VerificationToken> optional = verificationTokenRepo.findByToken(token);
        if(optional.isEmpty()){ throw new RuntimeException(ErrorList.INVALID_TOKEN.toString()); }

        VerificationToken verificationToken =optional.get();
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException(ErrorList.TOKEN_EXPIRED.toString());
        }

        User _user = verificationToken.getUser();
        _user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
        } catch (ValidationException e) {
            throw new ValidationException(ErrorList.VALIDATION_ERROR.toString());
        }
    }
}
