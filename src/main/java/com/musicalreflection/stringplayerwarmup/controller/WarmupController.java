package com.musicalreflection.stringplayerwarmup.controller;

import com.musicalreflection.stringplayerwarmup.dto.WarmupSessionDTO;
import com.musicalreflection.stringplayerwarmup.service.WarmupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/warmup")
@CrossOrigin(origins = "*")
public class WarmupController {

    private final WarmupService warmupService;

    public WarmupController(WarmupService warmupService) {
        this.warmupService = warmupService;
    }

    @GetMapping
    public ResponseEntity<WarmupSessionDTO> getDailySession(
            @RequestParam(value = "lang", required = false, defaultValue = "en") String lang) {
        
        // Generates the customized session based on the requested language
        WarmupSessionDTO session = warmupService.generateDailySession(lang);
        
        return ResponseEntity.ok(session);
    }
}