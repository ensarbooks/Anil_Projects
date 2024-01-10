package com.vizen.request.dto;

import com.vizen.util.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;



@ApiModel(value = "CreateUpdateForecastDashBoardDto", description = "Parameters required to create/update ForecastDashBoard")
@Accessors(chain = true)
@Setter
@Getter
public class CreateUpdateForecastDashBoardDto {

    @ApiModelProperty(notes = "ForecastDashBoard Name", required = true)
    @NotBlank(message = "ForecastDashBoard Name is required")
    @Size(max = 50)
    private String name;

    @ApiModelProperty(notes = "ForecastDashBoard Description", required = false)
    @Size(max = 500)
    private String description;

    @ApiModelProperty(notes = "Forecast dashboard ID")
    @NotBlank(message = "ForecastDashBoard ID is required")
    @Pattern(regexp = Constants.UUID_PATTERN, message = "Invalid Forecast dashboard ID")
    private String dashBoardId;

    @ApiModelProperty(notes = "Organization")
    @NotBlank(message = "ForecastDashBoard Organization is required")
    private String organizationId;

}
