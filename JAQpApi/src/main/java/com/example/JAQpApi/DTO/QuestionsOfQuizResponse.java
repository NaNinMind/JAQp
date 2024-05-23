package com.example.JAQpApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.JAQpApi.Entity.Quiz.Question;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionsOfQuizResponse implements Serializable
{
    private List<Integer> questions;

    static public QuestionsOfQuizResponse toDto(List<Question> _list)
    {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < _list.size(); i++)
        {
            list.add(_list.get(i).getId());
        }
        return new QuestionsOfQuizResponse(list);
    }
}
