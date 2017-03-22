package com.company.insurance.repository;

import com.company.insurance.domain.Module;
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
import java.math.BigDecimal;

import static com.company.insurance.TestRepositoryDataFactory.createModule;
import static com.company.insurance.domain.ModuleType.*;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
public class ModuleRepositoryIT {

    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateModule() throws Exception {
        final Module module = entityManager.persist(createModule(SPORTS_EQUIPMENT));

        final Module createdModule = moduleRepository.findOne(module.getId());
        assertThat(createdModule, is(module));
        assertThat(createdModule.getCreated(), is(createdModule.getUpdated()));
    }

    @Test(expected = PersistenceException.class)
    public void testCreateModule_fail_emptyType() throws Exception {
        final Module module = new Module();
        module.setMinCoverage(new BigDecimal(0));
        module.setMaxCoverage(new BigDecimal(20000));
        module.setRisk(0.30d);

        entityManager.persist(module);
    }

    @Test(expected = PersistenceException.class)
    public void testCreateModule_fail_emptyMinCoverage() throws Exception {
        final Module module = new Module();
        module.setMinCoverage(null);

        entityManager.persist(module);
    }

    @Test(expected = PersistenceException.class)
    public void testCreateModule_fail_emptyMaxCoverage() throws Exception {
        final Module module = new Module();
        module.setMaxCoverage(null);

        entityManager.persist(module);
    }

    @Test(expected = PersistenceException.class)
    public void testCreateModule_fail_emptyRisk() throws Exception {
        final Module module = new Module();
        module.setRisk(null);

        entityManager.persist(module);
    }

    @Test
    public void testUpdateModule() throws Exception {
        final Module module = entityManager.persist(createModule(SPORTS_EQUIPMENT));

        module.setType(ELECTRONICS);
        module.setMinCoverage(new BigDecimal(500));
        module.setMaxCoverage(new BigDecimal(25000));
        module.setRisk(0.40d);
        moduleRepository.flush();

        entityManager.clear();
        final Module updatedModule = moduleRepository.findOne(module.getId());
        assertThat(updatedModule.getType(), is(SPORTS_EQUIPMENT));
        assertThat(updatedModule.getMinCoverage().longValue(), is(500L));
        assertThat(updatedModule.getMaxCoverage().longValue(), is(25000L));
        assertThat(updatedModule.getRisk(), is(0.40d));
        assertThat(updatedModule.getUpdated().getTime(), greaterThan(updatedModule.getCreated().getTime()));
    }

    @Test
    public void testDeleteModule() throws Exception {
        final Module module = entityManager.persist(createModule(SPORTS_EQUIPMENT));

        final Long id = module.getId();
        moduleRepository.delete(id);

        assertTrue(moduleRepository.findAll().isEmpty());
    }

    @Test
    public void testPageable() throws Exception {
        entityManager.persist(createModule(SPORTS_EQUIPMENT));
        entityManager.persist(createModule(BIKE));
        entityManager.persist(createModule(JEWELRY));

        final Page<Module> modulePage1 = moduleRepository.findAll(new PageRequest(0, 2));

        assertThat(modulePage1.getTotalElements(), is(3L));
        assertThat(modulePage1.getContent().size(), is(2));

        final Page<Module> modulePage2 = moduleRepository.findAll(new PageRequest(1, 2));
        //then
        assertThat(modulePage2.getTotalElements(), is(3L));
        assertThat(modulePage2.getContent().size(), is(1));
    }
}
