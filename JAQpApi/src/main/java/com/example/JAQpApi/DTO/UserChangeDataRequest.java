package com.example.JAQpApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeDataRequest
{
    @Schema(example = "Ivan")
    private String firstName;
    @Schema ( example = "Ivanov")
    private String lastName;
    @Schema ( example = "1999.9.9")
    private OffsetDateTime birthDate;
}
