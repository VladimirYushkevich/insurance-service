package com.company.insurance.dto;

import com.company.insurance.domain.ModuleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ModuleResponseDTO {
    @NotNull
    @ApiModelProperty(notes = "Module ID.", required = true)
    private Long id;
    @NotNull
    @ApiModelProperty(notes = "Module type.", required = true)
    private ModuleType type;
    @NotNull
    @ApiModelProperty(notes = "Module's MIN coverage.", required = true)
    private BigDecimal minCoverage;
    @NotNull
    @ApiModelProperty(notes = "Module's MAX coverage.", required = true)
    private BigDecimal maxCoverage;
    @NotNull
    @ApiModelProperty(notes = "Module's risk.", required = true)
    private Double risk;
}
