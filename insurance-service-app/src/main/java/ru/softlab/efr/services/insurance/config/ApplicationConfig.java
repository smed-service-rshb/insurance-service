package ru.softlab.efr.services.insurance.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jndi.JndiTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import ru.softlab.efr.clients.model.Constants;
import ru.softlab.efr.common.bpm.support.EnableBPM;
import ru.softlab.efr.common.settings.annotations.EnableSettings;
import ru.softlab.efr.common.utilities.hibernate.annotations.EnableHibernateJpa;
import ru.softlab.efr.infrastructure.logging.annotations.EnableLogging;
import ru.softlab.efr.infrastructure.transport.annotations.EnableTransport;
import ru.softlab.efr.services.authorization.annotations.EnablePermissionControl;

import javax.mail.Session;
import javax.naming.NamingException;


/**
 * Конф. класс приложения
 *
 * @author khudyakov
 * @since 28.07.2017
 */
@Configuration
@EnableBPM(serviceName = ApplicationConfig.APPLICATION_NAME, basePath = ApplicationConfig.APPLICATION_NAME)
@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan(
        basePackages = {
                "ru.softlab.efr.services.insurance",
                "ru.softlab.efr.services.auth",
                "ru.softlab.efr.common.client",
                "ru.softlab.efr.services.client"})

@EnableHibernateJpa(
        repositoryPackages = "ru.softlab.efr.services.insurance.repositories",
        modelPackages = "ru.softlab.efr.services.insurance.model.db",
        repositoryFactoryBeanClass = org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean.class
)
@EnablePermissionControl
@EnableTransport(ApplicationConfig.APPLICATION_NAME)
@EnableSettings(basePath = Constants.APPLICATION_PATH)
@EnableCaching
@EnableScheduling
@EnableLogging
@EnableAsync
public class ApplicationConfig {
    public static final String APPLICATION_NAME = "insurance-service";

    @Value("${application.mail.jndi}")
    private String mailJndi;


    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxUploadSize(52428800);
        multipartResolver.setMaxUploadSizePerFile(52428800);
        multipartResolver.setResolveLazily(false);
        return new CommonsMultipartResolver();
    }

    @Bean
    public JavaMailSenderImpl mailSender() throws NamingException {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setProtocol("smtp");
        javaMailSender.setSession(getMailSession());
        return javaMailSender;
    }

    private Session getMailSession() throws NamingException {
        JndiTemplate template = new JndiTemplate();
        return (Session) template.lookup(mailJndi);
    }

    @Bean(name = "extractThreadPoolTaskExecutor")
    public TaskExecutor extractThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.initialize();
        return executor;
    }

    @Bean(name = "clientCheckThreadPoolTaskExecutor")
    public TaskExecutor clientCheckThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.initialize();
        return executor;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setRetryHandler(new DefaultHttpRequestRetryHandler(2, true))
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(90000);
        return new RestTemplate(requestFactory);
    }

    @Bean(name = "pessimisticLockTaskExecutor")
    public TaskExecutor pessimisticLockTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(4);
        executor.initialize();
        return executor;
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