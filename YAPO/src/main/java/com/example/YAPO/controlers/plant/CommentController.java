package com.example.YAPO.controlers.plant;

import com.example.YAPO.models.MyUserDetails;
import com.example.YAPO.models.UpdateField;
import com.example.YAPO.models.plant.Comment;
import com.example.YAPO.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/plants/{id}/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    public ResponseEntity<?> createComment (@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @RequestBody @Valid Comment comment) {
        return commentService.createComment(id, userDetails.getUser(), comment);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @RequestBody @Valid Comment comment) {
        return commentService.deleteComment(id, comment, userDetails.getUser());
    }

    @PatchMapping("")
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @RequestBody @Valid UpdateField  updateField) {
        return commentService.updateComment(id, updateField, userDetails.getUser());
    }
}
