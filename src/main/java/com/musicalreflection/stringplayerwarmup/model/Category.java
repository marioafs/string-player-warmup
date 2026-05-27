package com.musicalreflection.stringplayerwarmup.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer presentationOrder;

    private String nameEn;
    private String namePt;
    private String nameEs;

    public Category() {
    }

    public Category(Integer presentationOrder, String nameEn, String namePt, String nameEs) {
        this.presentationOrder = presentationOrder;
        this.nameEn = nameEn;
        this.namePt = namePt;
        this.nameEs = nameEs;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPresentationOrder() {
        return presentationOrder;
    }

    public void setPresentationOrder(Integer presentationOrder) {
        this.presentationOrder = presentationOrder;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNamePt() {
        return namePt;
    }

    public void setNamePt(String namePt) {
        this.namePt = namePt;
    }

    public String getNameEs() {
        return nameEs;
    }

    public void setNameEs(String nameEs) {
        this.nameEs = nameEs;
    }
}