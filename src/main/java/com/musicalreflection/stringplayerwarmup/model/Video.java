package com.musicalreflection.stringplayerwarmup.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String youtubeId;
    
    // Title
    private String titleEn;
    private String titlePt;
    private String titleEs;
    
    // Objetive
    private String objectiveEn;
    private String objectivePt;
    private String objectiveEs;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Video() {
    }

    public Video(String youtubeId, String titleEn, String titlePt, String titleEs, 
                 String objectiveEn, String objectivePt, String objectiveEs, Category category) {
        this.youtubeId = youtubeId;
        this.titleEn = titleEn;
        this.titlePt = titlePt;
        this.titleEs = titleEs;
        this.objectiveEn = objectiveEn;
        this.objectivePt = objectivePt;
        this.objectiveEs = objectiveEs;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getTitlePt() {
        return titlePt;
    }

    public void setTitlePt(String titlePt) {
        this.titlePt = titlePt;
    }

    public String getTitleEs() {
        return titleEs;
    }

    public void setTitleEs(String titleEs) {
        this.titleEs = titleEs;
    }

    public String getObjectiveEn() {
        return objectiveEn;
    }

    public void setObjectiveEn(String objectiveEn) {
        this.objectiveEn = objectiveEn;
    }

    public String getObjectivePt() {
        return objectivePt;
    }

    public void setObjectivePt(String objectivePt) {
        this.objectivePt = objectivePt;
    }

    public String getObjectiveEs() {
        return objectiveEs;
    }

    public void setObjectiveEs(String objectiveEs) {
        this.objectiveEs = objectiveEs;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}