package ru.softlab.efr.services.insurance.model.db;


import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.softlab.efr.services.insurance.model.enums.AddressTypeEnum;

import javax.persistence.*;

@Entity
@Table(name = "addresses_for_client")
public class AddressForClientShortData {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id записи

    @Column(name = "address_type")
    @Enumerated(EnumType.STRING)
    private AddressTypeEnum addressType;

    @Column
    private String country; // страна

    @Column(name = "country_code")
    private String countryCode; // Код страны по классификатору ОКСМО

    @Column
    private String region; // регион

    @Column
    private String okato; // Код ОКАТО региона

    @Column
    private String area; // район

    @Column
    private String city; //город

    @Column
    private String locality; // населенный пункт

    @Column
    private String street; // улица

    @Column
    private String house; // номер дома

    @Column
    private String construction; // номер строения

    @Column
    private String housing; // корпус

    @Column
    private String apartment; // номер квартиры

    @Column(name = "index")
    private String postIndex; // почтовый индекс

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    protected ClientEntity client;


    public AddressForClientShortData() {
    }

    public AddressForClientShortData(AddressTypeEnum addressType, String country, String countryCode, String region, String okato, String area, String city, String locality, String street, String house, String construction, String housing, String apartment, String postIndex) {
        this.addressType = addressType;
        this.country = country;
        this.countryCode = countryCode;
        this.region = region;
        this.okato = okato;
        this.area = area;
        this.city = city;
        this.locality = locality;
        this.street = street;
        this.house = house;
        this.construction = construction;
        this.housing = housing;
        this.apartment = apartment;
        this.postIndex = postIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AddressTypeEnum getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressTypeEnum addressType) {
        this.addressType = addressType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOkato() {
        return okato;
    }

    public void setOkato(String okato) {
        this.okato = okato;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }

    public String getHousing() {
        return housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getPostIndex() {
        return postIndex;
    }

    public void setPostIndex(String postIndex) {
        this.postIndex = postIndex;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }
}
