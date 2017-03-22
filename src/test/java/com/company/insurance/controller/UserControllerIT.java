package com.company.insurance.controller;

import com.company.insurance.dto.UserRequestDTO;
import com.google.common.collect.Lists;
import com.jayway.restassured.RestAssured;
import org.junit.After;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.config.JsonConfig.jsonConfig;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;


public class UserControllerIT extends BaseControllerIT {

    @Test
    public void testCreateUser_fail_emptyFields() throws Exception {
        dto.setFirstName(null);
        dto.setLastName(null);
        dto.setRiskFactor(null);

        createAndVerifyUser_fail(dto, Lists.newArrayList("firstName", "lastName", "riskFactor"));
    }

    @Test
    public void testCreateUser_fail_notValidRiskFactor() throws Exception {
        dto.setRiskFactor(0.0d);
        createAndVerifyUser_fail(dto, Lists.newArrayList("riskFactor"));

        dto.setRiskFactor(-0.5d);
        createAndVerifyUser_fail(dto, Lists.newArrayList("riskFactor"));

        dto.setRiskFactor(1.0d);
        createAndVerifyUser_fail(dto, Lists.newArrayList("riskFactor"));
    }

    @Test
    public void testUpdateUser_success() throws Exception {
        final Long userId = createAndVerifyUser_success(dto);
        dto.setFirstName("newFistName");
        dto.setLastName(null);
        dto.setRiskFactor(0.5d);

        given()
            .config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .put(userBase + "/{user_id}", userId).prettyPeek()
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", notNullValue())
            .body("firstName", is(dto.getFirstName()))
            .body("lastName", is("testLastName"))
            .body("riskFactor", closeTo(BigDecimal.valueOf(dto.getRiskFactor()), new BigDecimal("1E-20")));
    }

    @Test
    public void testUserList_success() throws Exception {
        IntStream.rangeClosed(1, 3).forEach(i -> createUserSafe(dto, i));

        given()
            .and().parameter("page", 0)
            .and().parameter("size", 2)
        .when()
            .get(userBase).prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("totalPages", is(2))
            .body("totalEntries", is(3))
            .body("entries.findAll { it.id > 0 }.size()", is(2))
            .body("entries.findAll { it.firstName != null }.size()", is(2))
            .body("entries.findAll { it.lastName != null }.size()", is(2))
            .body("entries.findAll { it.riskFactor != null }.size()", is(2));
    }

    @Test
    public void getUserById_success() throws Exception {
        final Long userId = createAndVerifyUser_success(dto);

        given()
            .config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
        .when()
            .get(userBase + "/{user_id}", userId).prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", notNullValue())
            .body("firstName", is(dto.getFirstName()))
            .body("lastName", is(dto.getLastName()))
            .body("riskFactor", closeTo(BigDecimal.valueOf(dto.getRiskFactor()), new BigDecimal("1E-20")));
    }

    @Test
    public void getUserById_fail() throws Exception {
        final Long userId = createAndVerifyUser_success(dto);

        when()
            .get(userBase + "/{user_id}", userId * 10000).prettyPeek()
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deleteUser_success() throws Exception {
        final Long userId = createAndVerifyUser_success(dto);

        when()
            .delete(userBase + "/{user_id}", userId).prettyPeek()
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    public void deleteUser_fail_notFound() throws Exception {
        final Long userId = createAndVerifyUser_success(dto);

        when()
            .delete(userBase + "/{user_id}", userId * 1000).prettyPeek()
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void createAndVerifyUser_fail(UserRequestDTO dto, List<String> errorFields) throws Exception {
        given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .post(userBase).prettyPeek()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors.field", containsInAnyOrder(errorFields.toArray()));
    }

    private void createUserSafe(UserRequestDTO dto, int counter) {
        dto.setFirstName(dto.getFirstName() + counter);
        dto.setLastName(dto.getLastName() + counter);
        try {
            createAndVerifyUser_success(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }
}
