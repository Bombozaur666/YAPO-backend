package com.example.YAPO.controlers.plant;

import com.example.YAPO.models.User.MyUserDetails;
import com.example.YAPO.models.UpdateField;
import com.example.YAPO.models.plant.Comment;
import com.example.YAPO.service.plant.CommentService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/plants/{id}/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("")
    public List<Comment> getComments(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id) {
        return commentService.getComments(id, userDetails.getUser());
    }
    @PostMapping("")
    public Comment createComment (@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @RequestBody @Valid Comment comment) {
        return commentService.createComment(id, userDetails.getUser(), comment);
    }

    @DeleteMapping("/{commentId}")
    public Boolean deleteComment(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @PathVariable Long commentId) {
        return commentService.deleteComment(id, commentId, userDetails.getUser());
    }

    @PatchMapping("")
    public Comment updateComment(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @RequestBody @Valid UpdateField  updateField) {
        return commentService.updateComment(id, updateField, userDetails.getUser());
    }
}
