package com.example.JAQpApi.Repository;


import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.JAQpApi.Entity.Quiz.Question;

@Hidden
public interface QuestionRepo extends JpaRepository<Question, Integer>
{
    // Optional<Question> findBy
}
