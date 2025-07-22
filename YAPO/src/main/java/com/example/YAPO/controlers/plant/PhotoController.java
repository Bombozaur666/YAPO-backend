package com.example.YAPO.controlers.plant;

import com.example.YAPO.models.plant.PhotoGallery;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/plants/{id}/photo")
public class PhotoController {
    @PostMapping("")
    public void addPhoto(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody PhotoGallery photoGallery) {}

    @GetMapping("")
    public void getAllPhotos(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody PhotoGallery photoGallery) {}

    @PatchMapping("/{photo_id}")
    public void updatePhoto(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @PathVariable Long photo_id, @RequestBody PhotoGallery photoGallery) {}

}
