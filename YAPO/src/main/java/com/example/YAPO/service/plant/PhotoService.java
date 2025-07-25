package com.example.YAPO.service.plant;

import com.example.YAPO.models.User.User;
import com.example.YAPO.models.enums.ErrorList;
import com.example.YAPO.models.plant.PhotoGallery;
import com.example.YAPO.models.plant.Plant;
import com.example.YAPO.repositories.plant.PhotoRepo;
import com.example.YAPO.repositories.plant.PlantRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;

@Service
public class PhotoService {
    private final PlantRepo plantRepo;
    private final PhotoRepo photoRepo;

    public PhotoService(PlantRepo plantRepo, PhotoRepo photoRepo) {
        this.plantRepo = plantRepo;
        this.photoRepo = photoRepo;
    }

    @Transactional
    public PhotoGallery createPhoto(Long id, User user, @Valid PhotoGallery photoGallery) {
        Plant _plant = plantRepo.findByIdAndUser_Username(id,user.getUsername());
        if (_plant != null) {
            photoGallery.setPlant(_plant);
            try {
                photoGallery = photoRepo.save(photoGallery);
            } catch (DataIntegrityViolationException |
                     ConstraintViolationException | TransactionSystemException e) {
                throw new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
            }
        } else {
            throw new RuntimeException(ErrorList.PLANT_NOT_FOUND.toString());
        }
        return photoGallery;
    }

    @Transactional
    public PhotoGallery updatePhoto(User user, Long photoId, PhotoGallery photoGallery) {
        PhotoGallery _photo = photoRepo.findByIdAndPlant_User_Id(photoId, user.getId());
        if (_photo != null) {
            _photo.setDescription(photoGallery.getDescription());
            _photo.setTitle(photoGallery.getTitle());
            try {
                _photo = photoRepo.save(_photo);
            } catch (DataIntegrityViolationException |
                     ConstraintViolationException | TransactionSystemException e) {
                throw new RuntimeException(ErrorList.ERROR_DURING_DATABASE_SAVING.toString());
            }
        } else  {
            throw new RuntimeException(ErrorList.PHOTO_NOT_FOUND.toString());
        }
        return _photo;
    }

    public List<PhotoGallery> getPhotos(Long plantId, User user) {
        return photoRepo.findAllByPlant_IdAndPlant_User_Id(plantId, user.getId());
    }
}
