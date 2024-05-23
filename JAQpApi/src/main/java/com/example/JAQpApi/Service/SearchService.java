package com.example.JAQpApi.Service;

import com.example.JAQpApi.DTO.OwnedQuizListResponse;
import com.example.JAQpApi.DTO.QuizData;
import com.example.JAQpApi.Entity.Quiz.Quiz;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService
{
    private final EntityManager entityManager;

    private final int pageSize = 10;
    public OwnedQuizListResponse FindQuiz(String _text, String _tags, Integer _page)
    {
        SearchSession searchSession = Search.session(entityManager);
        List<Quiz> searchResult;
        if(_text != null)
        {
            searchResult = searchSession.search(Quiz.class)
                    .where(f -> f.and(
                            f.or(
                                    f.match().fields("name", "description", "tags.name").matching(_text),
                                    f.match().field("tags.tagId").matching((_tags == null ? "" : _tags))
                            ),
                            f.match().field("isPublic").matching(true))
                    ).fetchHits(pageSize * _page, pageSize);
        }
        else
        {
            searchResult = searchSession.search(Quiz.class)
                    .where(f -> f.and(
                            f.wildcard().fields("name", "description", "tags.name").matching("*"),
                            f.match().field("isPublic").matching(true))
                    ).fetchHits(pageSize * _page, pageSize);
        }
        List<QuizData> result = new ArrayList<QuizData>();
        for (Quiz quiz : searchResult)
        {
            result.add(QuizData.builder()
                    .id(quiz.getId())
                    .name(quiz.getName())
                    .image((quiz.getThumbnail() == null) ? null : quiz.getThumbnail().getName())
                    .description(quiz.getDescription())
                    .build());

        }
        return new OwnedQuizListResponse(result);
    }

}
