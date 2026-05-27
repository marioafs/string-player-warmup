package com.musicalreflection.stringplayerwarmup.service;

import com.musicalreflection.stringplayerwarmup.dto.WarmupSessionDTO;
import com.musicalreflection.stringplayerwarmup.dto.WarmupSessionDTO.CategoryRoutineDTO;
import com.musicalreflection.stringplayerwarmup.model.Category;
import com.musicalreflection.stringplayerwarmup.model.Phrase;
import com.musicalreflection.stringplayerwarmup.model.Video;
import com.musicalreflection.stringplayerwarmup.repository.CategoryRepository;
import com.musicalreflection.stringplayerwarmup.repository.PhraseRepository;
import com.musicalreflection.stringplayerwarmup.repository.VideoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WarmupService {

    private final CategoryRepository categoryRepository;
    private final VideoRepository videoRepository;
    private final PhraseRepository phraseRepository;

    // Constructor Injection
    public WarmupService(CategoryRepository categoryRepository, 
                         VideoRepository videoRepository, 
                         PhraseRepository phraseRepository) {
        this.categoryRepository = categoryRepository;
        this.videoRepository = videoRepository;
        this.phraseRepository = phraseRepository;
    }

    public WarmupSessionDTO generateDailySession(String lang) {
        // Get all categories sorted by presentation order
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "presentationOrder"));
        List<CategoryRoutineDTO> routineList = new ArrayList<>();

        // For each category, fetch a random video
        for (Category cat : categories) {
            videoRepository.findRandomVideoByCategoryId(cat.getId()).ifPresent(video -> {
                routineList.add(new CategoryRoutineDTO(
                        getCategoryName(cat, lang), 
                        cat.getPresentationOrder(),
                        video.getYoutubeId(),
                        getVideoTitle(video, lang),       
                        getVideoObjective(video, lang) 
                ));
            });
        }

        // Fetch a random motivational phrase and select the correct language translation
        String motivationalText = phraseRepository.findRandomPhrase()
                .map(phrase -> getTranslation(phrase, lang))
                .orElse("Keep focus and breathe."); // Fallback string

        return new WarmupSessionDTO(routineList, motivationalText);
    }

    private String getTranslation(Phrase phrase, String lang) {
        if (lang == null) return phrase.getTextEn();
        return switch (lang.toLowerCase()) {
            case "pt" -> phrase.getTextPt();
            case "es" -> phrase.getTextEs();
            default -> phrase.getTextEn();
        };
    }

    private String getCategoryName(Category cat, String lang) {
        if (lang == null) return cat.getNameEn();
        return switch (lang.toLowerCase()) {
            case "pt" -> cat.getNamePt();
            case "es" -> cat.getNameEs();
            default -> cat.getNameEn();
        };
    }

    private String getVideoTitle(Video video, String lang) {
        if (lang == null) return video.getTitleEn();
        return switch (lang.toLowerCase()) {
            case "pt" -> video.getTitlePt();
            case "es" -> video.getTitleEs();
            default -> video.getTitleEn();
        };
    }

    private String getVideoObjective(Video video, String lang) {
        if (lang == null) return video.getObjectiveEn();
        return switch (lang.toLowerCase()) {
            case "pt" -> video.getObjectivePt();
            case "es" -> video.getObjectiveEs();
            default -> video.getObjectiveEn();
        };
    }
}