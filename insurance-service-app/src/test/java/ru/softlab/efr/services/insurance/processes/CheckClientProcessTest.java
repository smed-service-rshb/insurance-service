package ru.softlab.efr.services.insurance.processes;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.softlab.efr.services.insurance.config.TestProcessConfiguration;
import ru.softlab.efr.services.insurance.model.rest.FoundClient;
import ru.softlab.efr.services.insurance.process.Definitions;
import ru.softlab.efr.services.insurance.task.CheckClientIsBlockedTask;
import ru.softlab.efr.services.insurance.task.CheckTerroristTask;
import ru.softlab.efr.services.insurance.task.ClientSearchTask;
import ru.softlab.efr.services.insurance.utils.TestUtils;
import ru.softlab.efr.services.insurance.validation.ContextVariablesNames;
import ru.softlab.efr.test.common.bpm.BpmProcessTestBase;

import java.util.ArrayList;
import java.util.List;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareAssertions.assertThat;
import static ru.softlab.efr.services.insurance.stubs.TestData.CLIENT_1;
import static ru.softlab.efr.services.insurance.stubs.TestData.CLIENT_ENTITY_1;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestProcessConfiguration.class})
public class CheckClientProcessTest extends BpmProcessTestBase {
    private static final String CHECK_CLIENT_PROCESS_RESOURCE = "processes/check-client.bpmn20.xml";
    private static final String CHECK_CLIENT_START_EVENT = "CheckClientStartEvent";

    private static final String ERROR_MESSAGE = "Error";

    @Test
    @Deployment(resources = CHECK_CLIENT_PROCESS_RESOURCE)
    public void canStartProcess() throws Exception {
        mockHandler(ClientSearchTask.class);
        mockHandler(CheckTerroristTask.class);
        mockHandler(CheckClientIsBlockedTask.class);
        createProcessInstanceByKey(Definitions.CHECK_CLIENT_SUBPROCESS_KEY)
                .startAfterActivity(CHECK_CLIENT_START_EVENT)
                .setVariable(ContextVariablesNames.NEED_CHECK_PASSPORT_DATA, false)
                .execute();
    }


    @Test
    @Deployment(resources = CHECK_CLIENT_PROCESS_RESOURCE)
    public void saveDraftSuccess() throws Exception {
        List<FoundClient> clients = new ArrayList<>();
        clients.add(CLIENT_ENTITY_1.toFoundClient(null,"",""));
        mockHandler(ClientSearchTask.class);
        ProcessInstance processInstance = createProcessInstanceByKey(Definitions.CHECK_CLIENT_SUBPROCESS_KEY)
                .startAfterActivity(CHECK_CLIENT_START_EVENT)
                .setVariable(ContextVariablesNames.NEED_CHECK_PASSPORT_DATA, false)
                .setVariable(ContextVariablesNames.CLIENT_ID, null)
                .setVariable(ContextVariablesNames.CLIENT_DATA, TestUtils.convertObjectToJson(CLIENT_1))
                .setVariable(ContextVariablesNames.CLIENT_DECISION_DATA, TestUtils.convertObjectToJson(clients))
                .execute();
        assertThat(processInstance).isWaitingAt("ClientDecisionTask");
    }

}
