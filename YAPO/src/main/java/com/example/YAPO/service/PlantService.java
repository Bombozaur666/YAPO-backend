package com.example.YAPO.service;

import com.example.YAPO.models.*;
import com.example.YAPO.repositories.LocalizationRepo;
import com.example.YAPO.repositories.PlantRepo;
import com.example.YAPO.utility.ValueConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class PlantService {
    private final PlantRepo plantRepo;
    private final LocalizationRepo localizationRepo;

    public PlantService(PlantRepo plantRepo, LocalizationRepo localizationRepo) {
        this.plantRepo = plantRepo;
        this.localizationRepo = localizationRepo;
    }

    public List<Plant> getAllPlants(String username) {
        return plantRepo.findByUser_Username(username);
    }

    public Plant createPlant(Plant plant, User user) {
        Localization localization = localizationRepo.findById(plant.getLocalization().getId()).get();
        plant.setLocalization(localization);

        plant.setUser(user);
        return plantRepo.save(plant);
    }

    public Plant getPlant(Long plant_id, String username) {
        return plantRepo.findByIdAndUser_Username(plant_id, username);
    }

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

            plantRepo.save(plant);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
