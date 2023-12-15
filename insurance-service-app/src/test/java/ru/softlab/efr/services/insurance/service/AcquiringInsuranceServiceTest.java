package ru.softlab.efr.services.insurance.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.config.TestApplicationConfig;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.services.InsuranceService;
import ru.softlab.efr.services.insurance.services.StatusService;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
@Transactional
public class AcquiringInsuranceServiceTest {

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private AcquiringInsuranceService acquiringInsuranceService;

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void searchKspContract() {
        Insurance insurance = insuranceService.findById(11L);
        assertEquals("MADE_NOT_COMPLETED", insurance.getStatus().getCode().name());
        assertEquals("KSP", insurance.getProgramSetting().getProgram().getType().name());
        acquiringInsuranceService.checkKspContract();
        insurance = insuranceService.findById(11L);
        assertEquals("REVOKED", insurance.getStatus().getCode().name());
    }

    @Test
    @Sql(value = "classpath:test-script.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void searchContract() {
        Insurance insurance = insuranceService.findById(11L);
        insurance.setStatus(statusService.getByCode(InsuranceStatusCode.MADE));
        insuranceService.update(insurance);
        insurance = insuranceService.findById(11L);
        assertEquals("MADE", insurance.getStatus().getCode().name());
        assertEquals("KSP", insurance.getProgramSetting().getProgram().getType().name());
        acquiringInsuranceService.checkKspContract();
        insurance = insuranceService.findById(11L);
        assertEquals("MADE", insurance.getStatus().getCode().name());
    }
}
