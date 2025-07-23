package com.example.YAPO.controlers.plant;

import com.example.YAPO.models.MyUserDetails;
import com.example.YAPO.models.plant.PhotoGallery;
import com.example.YAPO.service.PhotoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/plants/{id}/photo")
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping("")
    public ResponseEntity<?> addPhoto(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @RequestBody @Valid PhotoGallery photoGallery) {
        return photoService.createPhoto(id, userDetails.getUser(), photoGallery);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPhotos(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id) {
        return photoService.getPhotos(id, userDetails.getUser());
    }

    @PatchMapping("/{photoId}")
    public ResponseEntity<?> updatePhoto(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long photoId, @RequestBody PhotoGallery photoGallery) {
        return photoService.updatePhoto(userDetails.getUser(), photoId, photoGallery);
    }

}
