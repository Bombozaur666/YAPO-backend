package com.example.YAPO.controlers.plant;

import com.example.YAPO.models.plant.Note;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/plants/{id}/note")
public class NoteController {
    @PostMapping("")
    public void createNote(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, Note note) {}

    @DeleteMapping("")
    public void deleteNote(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, Note note) {}

    @PatchMapping("")
    public void updateNote(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, Note note) {}
}
