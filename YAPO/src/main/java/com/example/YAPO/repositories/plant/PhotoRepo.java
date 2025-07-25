package com.example.YAPO.repositories.plant;

import com.example.YAPO.models.plant.PhotoGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepo extends JpaRepository<PhotoGallery, Long> {
    PhotoGallery findByIdAndPlant_User_Id(Long id, Long plantUserId);

    List<PhotoGallery> findAllByPlant_IdAndPlant_User_Id(Long plantId, Long plantUserId);
}
