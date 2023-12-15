package ru.softlab.efr.services.insurance.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.ProgramKind;
import ru.softlab.efr.services.insurance.services.InsuranceService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
public class InsuranceServiceTest {

    @Autowired
    private InsuranceService insuranceService;

    @Test
    @Sql(value = {"classpath:test-script.sql","classpath:test-script-ins.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetInsurancesWithSameProgramForInsuredEndingAfter() {
        List<Insurance> programs = insuranceService.findAllByProgramAndClientAndEndDateMoreThan(8L, 4L, LocalDate.of(2018, 1, 1));
        assertEquals(2, programs.size());
        programs = insuranceService.findAllByProgramAndClientAndEndDateMoreThan(8L, 4L, LocalDate.of(2018, 2, 2));
        assertEquals(1, programs.size());
    }

    @Test
    @Sql(value = {"classpath:test-script.sql","classpath:test-script-ins.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetInsurancesWithWrongProgramForInsuredEndingAfter() {
        List<Insurance> programs = insuranceService.findAllByProgramAndClientAndEndDateMoreThan(5L, 4L, LocalDate.of(2018, 1, 1));
        assertEquals(0, programs.size());
    }

    @Test
    @Sql(value = {"classpath:test-script.sql","classpath:test-script-ins.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetInsurancesWithSameProgramKindForInsuredEndingAfter() {
        List<Insurance> programs = insuranceService.findAllByProgramKindAndClient(ProgramKind.KSP, 4L);
        assertEquals(2, programs.size());
    }
}
