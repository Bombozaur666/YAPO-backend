package com.example.YAPO.repositories;


import com.example.YAPO.models.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepo extends JpaRepository<Plant, Integer> {
    Plant findByName(String username);
}