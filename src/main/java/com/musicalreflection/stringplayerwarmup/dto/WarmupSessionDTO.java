package com.musicalreflection.stringplayerwarmup.dto;

import java.util.List;

public class WarmupSessionDTO {
    private List<CategoryRoutineDTO> routine;
    private String globalMotivationalPhrase;

    public WarmupSessionDTO(List<CategoryRoutineDTO> routine, String globalMotivationalPhrase) {
        this.routine = routine;
        this.globalMotivationalPhrase = globalMotivationalPhrase;
    }

    // Getters
    public List<CategoryRoutineDTO> getRoutine() { return routine; }
    public String getGlobalMotivationalPhrase() { return globalMotivationalPhrase; }

    // Inner DTO for clean structure
    public static class CategoryRoutineDTO {
        private String categoryName;
        private Integer order;
        private String youtubeId;
        private String videoTitle;
        private String videoObjective;

        public CategoryRoutineDTO(String categoryName, Integer order, String youtubeId, String videoTitle, String videoObjective) {
            this.categoryName = categoryName;
            this.order = order;
            this.youtubeId = youtubeId;
            this.videoTitle = videoTitle;
            this.videoObjective = videoObjective;
        }

        // Getters
        public String getCategoryName() {
            return categoryName;
        }

        public Integer getOrder() {
            return order;
        }

        public String getYoutubeId() {
            return youtubeId;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public String getVideoObjective() {
            return videoObjective;
        }
    }
}