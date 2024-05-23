package com.example.JAQpApi.DTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class QuizChangeRequest
{
    private String name;
    private String description;
    private List<String> tags;
}
