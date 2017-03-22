package com.company.insurance.repository;

import com.company.insurance.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;

import static com.company.insurance.TestRepositoryDataFactory.createUser;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser() throws Exception {
        final User user = entityManager.persist(createUser());

        final User createdUser = userRepository.findOne(user.getId());
        assertThat(createdUser, is(user));
        assertThat(createdUser.getCreated(), is(createdUser.getUpdated()));
    }

    @Test(expected = PersistenceException.class)
    public void testCreateUser_fail_emptyFirstName() throws Exception {
        final User user = createUser();
        user.setFirstName(null);

        entityManager.persist(user);
    }

    @Test(expected = PersistenceException.class)
    public void testCreateUser_fail_emptyLastName() throws Exception {
        final User user = createUser();
        user.setLastName(null);

        entityManager.persist(user);
    }

    @Test(expected = PersistenceException.class)
    public void testCreateUser_fail_emptyRiskFactor() throws Exception {
        final User user = createUser();
        user.setRiskFactor(null);

        entityManager.persist(user);
    }

    @Test
    public void testUpdateUser() throws Exception {
        final User user = entityManager.persist(createUser());

        user.setFirstName("firstNameNew");
        user.setLastName("lastNameNew");
        user.setRiskFactor(1.1d);
        userRepository.flush();

        entityManager.clear();
        final User updatedUser = userRepository.findOne(user.getId());
        assertThat(updatedUser.getFirstName(), is("firstNameNew"));
        assertThat(updatedUser.getLastName(), is("lastNameNew"));
        assertThat(updatedUser.getRiskFactor(), is(1.1d));
        assertThat(updatedUser.getUpdated().getTime(), greaterThan(updatedUser.getCreated().getTime()));
    }

    @Test
    public void testDeleteUser() throws Exception {
        final User user = entityManager.persist(createUser());

        final Long id = user.getId();
        userRepository.delete(id);

        assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    public void testPageable() throws Exception {
        entityManager.persist(createUser());
        entityManager.persist(createUser());
        entityManager.persist(createUser());

        final Page<User> userPage1 = userRepository.findAll(new PageRequest(0, 2));

        assertThat(userPage1.getTotalElements(), is(3L));
        assertThat(userPage1.getContent().size(), is(2));

        final Page<User> userPage2 = userRepository.findAll(new PageRequest(1, 2));
        //then
        assertThat(userPage2.getTotalElements(), is(3L));
        assertThat(userPage2.getContent().size(), is(1));
    }
}
