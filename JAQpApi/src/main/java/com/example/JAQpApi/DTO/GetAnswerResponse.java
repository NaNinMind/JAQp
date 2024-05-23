package com.example.JAQpApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class GetAnswerResponse implements Serializable
{
    private boolean isRight;
    private String content;
    private String image;
    private int id;
}
