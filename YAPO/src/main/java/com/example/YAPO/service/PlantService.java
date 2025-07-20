package com.example.YAPO.service;

import com.example.YAPO.models.Plant;
import com.example.YAPO.models.Localization;
import com.example.YAPO.models.User;
import com.example.YAPO.repositories.PlantRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantService {
    private final PlantRepo plantRepo;

    public PlantService(PlantRepo plantRepo) {
        this.plantRepo = plantRepo;
    }

    public Plant createPlant(Plant plant, User user, Localization localization) {
        plant.setLocalization(localization);
        plant.setUser(user);
        return plantRepo.save(plant);
    }

    public Plant getPlant(Long plant_id, String username) {
        return plantRepo.findByIdAndUser_Username(plant_id, username);
    }


    public List<Plant> getAllPlants(String username) {
        return plantRepo.findByUser_Username(username);
    }

    public Plant updatePlant(Plant plant, String username) {
        Plant plantInDB = plantRepo.findByIdAndUser_Username(plant.getId(), username);
        plantInDB.setName(plant.getName());
        plantInDB.setLocalization(plant.getLocalization());
        return plantRepo.save(plant);
    }

    public void deletePlant(Long plantId, String username) {
        plantRepo.deletePlantByIdAndUser_Username(plantId, username);
    }
}
