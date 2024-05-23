package com.example.JAQpApi.Entity.Quiz;

import com.example.JAQpApi.Entity.User.User;
import com.example.JAQpApi.Entity.UserResult;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.annotations.Cascade;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import java.util.List;

@Entity
@Table(name = "quiz")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Indexed(index = "quiz_index")
public class Quiz
{
    @Column(nullable = false)
    @Unique
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)

    @GenericField

    private Boolean isPublic;

    @Column(nullable = true)
    @FullTextField
    private String name;

    @Column(nullable = true)
    @FullTextField
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToOne
    @JoinColumn(name = "imageMetadata_name", nullable = true)
    private ImageMetadata thumbnail;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Question> questions;


    @ManyToMany
    @IndexedEmbedded
    private List<Tag> tags;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE)
    private List<UserResult> userResults;
}
