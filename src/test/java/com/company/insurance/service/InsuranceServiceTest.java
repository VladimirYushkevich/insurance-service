package com.company.insurance.service;

import com.company.insurance.domain.Module;
import com.company.insurance.domain.User;
import com.company.insurance.dto.ModulePriceRequestDTO;
import com.company.insurance.exception.ModuleValidationException;
import com.company.insurance.repository.ModuleRepository;
import com.company.insurance.repository.UserRepository;
import com.company.insurance.service.impl.InsuranceServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static com.company.insurance.domain.ModuleType.BIKE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InsuranceServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModuleRepository moduleRepository;
    @InjectMocks
    private InsuranceServiceImpl insuranceService;

    private User user1;
    private User user2;
    private Module module;

    @Before
    public void setUp() throws Exception {
        user1 = User.builder().riskFactor(0.25d).build();
        user2 = User.builder().riskFactor(0.9d).build();

        module = Module.builder().type(BIKE).minCoverage(new BigDecimal(500)).maxCoverage(new BigDecimal(6000)).risk(35.0d).build();

        when(userRepository.findOne(1L)).thenReturn(user1);
        when(userRepository.findOne(2L)).thenReturn(user2);

        when(moduleRepository.findOne(anyLong())).thenReturn(module);
    }

    @Test
    public void calculateTariffPrice_success() throws Exception {
        assertThat(insuranceService.calculateTariffPrice(ModulePriceRequestDTO.builder().userId(1L).moduleCoverage(3371.0d).build())
                .getPrice(), is(new BigDecimal("147.48")));
        assertThat(insuranceService.calculateTariffPrice(ModulePriceRequestDTO.builder().userId(1L).moduleCoverage(4371.0d).build())
                .getPrice(), is(new BigDecimal("191.23")));

        assertThat(insuranceService.calculateTariffPrice(ModulePriceRequestDTO.builder().userId(2L).moduleCoverage(3371.0d).build())
                .getPrice(), is(new BigDecimal("224.17")));
        assertThat(insuranceService.calculateTariffPrice(ModulePriceRequestDTO.builder().userId(2L).moduleCoverage(4371.0d).build())
                .getPrice(), is(new BigDecimal("290.67")));
    }

    @Test(expected = ModuleValidationException.class)
    public void calculateTariffPrice_fail_userNotFound() throws Exception {
        when(userRepository.findOne(anyLong())).thenReturn(null);

        insuranceService.calculateTariffPrice(ModulePriceRequestDTO.builder().userId(1L).moduleCoverage(3371.0d).build());
    }

    @Test(expected = ModuleValidationException.class)
    public void calculateTariffPrice_fail_moduleNotFound() throws Exception {
        when(moduleRepository.findOne(anyLong())).thenReturn(null);

        insuranceService.calculateTariffPrice(ModulePriceRequestDTO.builder().userId(1L).moduleCoverage(3371.0d).build());
    }

    @Test
    public void calculateTariffPrice_fail_moduleCoverageIsNotValid() throws Exception {
        try {
            insuranceService.calculateTariffPrice(ModulePriceRequestDTO.builder().userId(1L).moduleCoverage(499.99d).build());
            fail("Should throw exception");
        } catch (ModuleValidationException ignored) {
        }
        try {
            insuranceService.calculateTariffPrice(ModulePriceRequestDTO.builder().userId(1L).moduleCoverage(6000.01d).build());
            fail("Should throw exception");
        } catch (ModuleValidationException ignored) {
        }
    }
}
