package com.vizen.request.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@ApiModel(value = "IdListDto", description = "DTO to send list of Id values")
@Accessors(chain = true)
@Setter
@Getter
public class IdListDto {

    @ApiModelProperty(notes = "Id List", required = true)
    private List<String> idList;

}
