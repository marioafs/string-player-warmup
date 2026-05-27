package com.musicalreflection.stringplayerwarmup.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/api/ui")
@CrossOrigin(origins = "*")
public class UIController {

    @GetMapping
    public Map<String, String> getUITexts(@RequestParam(value = "lang", defaultValue = "en") String lang) {
        // Loads the correct messages_XX.properties based on the requested language
        Locale locale = new Locale(lang);
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        // Converts the properties into a JSON-friendly Map
        Map<String, String> texts = new HashMap<>();
        for (String key : bundle.keySet()) {
            texts.put(key, bundle.getString(key));
        }
        return texts;
    }
}