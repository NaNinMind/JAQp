package com.example.JAQpApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ChangeAnswerRequest
{
    private String content;
    private Boolean is_right;
}
