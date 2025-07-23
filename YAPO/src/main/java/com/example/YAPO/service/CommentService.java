package com.example.YAPO.service;

import com.example.YAPO.models.UpdateField;
import com.example.YAPO.models.User;
import com.example.YAPO.models.plant.Comment;
import com.example.YAPO.models.plant.Plant;
import com.example.YAPO.repositories.CommentRepo;
import com.example.YAPO.repositories.PlantRepo;
import com.example.YAPO.repositories.UserRepo;
import com.example.YAPO.utility.ValueConverter;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Service
public class CommentService {
    private final CommentRepo commentRepo;
    private final PlantRepo plantRepo;
    private final UserRepo userRepo;

    public CommentService(CommentRepo commentRepo, PlantRepo plantRepo, UserRepo userRepo) {
        this.commentRepo = commentRepo;
        this.plantRepo = plantRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public ResponseEntity<?> createComment(Long plantId, User user, Comment comment) {
        User _user = userRepo.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        comment.setUser(_user);

        Plant _plant = plantRepo.findById(plantId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        comment.setPlant(_plant);

        try {
            return ResponseEntity.ok(commentRepo.save(comment));
        } catch (DataIntegrityViolationException |
                 ConstraintViolationException | TransactionSystemException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteComment(Long plantId, Comment comment, User user) {
        if (Objects.equals(comment.getUser().getId(), user.getId()) && Objects.equals(comment.getPlant().getId(), plantId)) {
            try {
                comment.setVisible(false);
                commentRepo.save(comment);
                return checkIfCommentIsVisible(comment.getId()) ? ResponseEntity.badRequest().build() :  ResponseEntity.ok().build();
            } catch (DataIntegrityViolationException |
                     ConstraintViolationException | TransactionSystemException e ) {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    private boolean checkIfCommentIsVisible(long id) {
        return commentRepo.findById(id).orElseThrow(() -> new RuntimeException("Comment not found")).isVisible();
    }

    public ResponseEntity<Object> updateComment(Long id, UpdateField updateField, User user) {
        List<String> allowedFields = List.of("name", "visible");
        if (!allowedFields.contains(updateField.getFieldName())) {
            return ResponseEntity.badRequest().body("Wrong Field");
        }
        Comment _comment = commentRepo.findByIdAndUser_Username(id, user.getUsername());

        try {
            Field field = Comment.class.getDeclaredField(updateField.getFieldName());
            field.setAccessible(true);

            Object convertedValue = ValueConverter.convert(field.getType(), updateField.getFieldValue());
            field.set(_comment, convertedValue);

            return ResponseEntity.ok(commentRepo.save(_comment));
        } catch (NoSuchFieldException | IllegalAccessException | DataIntegrityViolationException |
                 ConstraintViolationException | TransactionSystemException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
