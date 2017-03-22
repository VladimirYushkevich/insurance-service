package com.company.insurance.service;

import com.company.insurance.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Simple CRUD operations for Users.
 */

public interface UserService {

    User create(User user);

    User update(User user);

    void delete(Long userId);

    User find(Long userId);

    Page<User> findAllByPage(Pageable pageable);
}
