package ru.softlab.efr.services.insurance.config;

import org.springframework.context.annotation.*;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import ru.softlab.efr.clients.model.Constants;
import ru.softlab.efr.clients.model.rest.ShowErrorRequest;
import ru.softlab.efr.common.settings.annotations.EnableSettings;
import ru.softlab.efr.common.utilities.hibernate.annotations.EnableHibernateJpa;
import ru.softlab.efr.infrastructure.logging.annotations.EnableLogging;
import ru.softlab.efr.infrastructure.transport.annotations.EnableTransport;
import ru.softlab.efr.services.auth.EmployeesManageAuthServiceClient;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.authorization.SecurityContext;
import ru.softlab.efr.services.authorization.SecurityContextFactory;
import ru.softlab.efr.services.authorization.repositories.PermissionRepository;
import ru.softlab.efr.services.authorization.services.PermissionStoreService;
import ru.softlab.efr.test.common.bpm.BpmProcessTestConfig;

import static org.mockito.Mockito.mock;
import static org.springframework.context.annotation.FilterType.ANNOTATION;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@ComponentScan(
        basePackages = {
                "ru.softlab.efr.services.insurance",
                "ru.softlab.efr.services.auth",
                "ru.softlab.efr.common.client"
                },
        excludeFilters = {@ComponentScan.Filter(
                value = Configuration.class,
                type = ANNOTATION
        ),
                @ComponentScan.Filter(
                        value = ru.softlab.efr.common.bpm.support.controllers.GlobalExceptionHandler.class,
                        type = ASSIGNABLE_TYPE
                )
        })
@Import({WebMvcConfig.class})
@EnableHibernateJpa(
        repositoryPackages = {
                "ru.softlab.efr.services.insurance.repositories",
                "ru.softlab.efr.services.authorization.repositories"},
        modelPackages = {
                "ru.softlab.efr.services.insurance.model.db",
                "ru.softlab.efr.services.authorization.model"},
        repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class
                )
@EnableSettings(basePath = Constants.APPLICATION_PATH)
@EnableTransport(ApplicationConfig.APPLICATION_NAME)
@EnableLogging
@Configuration
public class TestProcessConfiguration extends BpmProcessTestConfig {
    @Bean
    @Primary
    public ShowErrorRequest getShowErrorRequest(){
        return new ShowErrorRequest();
    }

    /**
     * данные принципала
     * @return данные принципала
     */
    @Bean
    @Primary
    public PrincipalDataSource principalDataSource() {
        return mock(PrincipalDataSource.class);
    }

    /**
     * репозиторий с правами
     * @return репозиторий с правами
     */
    @Bean
    @Primary
    public PermissionRepository permissionRepository() {
        return mock(PermissionRepository.class);
    }

    /**
     * хранилище с правами
     * @return хранилище с правами
     */
    @Bean
    @Primary
    public PermissionStoreService permissionStoreService() {
        return mock(PermissionStoreService.class);
    }

    /**
     * установка бина с контекста для проверки прав
     * @return контекст для проверки прав
     */
    @Bean
    @Primary
    public SecurityContext securityContext() {
        SecurityContext mock = mock(SecurityContext.class);
        //Mockito.when(mock.implies(Mockito.any()))
        return mock;
    }

    /**
     * установка бина с фабрикой контекста для проверки прав
     * @return фабрика для контекст для проверки прав
     */
    @Bean
    @Primary
    public SecurityContextFactory securityContextFactory(SecurityContext securityContext) {
        return new SecurityContextFactory(null) {
            @Override
            public SecurityContext createContext(PrincipalDataSource principalDataSource) {
                return securityContext;
            }
        };
    }

    @Bean
    @Primary
    public EmployeesManageAuthServiceClient employeesManageAuthServiceClient() {
        return mock(EmployeesManageAuthServiceClient.class);
    }

/*
    @Bean
    @Primary
    public GlobalExceptionHandler employeesManageAuthServiceClient() {
        return mock(EmployeesManageAuthServiceClient.class);
    }
*/

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setProtocol("smtp");
        javaMailSender.setPort(3025);
        return javaMailSender;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(new ClassLoaderTemplateResolver());
        return templateEngine;
    }

}