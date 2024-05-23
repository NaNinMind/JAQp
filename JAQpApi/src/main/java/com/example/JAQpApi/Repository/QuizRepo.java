package com.example.JAQpApi.Repository;


import com.example.JAQpApi.Entity.Quiz.Quiz;
import com.example.JAQpApi.Entity.User.User;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Hidden
public interface QuizRepo extends JpaRepository<Quiz, Integer>
{
    List<Quiz> findAllByOwner(User owner);
    Optional<Quiz> findByName(String name);
}
