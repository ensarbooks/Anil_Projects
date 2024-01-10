package com.vizen.request.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.*;

@ApiModel(value = "LoginUserParam", description = "Parameters required to login user")
@Accessors(chain = true)
@Setter
@Getter
public class LoginUserDto {
    @ApiModelProperty(notes = "User Email", required = true)
    @NotBlank(message = "User Email is required")
    private String email;

    @ApiModelProperty(notes = "User password", required = true)
    @NotBlank(message = "Password is required")
    private String password;
}
