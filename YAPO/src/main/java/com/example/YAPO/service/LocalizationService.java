package com.example.YAPO.service;

import com.example.YAPO.models.Localization;
import com.example.YAPO.models.User;
import com.example.YAPO.repositories.LocalizationRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalizationService {
    private final LocalizationRepo  localizationRepo;

    public LocalizationService(LocalizationRepo localizationRepo) {
        this.localizationRepo = localizationRepo;
    }

    public List<Localization> getAllLocalizationsByUsername(String username) {
        return localizationRepo.findByUser_Username(username);
    }

    public Localization createLocalization(User userDetails, Localization localization) {
        localization.setUser(userDetails);
        return localizationRepo.save(localization);
    }

    public Localization getLocalizationByIdAndUsername(String username, long id) {
        return localizationRepo.findByIdAndUser_Username(id, username);
    }

    public Localization updateLocalizations(User userDetails, Localization localization) {
        Localization _localization = localizationRepo.findByIdAndUser_Username(localization.getId(), userDetails.getUsername());
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
