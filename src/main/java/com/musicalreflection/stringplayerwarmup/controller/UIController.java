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

import com.musicalreflection.stringplayerwarmup.repository.PhraseRepository;
import com.musicalreflection.stringplayerwarmup.model.Phrase;

@RestController
@RequestMapping("/api/ui")
@CrossOrigin(origins = "*")
public class UIController {

    private PhraseRepository phraseRepository;

    public UIController(PhraseRepository phraseRepository) {
        this.phraseRepository = phraseRepository;
    }

    @GetMapping
    public Map<String, String> getUITexts(@RequestParam(value = "lang", defaultValue = "en") String lang) {
        // load the right language properties
        Locale locale = new Locale(lang);
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        // put properties in a map
        Map<String, String> texts = new HashMap<>();
        for (String key : bundle.keySet()) {
            texts.put(key, bundle.getString(key));
        }
        return texts;
    }

    // get a random phrase from the db
    @GetMapping("/phrase/random")
    public Map<String, String> getRandomPhrase(@RequestParam(value = "lang", defaultValue = "pt") String lang) {
        Map<String, String> response = new HashMap<>();
        
        // fetch optional phrase
        java.util.Optional<Phrase> optionalPhrase = phraseRepository.findRandomPhrase();

        String chosenText = "";
        if (optionalPhrase.isPresent()) {
            Phrase randomPhraseEntity = optionalPhrase.get();
            
            switch (lang.toLowerCase()) {
                case "pt":
                    chosenText = randomPhraseEntity.getTextPt();
                    break;
                case "es":
                    chosenText = randomPhraseEntity.getTextEs();
                    break;
                case "en":
                default:
                    chosenText = randomPhraseEntity.getTextEn();
                    break;
            }
        } else {
            chosenText = "No sentence found in the database!";
        }

        response.put("phrase", chosenText);
        return response;
    }
}