package com.example.JAQpApi.Entity.User;


import com.example.JAQpApi.Entity.Quiz.ImageMetadata;
import com.example.JAQpApi.Entity.Quiz.Quiz;
import com.example.JAQpApi.Entity.Token.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails
{
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @Column(nullable = false)
    private Timestamp createdAt;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private OffsetDateTime birthDate;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ImageMetadata> imageMetadata;

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private List<Quiz> quizzes;

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return role.getAuthorities();
    }


}
