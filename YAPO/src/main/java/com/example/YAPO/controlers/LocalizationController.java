package com.example.YAPO.controlers;

import com.example.YAPO.models.Localization;
import com.example.YAPO.models.User;
import com.example.YAPO.service.LocalizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://localhost:4200/")
@RequestMapping("/rooms")
public class LocalizationController {
    private final LocalizationService localizationService;

    public LocalizationController(LocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    @GetMapping("/")
    public List<Localization> roomPage(@AuthenticationPrincipal UserDetails userDetails) {
        return  localizationService.getAllLocalizationsByUsername(userDetails.getUsername());
    }

    @PostMapping("/create-room")
    public Localization createLocalization(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Localization localization) {
        return localizationService.createLocalization((User) userDetails, localization);
    }
    
    @GetMapping("/{id}")
    public Localization getLocalizationById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        return localizationService.getLocalizationByIdAndUsername(userDetails.getUsername(), id);
    }

    @PutMapping("")
    public Localization updateLocalizationById(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Localization localization) {
        return localizationService.updateLocalizations((User) userDetails, localization);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLocalization( @AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        return (localizationService.deleteByIdAndUsername(id, userDetails.getUsername())) ? ResponseEntity.notFound().build() : ResponseEntity.ok().build();
    }
}
