package com.example.JAQpApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
public class UserResultsData
{

    private Float result;
    private Timestamp createdAt;
    private QuizData quizData;

}
