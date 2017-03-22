package com.company.insurance.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class ModulePriceRequestDTO {
    @NotNull
    @ApiModelProperty(notes = "Module ID.", required = true)
    private Long moduleId;
    @NotNull
    @ApiModelProperty(notes = "User ID.", required = true)
    private Long userId;
    @NotNull
    @ApiModelProperty(notes = "Module's coverage.", required = true)
    private Double moduleCoverage;
}
