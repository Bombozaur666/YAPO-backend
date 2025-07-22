package com.example.YAPO.controlers.plant;

import com.example.YAPO.models.plant.Comment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/plants/{id}/comment")
public class CommentController {
    @PostMapping("")
    public void createComment (@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody Comment comment) {}

    @DeleteMapping("")
    public void deleteComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody Comment comment) {}

    @PatchMapping("")
    public void updateComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody Comment comment) {}
}
