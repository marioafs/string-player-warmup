package com.musicalreflection.stringplayerwarmup.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter 
public class CategoryRoutineDTO {
    private String categoryName;
    private Integer order;
    private String youtubeId;
    private String videoTitle;
    private String videoObjective;
}
