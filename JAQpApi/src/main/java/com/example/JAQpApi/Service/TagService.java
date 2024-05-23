package com.example.JAQpApi.Service;

import com.example.JAQpApi.DTO.Tag.TagData;
import com.example.JAQpApi.DTO.Tag.TagListResponse;
import com.example.JAQpApi.Entity.Quiz.Tag;
import com.example.JAQpApi.Repository.TagRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TagService
{
    private TagRepo tagRepo;

    private final SearchService searchService;

    public void InitTags()
    {
        String[] tags = { "medicine", "music", "history", "science", "psychology", "travel", "art", "technology", "education", "literature", "sports", "cinema", "food", "languages", "animals", "politics", "religion", "games", "fashion", "architecture", "gardening", "business", "cars", "writing", "creativity", "dance", "photography", "environment"};
        String[] value = {"Медицина", "Музыка", "История", "Наука", "Психология", "Путешествия", "Искусство", "Технологии", "Образование", "Литература", "Спорт", "Кино", "Еда", "Языки", "Животные", "Политика", "Религия", "Игры", "Мода", "Архитектура", "Садоводство", "Бизнес", "Автомобили", "Писательство", "Творчество", "Танцы", "Фотография", "Окружающая среда"};
        for (int i = 0; i<value.length; i++)
        {
            tagRepo.save(Tag.builder().tagId(tags[i]).name(value[i]).build());
        }
    }

    public TagListResponse GetAllTags()
    {
        List<TagData> result = new ArrayList<TagData>();

        for (Tag tag: tagRepo.findAll())
        {
            result.add(TagData.builder().label(tag.getName()).value(tag.getTagId()).build());
        }
        return new TagListResponse(result);
    }

}
