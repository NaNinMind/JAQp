package com.example.JAQpApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizCreateRequest
{
    @Schema( example = "my quiz 123")
    private String name;
    private MultipartFile thumbnail;
    @Schema ( example = "quiz about smth")
    private String description;

    private List<String> tags;
}
