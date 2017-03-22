package com.company.insurance.controller;

import com.company.insurance.dto.UserRequestDTO;
import com.company.insurance.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.JsonConfig.jsonConfig;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseControllerIT {

    @Autowired
    ObjectMapper objectMapper;
    @Value("http://localhost:${local.server.port}/user")
    String userBase;
    @Autowired
    UserRepository userRepository;

    UserRequestDTO dto;

    @Before
    public void setUp() throws Exception {
        dto = objectMapper.readValue(getClass().getResourceAsStream("/json/user_request_dto.json"), UserRequestDTO.class);
    }

    Long createAndVerifyUser_success(UserRequestDTO dto) throws Exception {
        return given()
            .config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(dto))
        .when()
            .post(userBase).prettyPeek()
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", notNullValue())
            .body("firstName", is(dto.getFirstName()))
            .body("lastName", is(dto.getLastName()))
            .body("riskFactor", closeTo(BigDecimal.valueOf(dto.getRiskFactor()), new BigDecimal("1E-20")))
        .extract()
            .jsonPath()
            .getLong("id");
    }
}
