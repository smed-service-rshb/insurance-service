package ru.softlab.efr.services.insurance.services;

/**
 * Сервис работы со справочником стран
 *
 * @author olshansky
 * @since 16.01.2019
 */
public interface CountryService {
    String getOKSMCodeByCountryName(String countryName);
}
