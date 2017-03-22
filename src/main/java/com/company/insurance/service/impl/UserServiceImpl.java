package com.company.insurance.service.impl;

import com.company.insurance.domain.User;
import com.company.insurance.exception.NotFoundException;
import com.company.insurance.repository.UserRepository;
import com.company.insurance.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

import static com.company.insurance.utils.GenericRequestResponseMapper.getNullPropertyNames;
import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        final User createdUser = userRepository.save(user);

        log.debug("created {}", createdUser);

        return createdUser;
    }

    @Override
    public User update(User user) {
        final Long userId = user.getId();
        final User userToUpdate = Optional.ofNullable(userRepository.findOne(userId))
                .orElseThrow(NotFoundException::new);

        copyProperties(user, userToUpdate, getNullPropertyNames(user));

        final User updatedUser = userRepository.save(userToUpdate);

        log.debug("updated {}", updatedUser);

        return updatedUser;
    }

    @Override
    public void delete(Long userId) {
        Optional.ofNullable(userRepository.findOne(userId))
                .orElseThrow(NotFoundException::new);

        userRepository.delete(userId);

        log.debug("deleted user with id = {}", userRepository);
    }

    @Override
    public User find(Long userId) {
        final User user = Optional.ofNullable(userRepository.findOne(userId))
                .orElseThrow(NotFoundException::new);

        log.debug("found {}", user);

        return user;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<User> findAllByPage(Pageable pageable) {
        final Page page = userRepository.findAll(pageable);

        log.debug("page {} from {} has {} users", page.getNumber(), page.getTotalElements(), page.getContent().size());

        return page;
    }
}
