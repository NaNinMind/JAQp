package com.example.JAQpApi.Controller;

import com.example.JAQpApi.DTO.Tag.TagListResponse;
import com.example.JAQpApi.Service.TagService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tag")
@AllArgsConstructor
public class TagController
{
    private TagService tagService;

    @GetMapping("/init")
    public ResponseEntity Init()
    {
        tagService.InitTags();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public TagListResponse GetAll()
    {
        return tagService.GetAllTags();
    }
}
