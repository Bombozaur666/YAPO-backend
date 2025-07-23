package com.example.YAPO.service;

import com.example.YAPO.models.*;
import com.example.YAPO.models.plant.Localization;
import com.example.YAPO.models.plant.Plant;
import com.example.YAPO.models.plant.PlantUpdate;
import com.example.YAPO.repositories.LocalizationRepo;
import com.example.YAPO.repositories.PlantRepo;
import com.example.YAPO.repositories.UserRepo;
import com.example.YAPO.utility.ValueConverter;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<Object> createPlant(Plant plant, User user) {
        Localization localization = localizationRepo.findById(plant.getLocalization().getId()).get();
        User _user = userRepo.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        plant.setLocalization(localization);

        plant.setUser(_user);

        try {
            return ResponseEntity.ok( plantRepo.save(plant));
        } catch (DataIntegrityViolationException |
                 ConstraintViolationException | TransactionSystemException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Plant getPlant(Long plant_id, String username) {
        return plantRepo.findByIdAndUser_Username(plant_id, username);
    }

    @Transactional
    public void deletePlant(Long plantId, String username) {
        plantRepo.deletePlantByIdAndUser_Username(plantId, username);
    }

    public ResponseEntity<Object> updateField(Long id, User userDetails, UpdateField updateField) {
        List<String> allowedFields = List.of("name", "species", "purchaseDate", "purchaseLocalization", "fertilizationDate", "alive", "deathReason", "wateringDate", "plantCondition", "plantSoil", "plantWatering", "plantBerth", "plantToxicity", "plantLifeExpectancy");

        if (!allowedFields.contains(updateField.getFieldName())) {
            return ResponseEntity.badRequest().body("Wrong Field");
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

            return ResponseEntity.ok(plantRepo.save(plant));
        } catch (NoSuchFieldException | IllegalAccessException | DataIntegrityViolationException |
                 ConstraintViolationException | TransactionSystemException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
