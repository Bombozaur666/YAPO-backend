package com.example.YAPO.controlers.plant;

import com.example.YAPO.models.MyUserDetails;
import com.example.YAPO.models.plant.PhotoGallery;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/plants/{id}/photo")
public class PhotoController {
    @PostMapping("")
    public void addPhoto(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @RequestBody @Valid PhotoGallery photoGallery) {}

    @GetMapping("")
    public void getAllPhotos(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @RequestBody @Valid PhotoGallery photoGallery) {}

    @PatchMapping("/{photo_id}")
    public void updatePhoto(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @PathVariable Long photo_id, @RequestBody @Valid PhotoGallery photoGallery) {}

}
