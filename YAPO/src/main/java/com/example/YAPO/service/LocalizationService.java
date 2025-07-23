package com.example.YAPO.service;

import com.example.YAPO.models.UpdateField;
import com.example.YAPO.models.plant.Localization;
import com.example.YAPO.models.User;
import com.example.YAPO.repositories.LocalizationRepo;
import com.example.YAPO.repositories.UserRepo;
import com.example.YAPO.utility.ValueConverter;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class LocalizationService {
    private final LocalizationRepo  localizationRepo;
    private final UserRepo userRepo;

    public LocalizationService(LocalizationRepo localizationRepo, UserRepo userRepo) {
        this.localizationRepo = localizationRepo;
        this.userRepo = userRepo;
    }

    public List<Localization> getAllLocalizationsByUsername(String username) {
        return localizationRepo.findByUser_Username(username);
    }

    public ResponseEntity<Object> createLocalization(Long userID, Localization localization) {
        User user = userRepo.findById(userID)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        localization.setUser(user);
        try {
            return ResponseEntity.ok(localizationRepo.save(localization));
        } catch (DataIntegrityViolationException |
                 ConstraintViolationException | TransactionSystemException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Localization getLocalizationByIdAndUsername(String username, long id) {
        return localizationRepo.findByIdAndUser_Username(id, username);
    }

    public ResponseEntity<Object> updateLocalization(String username, UpdateField updateField, long id) {
        List<String> allowedFields = List.of("name");
        if (!allowedFields.contains(updateField.getFieldName())) {
            return ResponseEntity.badRequest().body("Wrong Field");
        }
        Localization _localization = localizationRepo.findByIdAndUser_Username(id, username);

        try {
            Field field = Localization.class.getDeclaredField(updateField.getFieldName());
            field.setAccessible(true);

            Object convertedValue = ValueConverter.convert(field.getType(), updateField.getFieldValue());
            field.set(_localization, convertedValue);

            localizationRepo.save(_localization);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<Object> deleteByIdAndUsername(long id, String username) {
        try {
            localizationRepo.deleteByIdAndUser_Username(id, username);
            return checkIfLocalizationExist(id) ? ResponseEntity.badRequest().build() :  ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException | TransactionSystemException e ) {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean checkIfLocalizationExist(long id) {
        return localizationRepo.existsById(id);
    }
}
