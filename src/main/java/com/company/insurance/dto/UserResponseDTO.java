package com.company.insurance.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    @NotNull
    @ApiModelProperty(notes = "User's first name", required = true)
    private String firstName;
    @NotNull
    @ApiModelProperty(notes = "User's last name", required = true)
    private String lastName;
    @NotNull
    @ApiModelProperty(notes = "User's risk factor. Individual configuration for each customer :)", required = true)
    private Double riskFactor;
}
