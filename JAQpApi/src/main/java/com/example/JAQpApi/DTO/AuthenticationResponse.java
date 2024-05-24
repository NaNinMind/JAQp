package com.example.JAQpApi.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse
{   
    @Schema(name = "Auth Msg", example = "auth success")
    private String msg;
    @Schema(name = "Auth id", example = "123")
    private Integer id;
    @Schema(name = "Auth token", example = "bfkjwebjk2b34bjkbkjdsbsjksdkjfnkw32")
    private String jwtToken;
    @Schema( example = "username123")
    private String username;


    public AuthenticationResponse(String msg) {
        this.msg = msg;
    }
}
