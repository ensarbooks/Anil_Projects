package com.vizen.response.dto;

import com.vizen.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@Setter
@Getter
public class AuthTokenResponse {
    private String accessToken;
    private long expiresIn;
    private User user;
}


