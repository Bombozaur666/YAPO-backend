package com.example.YAPO.service.plant;

import com.example.YAPO.models.*;
import com.example.YAPO.models.User.User;
import com.example.YAPO.models.enums.ErrorList;
import com.example.YAPO.models.plant.Localization;
import com.example.YAPO.models.plant.Plant;
import com.example.YAPO.models.plant.PlantUpdate;
import com.example.YAPO.repositories.plant.LocalizationRepo;
import com.example.YAPO.repositories.plant.PlantRepo;
import com.example.YAPO.repositories.user.UserRepo;
import com.example.YAPO.utility.ValueConverter;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class PlantService {
    private final PlantRepo plantRepo;
    private final LocalizationRepo localizationRepo;
    private final UserRepo userRepo;

    public PlantService(PlantRepo plantRepo, LocalizationRepo localizationRepo, UserRepo userRepo) {
        this.plantRepo = plantRepo;
        this.localizationRepo = localizationRepo;
        this.userRepo = userRepo;
    }

    public List<Plant> getAllPlants(String username) {
        return plantRepo.findByUser_Username(username);
    }

    @Transactional
    public Plant createPlant(Plant plant, User user) {
        Localization localization = localizationRepo.findById(plant.getLocalization().getId())
                .orElseThrow(() -> new RuntimeException(ErrorList.LOCALIZATION_NOT_FOUND.toString()));
        User _user = userRepo.findById(user.getId())
                .orElseThrow(() -> new RuntimeException(ErrorList.USER_NOT_FOUND.toString()));
        plant.setLocalization(localization);

        plant.setUser(_user);

        try {
            plant = plantRepo.save(plant);
        } catch (DataIntegrityViolationException |
                 ConstraintViolationException | TransactionSystemException e) {
            throw new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
        }
        return plant;
    }

    public Plant getPlant(Long plant_id, String username) {
        return plantRepo.findByIdAndUser_Username(plant_id, username);
    }

    @Transactional
    public boolean deletePlant(Long plantId, String username) {
        try {
            plantRepo.deletePlantByIdAndUser_Username(plantId, username);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(ErrorList.UNEXPECTED_ERROR_DURING_DELETE.toString());
        }
        return checkIfPlantExist(plantId);
    }

    @Transactional
    public Plant updateField(Long id, User userDetails, UpdateField updateField) {
        List<String> allowedFields = List.of("name", "species", "purchaseDate", "purchaseLocalization", "fertilizationDate", "alive", "deathReason", "wateringDate", "plantCondition", "plantSoil", "plantWatering", "plantBerth", "plantToxicity", "plantLifeExpectancy");

        if (!allowedFields.contains(updateField.getFieldName())) {
            throw new RuntimeException(ErrorList.WRONG_FIELD_TO_UPDATE.toString());
        }
        Plant plant = plantRepo.findByIdAndUser_Username(id, userDetails.getUsername());

        try {
            Field field = Plant.class.getDeclaredField(updateField.getFieldName());
            field.setAccessible(true);

            Object convertedValue = ValueConverter.convert(field.getType(), updateField.getFieldValue());
            String oldValue =  (String) field.get(plant);
            field.set(plant, convertedValue);

            PlantUpdate plantUpdate = new PlantUpdate();
            plantUpdate.setOldValue(oldValue);
            plantUpdate.setNewValue(updateField.getFieldValue());
            plant.getPlantHistory().add(plantUpdate);

            plant = plantRepo.save(plant);

        } catch (NoSuchFieldException | IllegalAccessException | DataIntegrityViolationException |
                 ConstraintViolationException | TransactionSystemException e) {
            throw new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
        }
        return plant;
    }

    private boolean checkIfPlantExist(long id) {
        return plantRepo.existsById(id);
    }
}
