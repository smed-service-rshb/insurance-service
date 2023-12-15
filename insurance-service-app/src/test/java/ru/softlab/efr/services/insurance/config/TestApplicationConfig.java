package ru.softlab.efr.services.insurance.config;

import net.ttddyy.dsproxy.listener.DataSourceQueryCountListener;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import ru.softlab.efr.clients.model.Constants;
import ru.softlab.efr.common.client.*;
import ru.softlab.efr.common.settings.annotations.EnableSettings;
import ru.softlab.efr.common.utilities.hibernate.annotations.EnableHibernateJpa;
import ru.softlab.efr.infrastructure.logging.annotations.EnableLogging;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.services.OperationLogService;
import ru.softlab.efr.infrastructure.transport.annotations.EnableTransport;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.auth.OrgUnitAuthServiceClient;
import ru.softlab.efr.services.auth.OrgUnitClient;
import ru.softlab.efr.services.authorization.annotations.EnablePermissionControl;
import ru.softlab.efr.services.insurance.stubs.*;
import ru.softlab.efr.services.insurance.utils.OperationLogServiceStatistics;

import javax.sql.DataSource;
import java.util.Calendar;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

/**
 * Конфигурационный класс запуска тестов
 *
 * @author khudyakov
 * @since 25.07.2017
 */
@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan(
        basePackages = {
                "ru.softlab.efr.services.insurance",
                "ru.softlab.efr.services.auth",
                "ru.softlab.efr.common.client"},
        excludeFilters = @ComponentScan.Filter(
                value = Configuration.class,
                type = ANNOTATION
        ))
@EnablePermissionControl
@Import({WebMvcConfig.class})
@EnableHibernateJpa(
        repositoryPackages = {
                "ru.softlab.efr.services.insurance.repositories",
                "ru.softlab.efr.services.authorization.repositories"},
        modelPackages = {
                "ru.softlab.efr.services.insurance.model.db",
                "ru.softlab.efr.services.authorization.model"},
        repositoryFactoryBeanClass = org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean.class)
@EnableTransport(ApplicationConfig.APPLICATION_NAME)
@EnableSettings(basePath = Constants.APPLICATION_PATH)
@EnableCaching
@EnableLogging
@EnableAsync
public class TestApplicationConfig {
    private static final String APPLICATION_NAME = "insurance-service";

    @Bean
    @Primary
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        ProxyDataSource proxyDataSource = new ProxyDataSource(builder
                .setType(EmbeddedDatabaseType.H2)
                .setName(APPLICATION_NAME)
                .build());
        proxyDataSource.addListener(new DataSourceQueryCountListener());
        return proxyDataSource;
    }

    @Bean
    @Primary
    @Autowired
    public OrgUnitAuthServiceClient orgUnitAuthServiceClient(MicroServiceTemplate microServiceTemplate) {
        return new OrgUnitAuthServiceClientStub(microServiceTemplate);
    }

    @Bean
    @Primary
    @Autowired
    public CurrenciesClient currenciesClient(MicroServiceTemplate microServiceTemplate) {
        return new CurrenciesClientStub(microServiceTemplate);
    }

    @Bean
    @Primary
    @Autowired
    public PrintTemplatesClient printTemplatesClient(MicroServiceTemplate microServiceTemplate) {
        return new PrintTemplatesClientStub(microServiceTemplate);
    }

    @Bean
    @Primary
    @Autowired
    public EmployeesClient employeesClient(MicroServiceTemplate microServiceTemplate) {
        return new EmployeesClientStub(microServiceTemplate);
    }

    @Bean
    @Primary
    @Autowired
    public OrgUnitClient orgUnitClient(MicroServiceTemplate microServiceTemplate) {
        return new OrgUnitClientStub(microServiceTemplate);
    }

    @Bean
    @Primary
    @Autowired
    public CurrencyRateClient currencyRateClient(MicroServiceTemplate microServiceTemplate) {
        return new CurrencyRateClientStub(microServiceTemplate);
    }

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setProtocol("smtp");
        javaMailSender.setPort(3025);
        return javaMailSender;
    }

    @Bean
    public OperationLogServiceStatistics operationLogServiceStatistics() {
        return new OperationLogServiceStatistics();
    }

    @Bean
    @Primary
    public OperationLogService operationLogService(OperationLogServiceStatistics operationLogServiceStatistics) {
        return new OperationLogService() {
            @Override
            public OperationLogEntry startLogging() {
                OperationLogEntry operationLogEntry = new OperationLogEntry();
                operationLogEntry.setLogDate(Calendar.getInstance());
                return operationLogEntry;
            }

            @Override
            public void log(OperationLogEntry operationLogEntry) {
                operationLogServiceStatistics.append(operationLogEntry);
            }
        };
    }

    @Bean(name = "extractThreadPoolTaskExecutor")
    public TaskExecutor extractThreadPoolTaskExecutor() {
        // для тестов вызов всех асинхронных методов формирования отчётов будет выполняться в том же потоке
        return new SyncTaskExecutor();
    }

    @Bean(name = "clientCheckThreadPoolTaskExecutor")
    public TaskExecutor clientCheckThreadPoolTaskExecutor() {
        // для тестов вызов всех асинхронных методов проверки клиентов будет выполняться в том же потоке
        return new SyncTaskExecutor();
    }

    @Bean(name = "pessimisticLockTaskExecutor")
    public TaskExecutor pessimisticLockTaskExecutor() {
        // для тестов вызов методов, связанных со взятием пессиместических блокировок, будет выполняться в отдельном потоке
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(1);
        executor.initialize();
        return executor;
    }

    @Bean
    @Primary
    @Autowired
    public BlockagesClient blockagesClient(MicroServiceTemplate microServiceTemplate) {
        return new BlockagesClientStub(microServiceTemplate);
    }

    @Bean
    @Primary
    @Autowired
    public InvalidIdentityDocsClient invalidIdentityDocsClient(MicroServiceTemplate microServiceTemplate) {
        return new InvalidIdentityDocsClientStub(microServiceTemplate);
    }

    @Bean
    @Primary
    @Autowired
    public TerroristsClient terroristsClient(MicroServiceTemplate microServiceTemplate) {
        return new TerroristsClientStub(microServiceTemplate);
    }

    @Bean
    @Primary
    @Autowired
    public DictStatusClient dictStatusClient(MicroServiceTemplate microServiceTemplate) {
        return new DictStatusClientStub(microServiceTemplate);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setCharacterEncoding("UTF-8");
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}