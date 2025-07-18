package com.example.YAPO.controlers;

import com.example.YAPO.models.Plant;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
public class PlantController {
    @GetMapping("/plants")
    public List<Plant> plantsPage() {
        return new ArrayList<>();
    }

    @GetMapping("/plants/{id}")
    public Plant plantByIdPage(@PathVariable String id) {
        return new Plant();
    }

    @PostMapping("create-plant")
    public Plant plantByIdPage(@RequestBody Plant plant) {
        return plant;
    }
}
