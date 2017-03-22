package com.company.insurance;

import com.company.insurance.domain.Module;
import com.company.insurance.domain.User;
import com.company.insurance.repository.ModuleRepository;
import com.company.insurance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;

import static com.company.insurance.domain.ModuleType.*;

/**
 * Loads data on start up. It avoid us writing SQL statements.
 */

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Profile("!test")
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        userRepository.save(User.builder()
                .firstName("John")
                .lastName("Doe")
                .riskFactor(0.3d)
                .build());
        userRepository.save(User.builder()
                .firstName("Max")
                .lastName("Mustermann")
                .riskFactor(0.5d)
                .build());

        moduleRepository.save(Module.builder()
                .type(BIKE)
                .minCoverage(new BigDecimal(0))
                .maxCoverage(new BigDecimal(3000.0d))
                .risk(30.0d)
                .build());
        moduleRepository.save(Module.builder()
                .type(JEWELRY)
                .minCoverage(new BigDecimal(500))
                .maxCoverage(new BigDecimal(10000.0d))
                .risk(5.0d)
                .build());
        moduleRepository.save(Module.builder()
                .type(ELECTRONICS)
                .minCoverage(new BigDecimal(500))
                .maxCoverage(new BigDecimal(6000.0d))
                .risk(35.0d)
                .build());
        moduleRepository.save(Module.builder()
                .type(SPORTS_EQUIPMENT)
                .minCoverage(new BigDecimal(0))
                .maxCoverage(new BigDecimal(20000.0d))
                .risk(30.0d)
                .build());
    }
}
