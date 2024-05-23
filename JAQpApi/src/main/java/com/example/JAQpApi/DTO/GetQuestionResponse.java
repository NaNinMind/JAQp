package com.example.JAQpApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GetQuestionResponse implements Serializable
{
    private Integer id;
    private String description;
    private String image;
    private List<Integer> answers;
}
