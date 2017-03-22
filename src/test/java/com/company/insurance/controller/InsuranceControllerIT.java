package com.company.insurance.controller;

import com.company.insurance.dto.ModulePriceRequestDTO;
import com.company.insurance.repository.ModuleRepository;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import static com.company.insurance.TestRepositoryDataFactory.createModule;
import static com.company.insurance.domain.ModuleType.*;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;

public class InsuranceControllerIT extends BaseControllerIT {

    @Value("http://localhost:${local.server.port}/insurance")
    private String insuranceBase;
    @Autowired
    private ModuleRepository moduleRepository;

    @Test
    public void testModuleList_success() throws Exception {
        Lists.newArrayList(BIKE, ELECTRONICS, JEWELRY).stream().parallel().forEach(t -> moduleRepository.save(createModule(t)));

        given()
            .and().parameter("page", 0)
            .and().parameter("size", 2)
        .when()
            .get(insuranceBase + "/modules").prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("totalPages", is(2))
            .body("totalEntries", is(3))
            .body("entries.findAll { it.id > 0 }.size()", is(2))
            .body("entries.findAll { it.type != null }.size()", is(2))
            .body("entries.findAll { it.minCoverage != null }.size()", is(2))
            .body("entries.findAll { it.maxCoverage != null }.size()", is(2))
            .body("entries.findAll { it.risk != null }.size()", is(2));
    }

    @Test
    public void testCalculatePrice_success() throws Exception {
        final Long userId = createAndVerifyUser_success(dto);
        final Long moduleId = moduleRepository.save(createModule(ELECTRONICS)).getId();
        final ModulePriceRequestDTO modulePriceRequestDTO = ModulePriceRequestDTO.builder()
                .userId(userId)
                .moduleId(moduleId)
                .moduleCoverage(10000.67d)
                .build();

        given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(modulePriceRequestDTO))
        .when()
            .post(insuranceBase + "/price").prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body(containsString("330.02"));
    }

    @Test
    public void testCalculatePrice_fail_userNotFound() throws Exception {
        final Long userId = createAndVerifyUser_success(dto);
        final Long moduleId = moduleRepository.save(createModule(ELECTRONICS)).getId();

        calculateAndValidatePrice_fail(ModulePriceRequestDTO.builder()
                .userId(userId * 1000)
                .moduleId(moduleId)
                .moduleCoverage(10000.67d)
                .build());
    }

    @Test
    public void testCalculatePrice_fail_moduleNotFound() throws Exception {
        final Long userId = createAndVerifyUser_success(dto);
        final Long moduleId = moduleRepository.save(createModule(ELECTRONICS)).getId();

        calculateAndValidatePrice_fail(ModulePriceRequestDTO.builder()
                .userId(userId)
                .moduleId(moduleId * 1000)
                .moduleCoverage(10000.67d)
                .build());
    }

    @Test
    public void testCalculatePrice_fail_moduleCoverageIsNotValid() throws Exception {
        final Long userId = createAndVerifyUser_success(dto);
        final Long moduleId = moduleRepository.save(createModule(ELECTRONICS)).getId();

        calculateAndValidatePrice_fail(ModulePriceRequestDTO.builder()
                .userId(userId)
                .moduleId(moduleId)
                .moduleCoverage(20000.01d)
                .build());
    }

    @Test
    public void testCalculatePrice_fail_emptyCoverage() throws Exception {
        final Long userId = createAndVerifyUser_success(dto);
        final Long moduleId = moduleRepository.save(createModule(ELECTRONICS)).getId();

        calculateAndValidatePrice_fail(ModulePriceRequestDTO.builder()
                .userId(userId)
                .moduleId(moduleId)
                .build());
    }

    @Test
    public void testCalculatePrice_fail_emptyUserId() throws Exception {
        final Long moduleId = moduleRepository.save(createModule(ELECTRONICS)).getId();

        calculateAndValidatePrice_fail(ModulePriceRequestDTO.builder()
                .moduleId(moduleId)
                .moduleCoverage(20000.01d)
                .build());
    }

    @Test
    public void testCalculatePrice_fail_emptyModuleId() throws Exception {
        final Long userId = createAndVerifyUser_success(dto);

        calculateAndValidatePrice_fail(ModulePriceRequestDTO.builder()
                .userId(userId)
                .moduleCoverage(20000.01d)
                .build());
    }

    private void calculateAndValidatePrice_fail(ModulePriceRequestDTO modulePriceRequestDTO) throws Exception {
        given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(modulePriceRequestDTO))
        .when()
            .post(insuranceBase + "/price").prettyPeek()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
        moduleRepository.deleteAll();
    }
}
