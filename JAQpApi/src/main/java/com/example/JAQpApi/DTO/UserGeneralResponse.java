package com.example.JAQpApi.DTO;

import com.example.JAQpApi.Entity.User.Role;
import com.example.JAQpApi.Entity.User.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGeneralResponse
{
    private Integer id;

    private String username;

    private Role role;

    private Timestamp createdAt;

    private String firstName;

    private String secondName;

    private String lastName;

    private OffsetDateTime birthDate;

    public static UserGeneralResponse FromUser(User _user)
    {
        return UserGeneralResponse.builder()
                .id(_user.getId())
                .username(_user.getUsername())
                .role(_user.getRole())
                .createdAt(_user.getCreatedAt())
                .firstName(_user.getFirstName())
                .lastName(_user.getLastName())
                .birthDate(_user.getBirthDate())
                .build();
    }

}
