package com.company.insurance.utils;

import com.company.insurance.domain.Module;
import com.company.insurance.dto.ModuleResponseDTO;
import com.company.insurance.dto.PageDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Utility class for mapping module to related DTOs and vice versa.
 */

@Component
public class ModuleMapper extends GenericResponseMapper<ModuleResponseDTO, Module> {

    public ModuleMapper() {
        super(ModuleResponseDTO.class);
    }

    public PageDTO<ModuleResponseDTO> buildPageModuleResponseDTO(Page<Module> modulePage) {
        return buildPageResponseDTO(modulePage);
    }
}
