package com.company.insurance.service;

import com.company.insurance.domain.Module;
import com.company.insurance.dto.ModulePriceRequestDTO;
import com.company.insurance.model.ModulePrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Simple business logic for insurance modules and risk/tariff price calculation.
 */

public interface InsuranceService {

    /**
     * Calculates individual price of module tariff for user with specific coverage.
     *
     * @param moduleRequest Module request
     * @return Module tariff price
     */
    ModulePrice calculateTariffPrice(ModulePriceRequestDTO moduleRequest);

    Page<Module> findAllByPage(Pageable pageable);
}
