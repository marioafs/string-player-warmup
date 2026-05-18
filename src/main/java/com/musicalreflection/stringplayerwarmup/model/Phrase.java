package com.musicalreflection.stringplayerwarmup.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "phrase")
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String textEn;
    private String textPt;
    private String textEs;

    public Phrase() {
    }

    public Phrase(String textEn, String textPt, String textEs) {
        this.textEn = textEn;
        this.textPt = textPt;
        this.textEs = textEs;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTextEn() { return textEn; }
    public void setTextEn(String textEn) { this.textEn = textEn; }
    public String getTextPt() { return textPt; }
    public void setTextPt(String textPt) { this.textPt = textPt; }
    public String getTextEs() { return textEs; }
    public void setTextEs(String textEs) { this.textEs = textEs; }
}