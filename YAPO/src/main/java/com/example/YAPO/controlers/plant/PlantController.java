package com.example.YAPO.controlers.plant;

import com.example.YAPO.models.*;
import com.example.YAPO.models.User.MyUserDetails;
import com.example.YAPO.models.plant.Plant;
import com.example.YAPO.service.plant.PlantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/plants")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping("/")
    public List<Plant> plantsPage(@AuthenticationPrincipal MyUserDetails userDetails) {
        return plantService.getAllPlants(userDetails.getUsername());
    }

    @PostMapping("/create-plant")
    public Plant createPlant(@AuthenticationPrincipal MyUserDetails userDetails, @RequestBody @Valid Plant plant) throws NoSuchElementException{
        return plantService.createPlant(plant, userDetails.getUser());
    }

    @GetMapping("/{id}")
    public Plant getPlantPage(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable long id) {
        return plantService.getPlant(id, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlantByIdPage(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id) {
        return !plantService.deletePlant(id, userDetails.getUsername()) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/update")
    public  Plant updateField(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @RequestBody @Valid UpdateField updateField) {
        return plantService.updateField(id, userDetails.getUser(), updateField);
    }
}
