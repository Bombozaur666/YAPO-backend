package com.example.YAPO.controlers;

import com.example.YAPO.models.Plant;
import com.example.YAPO.repositories.PlantRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/plants")
public class PlantController {
    private final PlantRepo plantRepo;

    public PlantController(PlantRepo plantRepo) {
        this.plantRepo = plantRepo;
    }

    @GetMapping("/")
    public List<Plant> plantsPage() {
        return plantRepo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Plant> getPlantByIdPage(@PathVariable String id) {
        return plantRepo.findById(Integer.valueOf(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlantByIdPage(@PathVariable String id) {
        plantRepo.deleteById(Integer.valueOf(id));
        if (plantRepo.existsById(Integer.valueOf(id))) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }


    }

    @PostMapping("create-plant")
    public Plant plantByIdPage(@RequestBody Plant plant) {
        return plantRepo.save(plant);
    }
}
