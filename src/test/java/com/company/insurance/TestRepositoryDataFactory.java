package com.company.insurance;

import com.company.insurance.domain.Module;
import com.company.insurance.domain.ModuleType;
import com.company.insurance.domain.User;

import java.math.BigDecimal;

/**
 * Simple data factory for testing.
 */

public class TestRepositoryDataFactory {

    public static User createUser() {
        return User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .riskFactor(0.8d)
                .build();
    }

    public static Module createModule(ModuleType moduleType) {
        return Module.builder()
                .type(moduleType)
                .minCoverage(new BigDecimal(0))
                .maxCoverage(new BigDecimal(20000))
                .risk(30d)
                .build();
    }
}
