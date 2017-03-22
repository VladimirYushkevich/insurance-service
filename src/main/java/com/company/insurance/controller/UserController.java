package com.company.insurance.controller;

import com.company.insurance.domain.User;
import com.company.insurance.dto.PageDTO;
import com.company.insurance.dto.UserRequestDTO;
import com.company.insurance.dto.UserResponseDTO;
import com.company.insurance.service.UserService;
import com.company.insurance.utils.UserMapper;
import com.company.insurance.validation.UserRequestDTOValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Slf4j
@Api(description = "CRUD operations for users")
public class UserController {

    private final UserService userService;
    private final UserRequestDTOValidator validator;
    private final UserMapper mapper;

    @InitBinder("userRequestDTO")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Creates user.")
    public UserResponseDTO createUser(@Validated @RequestBody UserRequestDTO request) {
        log.debug("::createUser {}", request);

        return mapper.buildUserResponseDTO(userService.create(mapper.buildUser(request)));
    }

    @RequestMapping(path = "/{user_id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Updates user.")
    public UserResponseDTO updateUser(@PathVariable("user_id") Long id, @RequestBody UserRequestDTO request) {
        log.debug("::updateUser {}", request);

        final User user = mapper.buildUser(request);
        user.setId(id);

        return mapper.buildUserResponseDTO(userService.update(user));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Fetches list of users per page.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0...N)."),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")
    })
    public PageDTO<UserResponseDTO> listUsers(Pageable pageable) {
        log.debug("::listUsers {}", pageable);

        return mapper.buildPageUserResponseDTO(userService.findAllByPage(pageable));
    }

    @RequestMapping(path = "/{user_id}", method = RequestMethod.GET)
    @ApiOperation(value = "Finds user by id.")
    public UserResponseDTO getUserById(@PathVariable("user_id") Long id) {
        log.debug("::getById {}", id);

        return mapper.buildUserResponseDTO(userService.find(id));
    }

    @RequestMapping(path = "/{user_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Deletes user.")
    public void deleteUser(@PathVariable("user_id") Long id) {
        log.debug("::deleteUser {}", id);

        userService.delete(id);
    }
}
