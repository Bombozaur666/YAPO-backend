package com.example.YAPO.repositories;


import com.example.YAPO.models.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepo extends JpaRepository<Plant, Long> {
    List<Plant> findByUser_Username(String username);
    Plant findByIdAndUser_Username(Long id, String userUsername);

    void deletePlantByIdAndUser_Username(Long id, String userUsername);
}