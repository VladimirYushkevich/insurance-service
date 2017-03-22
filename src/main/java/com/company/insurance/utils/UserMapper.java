package com.company.insurance.utils;

import com.company.insurance.domain.User;
import com.company.insurance.dto.PageDTO;
import com.company.insurance.dto.UserRequestDTO;
import com.company.insurance.dto.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Utility class for mapping user to related DTOs and vice versa.
 */

@Component
public class UserMapper extends GenericRequestResponseMapper<UserRequestDTO, UserResponseDTO, User> {

    public UserMapper() {
        super(User.class, UserResponseDTO.class);
    }

    public User buildUser(UserRequestDTO dto) {
        return buildModel(dto);
    }

    public UserResponseDTO buildUserResponseDTO(User user) {
        return buildDTO(user);
    }

    public PageDTO<UserResponseDTO> buildPageUserResponseDTO(Page<User> userPage) {
        return buildPageResponseDTO(userPage);
    }
}
