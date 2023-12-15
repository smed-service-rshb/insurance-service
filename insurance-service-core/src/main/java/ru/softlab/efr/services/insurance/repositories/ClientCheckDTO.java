package ru.softlab.efr.services.insurance.repositories;

import ru.softlab.efr.services.insurance.model.db.AddressForClientShortData;
import ru.softlab.efr.services.insurance.model.enums.AddressTypeEnum;

import java.time.LocalDate;

public class ClientCheckDTO {

    private Long id;
    private String surname;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private AddressForClientShortData addresses;

    public ClientCheckDTO(Long id, String surname, String firstName, String middleName, LocalDate birthDate, AddressTypeEnum addressType,
                          String country, String countryCode, String region, String okato, String area, String city, String locality,
                          String street, String house, String construction, String housing, String apartment, String postIndex) {
        this.id = id;
        this.surname = surname;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.addresses = new AddressForClientShortData();
        addresses.setAddressType(addressType);
        addresses.setCountry(country);
        addresses.setCountryCode(countryCode);
        addresses.setRegion(region);
        addresses.setOkato(okato);
        addresses.setArea(area);
        addresses.setCity(city);
        addresses.setLocality(locality);
        addresses.setStreet(street);
        addresses.setHouse(house);
        addresses.setConstruction(construction);
        addresses.setHousing(housing);
        addresses.setApartment(apartment);
        addresses.setPostIndex(postIndex);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public AddressForClientShortData getAddresses() {
        return addresses;
    }

    public void setAddresses(AddressForClientShortData addresses) {
        this.addresses = addresses;
    }
}
