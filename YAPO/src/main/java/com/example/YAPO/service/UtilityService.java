package com.example.YAPO.service;

import com.example.YAPO.models.User.User;
import com.example.YAPO.models.User.VerificationToken;
import com.example.YAPO.models.enums.ErrorList;
import com.example.YAPO.repositories.user.VerificationTokenRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UtilityService {
    private final VerificationTokenRepo verificationTokenRepo;

    public UtilityService(VerificationTokenRepo verificationTokenRepo) {
        this.verificationTokenRepo = verificationTokenRepo;
    }

    @Transactional
    public String tokenGenerator(User user) {
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

        return token;
    }

    public String linkGenerator(String token, String url) {
        return  "http://localhost:8080/user/"+ url + "?token=" + token;
    }
}
