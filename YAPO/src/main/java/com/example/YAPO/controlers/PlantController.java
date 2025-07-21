package com.example.YAPO.controlers;

import com.example.YAPO.models.Plant;
import com.example.YAPO.models.UpdateField;
import com.example.YAPO.models.User;
import com.example.YAPO.service.PlantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public List<Plant> plantsPage(@AuthenticationPrincipal UserDetails userDetails) {
        return plantService.getAllPlants(userDetails.getUsername());
    }

    @PostMapping("/create-plant")
    public Plant createPlant(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Plant plant) throws NoSuchElementException{
        return plantService.createPlant(plant, (User) userDetails);
    }

    @GetMapping("/{id}")
    public Plant getPlantPage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        return plantService.getPlant(id, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlantByIdPage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        plantService.deletePlant(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/update")
    public  ResponseEntity<Object> updateField(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody UpdateField updateField) {
        return plantService.updateField(id, (User) userDetails, updateField);
    }



}
