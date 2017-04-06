package com.company.insurance.controller;

import com.company.insurance.dto.ModulePriceRequestDTO;
import com.company.insurance.dto.ModuleResponseDTO;
import com.company.insurance.dto.PageDTO;
import com.company.insurance.service.InsuranceService;
import com.company.insurance.utils.ModuleMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/insurance")
@AllArgsConstructor
@Slf4j
@Api(description = "Business operations for modules")
public class InsuranceController {

    private final InsuranceService insuranceService;
    private final ModuleMapper mapper;

    @RequestMapping(path = "/modules", method = RequestMethod.GET)
    @ApiOperation(value = "Fetches list of modules per page.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0...N)."),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    public PageDTO<ModuleResponseDTO> listModules(Pageable pageable) {
        log.debug("::listModules {}", pageable);

        return mapper.buildPageModuleResponseDTO(insuranceService.findAllByPage(pageable));
    }

    @RequestMapping(path = "/price", method = RequestMethod.POST)
    @ApiOperation(value = "Calculate module tariff price for specific user.")
    public BigDecimal calculatePrice(@Validated @RequestBody ModulePriceRequestDTO request) {
        log.debug("::calculatePrice {}", request);

        return insuranceService.calculateTariffPrice(request).getPrice();
    }
}
