package com.example.YAPO.service;

import com.example.YAPO.models.User;
import com.example.YAPO.models.plant.Comment;
import com.example.YAPO.models.plant.Plant;
import com.example.YAPO.repositories.CommentRepo;
import com.example.YAPO.repositories.PlantRepo;
import com.example.YAPO.repositories.UserRepo;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

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
                commentRepo.delete(comment);
                return checkIfCommentExist(comment.getId()) ? ResponseEntity.badRequest().build() :  ResponseEntity.ok().build();
            } catch (EmptyResultDataAccessException | DataIntegrityViolationException | TransactionSystemException e ) {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    private boolean checkIfCommentExist(long id) {
        return commentRepo.existsById(id);
    }
}
