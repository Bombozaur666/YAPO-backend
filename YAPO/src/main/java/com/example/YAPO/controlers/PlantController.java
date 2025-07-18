package com.example.YAPO.controlers;

import com.example.YAPO.models.Plant;
import com.example.YAPO.models.Room;
import com.example.YAPO.models.User;
import com.example.YAPO.repositories.PlantRepo;
import com.example.YAPO.repositories.RoomRepo;
import com.example.YAPO.repositories.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/plants")
public class PlantController {
    private final PlantRepo plantRepo;
    private final RoomRepo roomRepo;
    private final UserRepo userRepo;

    public PlantController(PlantRepo plantRepo, RoomRepo roomRepo, UserRepo userRepo) {
        this.plantRepo = plantRepo;
        this.roomRepo = roomRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/")
    public List<Plant> plantsPage() {
        return plantRepo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Plant> getPlantByIdPage(@PathVariable int id) {
        return plantRepo.findById(id);
    }

    @PutMapping("/{id}")
    public Optional<Plant> updatePlantByIdPage(@PathVariable int id) {
        return plantRepo.findById(id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlantByIdPage(@PathVariable int id) {
        plantRepo.deleteById(id);
        return (plantRepo.existsById(id)) ? ResponseEntity.notFound().build() : ResponseEntity.ok().build();
    }

    @PostMapping("/create-plant")
    public Plant createPlant(HttpServletRequest request, @RequestBody Plant plant) {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.findByUsername(principal.getName());
        plant.setUser(user);

        Room room = roomRepo.findById(plant.getRoom().getId()).get();
        plant.setRoom(room);
        plantRepo.save(plant);

        return plant;
    }
    @PutMapping("/{plantId}/{roomId}")
    public Plant updatePlant(@PathVariable int plantId,@PathVariable int roomId){
        Plant plant = plantRepo.findById(plantId).get();
        Room room = roomRepo.findById(roomId).get();
        plant.setRoom(room);
        plantRepo.save(plant);
        return plant;
    }
}
