package com.example.YAPO.service.plant;

import com.example.YAPO.models.UpdateField;
import com.example.YAPO.models.enums.ErrorList;
import com.example.YAPO.models.plant.Localization;
import com.example.YAPO.models.User.User;
import com.example.YAPO.repositories.plant.LocalizationRepo;
import com.example.YAPO.repositories.user.UserRepo;
import com.example.YAPO.utility.ValueConverter;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @Transactional
    public Localization createLocalization(Long userID, Localization localization) {
        User user = userRepo.findById(userID)
                .orElseThrow(() -> new RuntimeException(ErrorList.USER_NOT_FOUND.toString()));

        localization.setUser(user);
        try {
             localizationRepo.save(localization);
        } catch (DataIntegrityViolationException |
                 ConstraintViolationException | TransactionSystemException e) {
            throw  new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
        }
        return localization;
    }

    public Localization getLocalizationByIdAndUsername(String username, long id) {
        return localizationRepo.findByIdAndUser_Username(id, username);
    }

    @Transactional
    public Localization updateLocalization(String username, UpdateField updateField, long id) {
        List<String> allowedFields = List.of("name");
        if (!allowedFields.contains(updateField.getFieldName())) {
            throw new RuntimeException(ErrorList.WRONG_FIELD_TO_UPDATE.toString());
        }
        Localization _localization = localizationRepo.findByIdAndUser_Username(id, username);

        try {
            Field field = Localization.class.getDeclaredField(updateField.getFieldName());
            field.setAccessible(true);

            Object convertedValue = ValueConverter.convert(field.getType(), updateField.getFieldValue());
            field.set(_localization, convertedValue);

            _localization = localizationRepo.save(_localization);
        } catch (NoSuchFieldException | IllegalAccessException |DataIntegrityViolationException |
                 ConstraintViolationException | TransactionSystemException e) {
            throw  new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
        }
        return _localization;
    }

    @Transactional
    public boolean deleteByIdAndUsername(long id, String username) {
        try {
            localizationRepo.deleteByIdAndUser_Username(id, username);
        } catch (EmptyResultDataAccessException | DataIntegrityViolationException | TransactionSystemException e ) {
            throw  new RuntimeException(ErrorList.UNEXPECTED_ERROR_DURING_DELETE.toString());
        }
        return !checkIfLocalizationExist(id);
    }

    private boolean checkIfLocalizationExist(long id) {
        return localizationRepo.existsById(id);
    }
}
