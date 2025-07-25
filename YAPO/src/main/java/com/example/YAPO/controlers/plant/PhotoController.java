package com.example.YAPO.controlers.plant;

import com.example.YAPO.models.User.MyUserDetails;
import com.example.YAPO.models.plant.PhotoGallery;
import com.example.YAPO.service.plant.PhotoService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/plants/{id}/photo")
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping("")
    public PhotoGallery addPhoto(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id, @RequestBody @Valid PhotoGallery photoGallery) {
        return photoService.createPhoto(id, userDetails.getUser(), photoGallery);
    }

    @GetMapping("")
    public List<PhotoGallery> getAllPhotos(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long id) {
        return photoService.getPhotos(id, userDetails.getUser());
    }

    @PatchMapping("/{photoId}")
    public PhotoGallery updatePhoto(@AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long photoId, @RequestBody PhotoGallery photoGallery) {
        return photoService.updatePhoto(userDetails.getUser(), photoId, photoGallery);
    }

}
