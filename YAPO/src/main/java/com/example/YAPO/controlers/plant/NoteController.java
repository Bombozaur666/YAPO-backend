package com.example.YAPO.controlers.plant;

import com.example.YAPO.models.User.MyUserDetails;
import com.example.YAPO.models.UpdateField;
import com.example.YAPO.models.plant.Note;
import com.example.YAPO.service.plant.NoteService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/plants/{id}/note")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("")
    public Note createNote(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @RequestBody @Valid Note note) {
        return noteService.createNote(id, note, userDetails.getUser());
    }

    @DeleteMapping("/{idNote}")
    public boolean deleteNote(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @PathVariable Long idNote) {
        return noteService.deleteNoteById(id, idNote, userDetails.getUser());
    }

    @PatchMapping("/{idNote}")
    public Note updateNote(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @PathVariable Long idNote, @RequestBody @Valid UpdateField updateField) {
        return noteService.updateNote(id, idNote, userDetails.getUser(), updateField);
    }
}
