package com.example.JAQpApi.Entity.Quiz;

import java.util.List;

import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.example.JAQpApi.Entity.User.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="quiz")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Quiz {


    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "text")
    private String name;
    
    // @ElementCollection(fetch = FetchType.EAGER)
    // @CollectionTable(name = "questions", joinColumns = @JoinColumn(name = "quiz_id"))
    // @Column(name = "question", nullable = false, columnDefinition = "text[]")
    // private List<Question> questions;

    @JdbcTypeCode(SqlTypes.JSON)
    private String questions;



}


