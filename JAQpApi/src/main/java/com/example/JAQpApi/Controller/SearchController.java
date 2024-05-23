package com.example.JAQpApi.Controller;

import com.example.JAQpApi.DTO.OwnedQuizListResponse;
import com.example.JAQpApi.Service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import com.example.JAQpApi.DTO.SearchRequest;
import com.example.JAQpApi.Service.SearchService;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchController
{
    private final SearchService searchService;

    @GetMapping("/find")
    public OwnedQuizListResponse FindQuiz(@RequestParam @Nullable String text, @RequestParam @Nullable String tags, @RequestParam Integer page)
    {
        return searchService.FindQuiz(text, tags, page);
    }
}
