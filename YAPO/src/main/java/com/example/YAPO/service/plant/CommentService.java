package com.example.YAPO.service.plant;

import com.example.YAPO.models.UpdateField;
import com.example.YAPO.models.User.User;
import com.example.YAPO.models.enums.ErrorList;
import com.example.YAPO.models.plant.Comment;
import com.example.YAPO.models.plant.Plant;
import com.example.YAPO.repositories.plant.CommentRepo;
import com.example.YAPO.repositories.plant.PlantRepo;
import com.example.YAPO.repositories.user.UserRepo;
import com.example.YAPO.utility.ValueConverter;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
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
    public Comment createComment(Long plantId, User user, Comment comment) {
        User _user = userRepo.findById(user.getId())
                .orElseThrow(() -> new RuntimeException(ErrorList.USER_NOT_FOUND.toString()));
        comment.setUser(_user);

        Plant _plant = plantRepo.findById(plantId)
                .orElseThrow(() -> new RuntimeException(ErrorList.PLANT_NOT_FOUND.toString()));
        comment.setPlant(_plant);

        try {
            commentRepo.save(comment);
        } catch (DataIntegrityViolationException |
                 ConstraintViolationException | TransactionSystemException e) {
            throw new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
        }
        return comment;
    }

    @Transactional
    public boolean deleteComment(Long plantId, Long commentId, User user) {
        Comment comment = commentRepo.findByIdAndUser_Username(commentId, user.getUsername());
        if (Objects.equals(comment.getUser().getId(), user.getId()) && Objects.equals(comment.getPlant().getId(), plantId)) {
            comment.setVisible(false);
            try {
                commentRepo.save(comment);
            } catch (DataIntegrityViolationException |
                     ConstraintViolationException | TransactionSystemException e ) {
                throw new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
            }
        }
        return !checkIfCommentIsVisible(comment.getId());
    }

    private boolean checkIfCommentIsVisible(long id) {
        return commentRepo.findById(id).orElseThrow(() -> new RuntimeException(ErrorList.COMMENT_NOT_FOUND.toString())).isVisible();
    }

    @Transactional
    public Comment updateComment(Long id, UpdateField updateField, User user) {
        List<String> allowedFields = List.of("name", "comment");
        if (!allowedFields.contains(updateField.getFieldName())) {
            throw new RuntimeException(ErrorList.WRONG_FIELD_TO_UPDATE.toString());
        }
        Comment _comment = commentRepo.findByIdAndUser_Username(id, user.getUsername());

        try {
            Field field = Comment.class.getDeclaredField(updateField.getFieldName());
            field.setAccessible(true);

            Object convertedValue = ValueConverter.convert(field.getType(), updateField.getFieldValue());
            field.set(_comment, convertedValue);

            _comment = commentRepo.save(_comment);
        } catch (NoSuchFieldException | IllegalAccessException | DataIntegrityViolationException |
                 ConstraintViolationException | TransactionSystemException e) {
            throw new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
        }
        return _comment;
    }

    public List<Comment> getComments(Long plantId, User user) {
        return commentRepo.findAllByIdAndUser_IdAndVisible(plantId, user.getId(), true);
    }
}
