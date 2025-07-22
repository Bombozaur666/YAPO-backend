package com.example.YAPO.service;

import com.example.YAPO.models.plant.Localization;
import com.example.YAPO.models.User;
import com.example.YAPO.repositories.LocalizationRepo;
import com.example.YAPO.repositories.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalizationService {
    private final LocalizationRepo  localizationRepo;
    private final UserRepo userRepo;

    public LocalizationService(LocalizationRepo localizationRepo, UserRepo userRepo) {
        this.localizationRepo = localizationRepo;
        this.userRepo = userRepo;
    }

    public List<Localization> getAllLocalizationsByUsername(String username) {
        return localizationRepo.findByUser_Username(username);
    }

    public Localization createLocalization(Long userID, Localization localization) {
        User user = userRepo.findById(userID)
                .orElseThrow(() -> new RuntimeException("USer Not Found"));

        localization.setUser(user);
        return localizationRepo.save(localization);
    }

    public Localization getLocalizationByIdAndUsername(String username, long id) {
        return localizationRepo.findByIdAndUser_Username(id, username);
    }

    public Localization updateLocalizations(String username, Localization localization) {
        Localization _localization = localizationRepo.findByIdAndUser_Username(localization.getId(), username);
        _localization.setName(localization.getName());
        return localizationRepo.save(_localization);
    }

    public boolean deleteByIdAndUsername(long id, String username) {
        localizationRepo.deleteByIdAndUser_Username(id, username);
        return checkIfLocalizationExist(id);
    }

    private boolean checkIfLocalizationExist(long id) {
        return localizationRepo.existsById(id);
    }
}
