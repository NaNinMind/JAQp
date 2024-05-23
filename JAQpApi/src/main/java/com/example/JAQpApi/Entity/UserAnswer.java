package com.example.JAQpApi.Entity;

import com.example.JAQpApi.Entity.Quiz.Answer;
import com.example.JAQpApi.Entity.User.User;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;

@Entity
@Table(name = "user_answers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAnswer
{
    @Column(nullable = false)
    @Unique
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = true)
    private User user;

    @ManyToOne
    private Answer answer;

}
