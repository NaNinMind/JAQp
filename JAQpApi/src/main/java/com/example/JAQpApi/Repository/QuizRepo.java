package com.example.JAQpApi.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.JAQpApi.Entity.Quiz.Quiz;


public interface QuizRepo extends JpaRepository<Quiz, Integer> {



  @Query(value = """
    select q from Quiz q inner join User u\s
    on q.user.id = u.id\s
    where u.id = :id\s
    """)
  List<Quiz> findAllQuizByUserId(Integer id);

  Optional<Quiz> findByName(String name);
    
    
}
