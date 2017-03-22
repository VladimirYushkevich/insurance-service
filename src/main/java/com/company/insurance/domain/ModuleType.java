package com.company.insurance.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ModuleType {

    BIKE("Bike"), JEWELRY("Jewelry"), ELECTRONICS("Electronics"), SPORTS_EQUIPMENT("Sports Equipment");

    @Getter
    private String name;

    public static ModuleType findByName(String name) {
        if (null == name) {
            return null;
        }

        for (ModuleType moduleType : values()) {
            if (moduleType.getName().equals(name)) {
                return moduleType;
            }
        }

        return null;
    }
}
