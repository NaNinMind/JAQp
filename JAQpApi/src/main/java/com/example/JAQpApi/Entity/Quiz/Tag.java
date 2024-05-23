package com.example.JAQpApi.Entity.Quiz;



import co.elastic.clients.elasticsearch.license.LicenseStatus;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import java.util.List;

import org.checkerframework.common.aliasing.qual.Unique;


@Entity
@Table(name = "tag")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag
{
    @Id
    @Column
    @Unique
    @FullTextField
    private String tagId;

    @Column
    @FullTextField
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Quiz> quizList;

    public String getTagId() {
        return tagId;
    }

    public String getName() {
        return name;
    }

}
