package com.company.insurance.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "modules")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Module extends InsuranceEntity {

    @Column(nullable = false, updatable = false, unique = true)
    private String type;

    @Column(name = "min_coverage", nullable = false)
    private BigDecimal minCoverage;

    @Column(name = "max_coverage", nullable = false)
    private BigDecimal maxCoverage;

    @Column(nullable = false)
    private Double risk;

    @Builder
    public Module(ModuleType type, BigDecimal minCoverage, BigDecimal maxCoverage, Double risk) {
        this.type = type.getName();
        this.minCoverage = minCoverage;
        this.maxCoverage = maxCoverage;
        this.risk = risk;
    }

    public ModuleType getType() {
        return ModuleType.findByName(this.type);
    }

    public void setType(ModuleType type) {
        this.type = type.getName();
    }
}
