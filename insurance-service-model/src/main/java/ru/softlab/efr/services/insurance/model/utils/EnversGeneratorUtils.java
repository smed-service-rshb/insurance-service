package ru.softlab.efr.services.insurance.model.utils;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

import java.util.EnumSet;

/**
 * Генератор SQL файлов на основании entity
 */
public class EnversGeneratorUtils {

    public static void main(String[] args) {
        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder().applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect"); // dialect

        MetadataSources metadataSources = new MetadataSources(registryBuilder.build());

        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        new LocalSessionFactoryBuilder(null, resourceLoader, metadataSources).scanPackages("ru.softlab.efr.services.insurance.model.db");

        Metadata metadata = metadataSources.buildMetadata();

        new SchemaExport().setFormat(true).setOutputFile("export.sql").createOnly(EnumSet.of(TargetType.STDOUT, TargetType.SCRIPT), metadata);
    }
}
