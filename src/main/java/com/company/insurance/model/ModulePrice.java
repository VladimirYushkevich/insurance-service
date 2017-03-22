package com.company.insurance.model;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * Simple model to handle all information for price calculation.
 */

@Builder
public class ModulePrice {

    private Double coverage;
    private Double moduleRisk;
    private Double userRisk;

    /**
     * Very simple calculation of price of the tariff for specific coverage, which is the individual configuration for each customer
     * ({@link com.company.insurance.domain.User#riskFactor}), based on the risk({@link com.company.insurance.domain.Module#risk}).
     */
    public BigDecimal getPrice() {
        return new BigDecimal((coverage * moduleRisk / 1000) * (1 + userRisk)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
