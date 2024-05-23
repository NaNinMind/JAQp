package com.example.JAQpApi.Entity.Quiz;

import com.example.JAQpApi.Entity.UserAnswer;
import com.example.JAQpApi.Entity.UserResult;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.List;

@Entity
@Table(name = "answer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Answer
{
    @Column
    private String description;

    @Id
    @Unique
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private boolean is_right;


    @ManyToOne
    private Question question;

    @OneToOne(optional = true)
    @JoinColumn(name = "imageMetadata_name")
    private ImageMetadata image;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE)
    private List<UserAnswer> userAnswers;

}


