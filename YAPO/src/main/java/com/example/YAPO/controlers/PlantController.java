package com.example.YAPO.controlers;

import com.example.YAPO.models.Plant;
import com.example.YAPO.repositories.PlantRepo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
public class PlantController {
    private PlantRepo plantRepo;

    public PlantController(PlantRepo plantRepo) {
        this.plantRepo = plantRepo;
    }

    @GetMapping("/plants")
    public List<Plant> plantsPage() {
        return plantRepo.findAll();
    }

    @GetMapping("/plants/{id}")
    public Optional<Plant> plantByIdPage(@PathVariable String id) {
        return plantRepo.findById(Integer.valueOf(id));
    }

    @PostMapping("create-plant")
    public Plant plantByIdPage(@RequestBody Plant plant) {
        return plantRepo.save(plant);
    }
}
