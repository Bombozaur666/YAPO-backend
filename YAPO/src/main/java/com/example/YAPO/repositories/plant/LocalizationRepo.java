package com.example.YAPO.repositories.plant;

import com.example.YAPO.models.plant.Localization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalizationRepo extends JpaRepository<Localization, Long> {
    List<Localization> findByUser_Username(String username);

    Localization findByIdAndUser_Username(Long Id,String userUsername);

    void deleteByIdAndUser_Username(long id, String username);
}
