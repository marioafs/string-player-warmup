package com.musicalreflection.stringplayerwarmup.dto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor 
@Getter
public class WarmupSessionDTO {
    private List<CategoryRoutineDTO> routine;
    private String globalMotivationalPhrase;
}