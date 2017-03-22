package com.company.insurance.service.impl;

import com.company.insurance.domain.Module;
import com.company.insurance.domain.User;
import com.company.insurance.dto.ModulePriceRequestDTO;
import com.company.insurance.exception.ModuleValidationException;
import com.company.insurance.model.ModulePrice;
import com.company.insurance.repository.ModuleRepository;
import com.company.insurance.repository.UserRepository;
import com.company.insurance.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Transactional
@Slf4j
public class InsuranceServiceImpl implements InsuranceService {

    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;

    @Override
    public ModulePrice calculateTariffPrice(ModulePriceRequestDTO moduleRequest) {
        log.debug("::calculateTariffPrice for {}", moduleRequest);

        final Long moduleId = moduleRequest.getModuleId();
        final Module module = Optional.ofNullable(moduleRepository.findOne(moduleId))
                .orElseThrow(() -> new ModuleValidationException(String.format("Can't find module by id=%s", moduleId)));

        log.debug("found {}", module);

        final Double moduleCoverage = moduleRequest.getModuleCoverage();
        if (moduleCoverage > module.getMaxCoverage().doubleValue() || moduleCoverage < module.getMinCoverage().doubleValue()) {
            throw new ModuleValidationException(String.format("Module's coverage [%s] is not valid for %s", moduleCoverage, module));
        }

        final Long userId = moduleRequest.getUserId();
        final User user = Optional.ofNullable(userRepository.findOne(userId))
                .orElseThrow(() -> new ModuleValidationException(String.format("Can't find user by id=%s", userId)));

        log.debug("found {}", user);

        return ModulePrice.builder()
                .coverage(moduleCoverage)
                .moduleRisk(module.getRisk())
                .userRisk(user.getRiskFactor())
                .build();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<Module> findAllByPage(Pageable pageable) {
        final Page page = moduleRepository.findAll(pageable);

        log.debug("page {} from {} has {} modules", page.getNumber(), page.getTotalElements(), page.getContent().size());

        return page;
    }
}
