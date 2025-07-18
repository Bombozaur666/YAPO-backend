package com.example.YAPO.controlers;

import com.example.YAPO.models.Room;
import com.example.YAPO.models.User;
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
@RequestMapping("/rooms")
public class RoomController {
    private final RoomRepo roomRepo;
    private final UserRepo userRepo;

    public RoomController(RoomRepo roomRepo, UserRepo userRepo) {
        this.roomRepo = roomRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/")
    public List<Room> roomPage() {
        return roomRepo.findAll();
    }

    @PostMapping("/create-room")
    public Room createRoom(HttpServletRequest request, @RequestBody Room room) {
        Principal principal = request.getUserPrincipal();
        User user = userRepo.findByUsername(principal.getName());
        room.setUser(user);
        return roomRepo.save(room);
    }
    
    @GetMapping("/{id}")
    public Optional<Room> getRoomById(@PathVariable int id) {
        return roomRepo.findById(id);
    }

    @PutMapping("/{id}")
    public Optional<Room> updateRoomById(@PathVariable int id) {
        return roomRepo.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRoom(@PathVariable int id) {
        roomRepo.deleteById(id);
        return (roomRepo.existsById(id)) ? ResponseEntity.notFound().build() : ResponseEntity.ok().build();
    }
}
