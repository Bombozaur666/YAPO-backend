package com.example.YAPO.service;

import com.example.YAPO.models.UpdateField;
import com.example.YAPO.models.User;
import com.example.YAPO.models.plant.Note;
import com.example.YAPO.models.plant.Plant;
import com.example.YAPO.repositories.NoteRepo;
import com.example.YAPO.repositories.PlantRepo;
import com.example.YAPO.utility.ValueConverter;
import jakarta.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

@Service
public class NoteService {
    private final NoteRepo noteRepo;
    private final PlantRepo plantRepo;

    public NoteService(NoteRepo noteRepo, PlantRepo plantRepo) {
        this.noteRepo = noteRepo;
        this.plantRepo = plantRepo;
    }

    @Transactional
    public ResponseEntity<Object> deleteNoteById(Long plantId, Long noteId, User user) {
        noteRepo.deleteByIdAndPlant_idAndPlant_User_Id(plantId, noteId, user.getId());
        return !checkIfNoteExist(noteId) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    private boolean checkIfNoteExist(long id) {
        return noteRepo.existsById(id);
    }

    public ResponseEntity<Object> updateNote(Long plantId, Long noteId, User user, @Valid UpdateField updateField) {
        List<String> allowedFields = List.of("name");
        if (!allowedFields.contains(updateField.getFieldName())) {
            return ResponseEntity.badRequest().body("Wrong Field");
        }
        Note _note = noteRepo.findByPlant_IdAndPlantUser_IdAndId(plantId, user.getId(), noteId);

        try {
            Field field = Note.class.getDeclaredField(updateField.getFieldName());
            field.setAccessible(true);

            Object convertedValue = ValueConverter.convert(field.getType(), updateField.getFieldValue());
            field.set(_note, convertedValue);

            _note.setEditDate(new Date());

            return ResponseEntity.ok(noteRepo.save(_note));
        } catch (NoSuchFieldException | IllegalAccessException | DataIntegrityViolationException |
                 ConstraintViolationException | TransactionSystemException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> createNote(Long id, @Valid Note note, User user) {
        Plant _plant =  plantRepo.findByIdAndUser_Username(id, user.getUsername());
        if (_plant != null) {
            note.setPlant(_plant);
            try {
                return ResponseEntity.ok(noteRepo.save(note));
            } catch (DataIntegrityViolationException |
                     ConstraintViolationException | TransactionSystemException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }  else {
            return ResponseEntity.badRequest().body("Wrong User");
        }
    }
}
