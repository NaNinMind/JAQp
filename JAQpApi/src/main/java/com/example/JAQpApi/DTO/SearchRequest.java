package com.example.JAQpApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class SearchRequest
{
    private String text;
    private String tags;
    private Integer page;
}
