package com.my.bob.member.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TokenDto {

    private String grantType;

    private String accessToken;

    private LocalDateTime accessTokenExpire;

    private String refreshToken;

    private LocalDateTime refreshTokenExpire;
}