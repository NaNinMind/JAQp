package com.example.JAQpApi.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizResponse implements Serializable
{
    @Schema( example = "123")
    private Integer id;
    @Schema ( example = "my_image.png")
    private String image_name;
    @Schema( example = "quiz about smth")
    private String description;
    @Schema( example = "my quiz 123")
    private String name;


    private Boolean isPublic;
    private List<String> tags;
}
