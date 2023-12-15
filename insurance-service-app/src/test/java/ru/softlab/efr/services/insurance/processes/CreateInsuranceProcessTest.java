package ru.softlab.efr.services.insurance.processes;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;
import ru.softlab.efr.services.insurance.config.TestProcessConfiguration;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;
import ru.softlab.efr.services.insurance.model.rest.Client;
import ru.softlab.efr.services.insurance.model.rest.SetStatusInsuranceModel;
import ru.softlab.efr.services.insurance.process.Definitions;
import ru.softlab.efr.services.insurance.process.HandleFormDataTask;
import ru.softlab.efr.services.insurance.process.SaveContractAndClientDataTask;
import ru.softlab.efr.services.insurance.utils.TestUtils;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;
import ru.softlab.efr.test.common.bpm.BpmProcessTestBase;

import java.time.LocalDate;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareAssertions.assertThat;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestProcessConfiguration.class})
public class CreateInsuranceProcessTest extends BpmProcessTestBase {
    private static final String CREATE_CLIENT_PROCESS_RESOURCE = "processes/create-contract.bpmn20.xml";
    private static final String CREATE_CONTRACT_START_EVENT = "CreateContractStartEvent";
    private static final String CREATE_CONTRACT_TASK = "CreateContractTask";


    private static final String ERROR_MESSAGE = "Error";

/*
    @Autowired
    private SecurityContext securityContext;

    @Before
    public void setUp() {
        super.setUp();
        when(securityContext.implies(Mockito.any(String.class))).thenReturn(Boolean.TRUE);
    }
*/

    @Test
    @Deployment(resources = CREATE_CLIENT_PROCESS_RESOURCE)
    public void canStartProcess() throws Exception {
        mockHandler(SaveContractAndClientDataTask.class);
        createProcessInstanceByKey(Definitions.CREATE_CONTRACT_PROCESS_KEY)
                .startAfterActivity(CREATE_CONTRACT_START_EVENT)
                .execute();
    }


    @Test
    @Deployment(resources = CREATE_CLIENT_PROCESS_RESOURCE)
    @Ignore //TODO исправить
    public void saveDraftSuccess() throws Exception {
        mockHandler(SaveContractAndClientDataTask.class);
        ProcessInstance processInstance = createProcessInstanceByKey(Definitions.CREATE_CONTRACT_PROCESS_KEY)
                .startAfterActivity("CreateContractTask")
                .execute();
        assertThat(processInstance).hasPassed("SaveContractAndClientDataTask");
        assertThat(processInstance).isEnded();
    }

/*
    @Test
    @Deployment(resources = CREATE_CLIENT_PROCESS_RESOURCE)
    public void setStatusMadeCheckHolderDataSuccess() throws Exception {
        mockSubprocess(Definitions.CHECK_CLIENT_SUBPROCESS_KEY);
        mockHandler(SaveContractAndClientDataTask.class);
        SetStatusInsuranceModel model = new SetStatusInsuranceModel();
        Client holderData = new Client();
        holderData.setBirthDate(LocalDate.now());
        holderData.setFirstName("firstName");
        holderData.setMiddleName("middleName");
        holderData.setSurName("surName");
        model.setHolderData(holderData);
        ProcessInstance processInstance = createProcessInstanceByKey(Definitions.CREATE_CONTRACT_PROCESS_KEY)
                .startAfterActivity(CREATE_CONTRACT_START_EVENT)
                .setVariable(ContextVariablesNames.TARGET_STATUS, InsuranceStatusCode.MADE)
                .setVariable(ContextVariablesNames.HOLDER_EQUALS_INSURED, Boolean.TRUE)
                //.setVariable(ContextVariablesNames.ERROR_MESSAGE, ERROR_MESSAGE)
                .setVariable(ContextVariablesNames.CONTRACT_DATA, TestUtils.convertObjectToJson(model))
                .execute();
        //assertThat(processInstance).isWaitingAt(CREATE_CONTRACT_TASK);
        assertThat(processInstance).isEnded();
    }
*/

}
