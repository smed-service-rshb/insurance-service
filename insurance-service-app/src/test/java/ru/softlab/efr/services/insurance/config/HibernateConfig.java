package ru.softlab.efr.services.insurance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Конф. класс hibernate
 *
 * @author khudyakov
 * @since 28.07.2017
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories({
        "ru.softlab.efr.services.insurance.repositories",
        "ru.softlab.efr.services.services.authorization.repositories"}
    )
public class HibernateConfig {

    private static final String[] HIBERNATE_PACKAGES = {"ru.softlab.efr.services.insurance.model.db", "ru.softlab.efr.services.services.authorization.model"};

    @Autowired
    private Environment env;

    /**
     * Получить bean источника данных (к БД)
     * @return bean источника данных
     * @throws ClassNotFoundException
     */
    @Bean
    @Primary
    public DataSource dataSource() throws ClassNotFoundException {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(env.getProperty("hibernate.connection.driver_class"));
        driver.setUrl(env.getProperty("hibernate.connection.url"));
        driver.setUsername(env.getProperty("hibernate.connection.username"));
        driver.setPassword(env.getProperty("hibernate.connection.password"));
        return driver;
    }

    /**
     * Получить bean фабрики управления
     * @param dataSource источник данных
     * @return bean фабрики управления
     * @throws IOException
     */
    @SuppressWarnings("Duplicates")
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) throws IOException {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(HIBERNATE_PACKAGES);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(jpaPropertiesFactoryBean().getObject());
        return entityManagerFactoryBean;
    }

    /**
     * Получить bean настроек
     * @return bean настроек
     */
    @Bean
    public PropertiesFactoryBean jpaPropertiesFactoryBean(){
        PropertiesFactoryBean propertiesFactory = new PropertiesFactoryBean();
        propertiesFactory.setLocation(new ClassPathResource("hibernate.properties"));
        return propertiesFactory;
    }

    /**
     * Получить bean менеджера транзакций
     * @param entityManagerFactory фабрика управления
     * @return bean менеджера транзакций
     */
    @Bean
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }
}
