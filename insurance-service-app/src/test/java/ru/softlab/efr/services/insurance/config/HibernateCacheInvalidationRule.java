package ru.softlab.efr.services.insurance.config;

import org.hibernate.Cache;
import org.hibernate.SessionFactory;
import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;

/**
 * Правило для инвалидации кеша втрого уровня Hibernate после выполнения каждого теста.
 * Данное правило надо добавлять в каждый класс с тестами, где создаётся spring context и тесты
 * выполняют взаимодействие с БД через hibernate.
 *
 * @author Andrey Grigorov
 */
@Service
public class HibernateCacheInvalidationRule extends ExternalResource {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public HibernateCacheInvalidationRule(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    protected void after() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Cache cache = sessionFactory.getCache();
        cache.evictQueryRegions();
        cache.evictDefaultQueryRegion();
        cache.evictCollectionRegions();
        cache.evictEntityRegions();
    }
}