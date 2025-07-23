package com.example.YAPO.service;

import com.example.YAPO.models.User;
import com.example.YAPO.models.plant.PhotoGallery;
import com.example.YAPO.models.plant.Plant;
import com.example.YAPO.repositories.PhotoRepo;
import com.example.YAPO.repositories.PlantRepo;
import jakarta.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

@Service
public class PhotoService {
    private final PlantRepo plantRepo;
    private final PhotoRepo photoRepo;

    public PhotoService(PlantRepo plantRepo, PhotoRepo photoRepo) {
        this.plantRepo = plantRepo;
        this.photoRepo = photoRepo;
    }

    public ResponseEntity<?> createPhoto(Long id, User user, @Valid PhotoGallery photoGallery) {
        Plant _plant = plantRepo.findByIdAndUser_Username(id,user.getUsername());
        if (_plant != null) {
            photoGallery.setPlant(_plant);
            photoRepo.save(photoGallery);
            return ResponseEntity.ok(photoGallery);
        } else {
            return ResponseEntity.badRequest().body("Wrong request");
        }
    }

    public ResponseEntity<Object> updatePhoto(User user, Long photoId, PhotoGallery photoGallery) {
        PhotoGallery _photo = photoRepo.findByIdAndPlant_User_Id(photoId, user.getId());
        if (_photo != null) {
            _photo.setDescription(photoGallery.getDescription());
            _photo.setTitle(photoGallery.getTitle());
            try {
                photoRepo.save(_photo);
            } catch (DataIntegrityViolationException |
                     ConstraintViolationException | TransactionSystemException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok(_photo);
        } else  {
            return ResponseEntity.badRequest().body("Wrong request");
        }
    }

    public ResponseEntity<Object> getPhotos(Long plantId, User user) {
        return ResponseEntity.ok(photoRepo.findAllByPlant_IdAndPlant_User_Id(plantId, user.getId()));
    }
}
