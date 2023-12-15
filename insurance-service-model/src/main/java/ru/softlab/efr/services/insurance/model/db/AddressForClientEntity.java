package ru.softlab.efr.services.insurance.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.springframework.util.StringUtils;
import ru.softlab.efr.services.insurance.model.enums.AddressTypeEnum;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;
import ru.softlab.efr.services.insurance.model.rest.Address;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.StringJoiner;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * адрес для заявки по созданию/редактированию клиента
 * @author basharin
 * @since 09.02.2018
 */
@Entity
@Table(name = "addresses_for_client")
@Audited
public class AddressForClientEntity {

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

    @Column(name = "registration_period_start")
    private LocalDate registrationPeriodStart; // дата регистрации

    @Column(name = "registration_period_end")
    private LocalDate registrationPeriodEnd; // дата окончания регистрации

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id")
    protected ClientEntity client;

    public AddressForClientEntity() {}

    /**
     * Конструктор
     * @param address адрес
     * @param client клиент
     */
    public AddressForClientEntity(Address address, ClientEntity client) {
        update(address);
        this.client = client;
    }

    /**
     * Получить id адреса клиента
     * @return id адреса клиента
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить тип адреса клиента
     * @return тип адреса клиента
     */
    public AddressTypeEnum getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressTypeEnum addressType) {
        this.addressType = addressType;
    }

    /**
     * Получить страну клиента
     * @return страну клиента
     */
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Получить регион клиента
     * @return регион клиента
     */
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Получить район клиента
     * @return район клиента
     */
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    /**
     * Получить город клиента
     * @return город клиента
     */
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Получить населенный пункт клиента
     * @return населенный пункт клиента
     */
    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    /**
     * Получить улицу клиента
     * @return улица клиента
     */
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Получить номер дома клиента
     * @return номер дома клиента
     */
    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    /**
     * Получить номер строения клиента
     * @return номер строения клиента
     */
    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }

    /**
     * Получить корпус клиента
     * @return корпус клиента
     */
    public String getHousing() {
        return housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }

    /**
     * Получить номер квартиры клиента
     * @return номер квартиры клиента
     */
    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    /**
     * Получить почтовый индекс клиента
     * @return почтовый индекс клиента
     */
    public String getIndex() {
        return postIndex;
    }

    public void setIndex(String index) {
        this.postIndex = index;
    }

    /**
     * Получить дата регистрации клиента
     * @return дата регистрации клиента
     */
    public LocalDate getRegistrationPeriodStart() {
        return registrationPeriodStart;
    }

    public void setRegistrationPeriodStart(LocalDate registrationPeriodStart) {
        this.registrationPeriodStart = registrationPeriodStart;
    }

    /**
     * Получить дата окончания регистрации клиента
     * @return дата окончания регистрации клиента
     */
    public LocalDate getRegistrationPeriodEnd() {
        return registrationPeriodEnd;
    }

    public void setRegistrationPeriodEnd(LocalDate registrationPeriodEnd) {
        this.registrationPeriodEnd = registrationPeriodEnd;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getOkato() {
        return okato;
    }

    public void setOkato(String okato) {
        this.okato = okato;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    /**
     * Обновить информацию об адресе
     * @param address адрес
     */
    public final void update(Address address) {
        this.addressType = address.getAddressType() != null ? AddressTypeEnum.valueOf(address.getAddressType().name()) : null;
        this.country = address.getCountry();
        this.countryCode = address.getCountryCode();
        this.region = address.getRegion();
        this.okato = address.getOkato();
        this.area = address.getArea();
        this.city = address.getCity();
        this.locality = address.getLocality();
        this.street = address.getStreet();
        this.house = address.getHouse();
        this.construction = address.getConstruction();
        this.housing = address.getHousing();
        this.apartment = address.getApartment();
        this.postIndex = address.getIndex();
        this.registrationPeriodStart = address.getRegistrationPeriodStart();
        this.registrationPeriodEnd = address.getRegistrationPeriodEnd();
    }

    /**
     * Конветрация в объекта класса {@link Address}
     * @param entity адрес
     * @return адрес
     */
    public static Address toAddress(AddressForClientEntity entity) {
        Address address = new Address();
        address.setAddressType(entity.getAddressType() != null ? ru.softlab.efr.services.insurance.model.rest.AddressType.valueOf(entity.getAddressType().name()): null);
        address.setCountry(entity.getCountry());
        address.setCountryCode(entity.getCountryCode());
        address.setRegion(entity.getRegion());
        address.setOkato(entity.getOkato());
        address.setArea(entity.getArea());
        address.setCity(entity.getCity());
        address.setLocality(entity.getLocality());
        address.setStreet(entity.getStreet());
        address.setHouse(entity.getHouse());
        address.setConstruction(entity.getConstruction());
        address.setHousing(entity.getHousing());
        address.setApartment(entity.getApartment());
        address.setIndex(entity.getIndex());
        address.setRegistrationPeriodStart(entity.getRegistrationPeriodStart());
        address.setRegistrationPeriodEnd(entity.getRegistrationPeriodEnd());
        address.setFullAddress(entity.getAddress());
        return address;
    }
    public static AddressForClientEntity toAddressForClientEntity(AddressForClientShortData entity){
        AddressForClientEntity address = new AddressForClientEntity();
        address.setCountry(entity.getCountry());
        address.setCountryCode(entity.getCountryCode());
        address.setRegion(entity.getRegion());
        address.setOkato(entity.getOkato());
        address.setArea(entity.getArea());
        address.setCity(entity.getCity());
        address.setLocality(entity.getLocality());
        address.setStreet(entity.getStreet());
        address.setHouse(entity.getHouse());
        address.setConstruction(entity.getConstruction());
        address.setHousing(entity.getHousing());
        address.setApartment(entity.getApartment());
        address.setIndex(entity.getPostIndex());
        return address;
    }

    public String asString() {
        StringBuilder sb = new StringBuilder("");
        if (!StringUtils.isEmpty(this.getCountry())) sb.append(this.getCountry()).append(" ");
        if (!StringUtils.isEmpty(this.getRegion())) sb.append(this.getRegion()).append(" ");
        if (!StringUtils.isEmpty(this.getArea())) sb.append(this.getArea()).append(" ");
        if (!StringUtils.isEmpty(this.getCity())) sb.append("г.").append(this.getCity()).append(" ");
        if (!StringUtils.isEmpty(this.getLocality())) sb.append(this.getLocality()).append(" ");
        if (!StringUtils.isEmpty(this.getStreet())) sb.append("ул.").append(this.getStreet()).append(" ");
        if (!StringUtils.isEmpty(this.getHouse())) sb.append("д.").append(this.getHouse()).append(" ");
        if (!StringUtils.isEmpty(this.getConstruction())) sb.append(this.getConstruction()).append(" ");
        if (!StringUtils.isEmpty(this.getApartment())) sb.append(this.getApartment()).append(" ");
        return sb.toString();
    }

    @Transient
    public String getAddress() {
        String delimiter = ", ";
        StringJoiner joiner = new StringJoiner(delimiter);

        if (isNotBlank(this.getIndex())) {
            joiner.add(this.getIndex());
        }
        if (isNotBlank(ReportableContract.getCountryNameByCode(ensureNotNull(this.getCountry())))) {
            joiner.add(ReportableContract.getCountryNameByCode(ensureNotNull(this.getCountry())));
        }
        if (isNotBlank(this.getRegion())) {
            joiner.add(this.getRegion());
        }
        if (isNotBlank(this.getArea())) {
            joiner.add(this.getArea());
        }
        if (isNotBlank(this.getCity())) {
            joiner.add("г. " + this.getCity());
        }
        if (isNotBlank(this.getLocality())) {
            joiner.add(this.getLocality());
        }
        if (isNotBlank(this.getStreet())) {
            joiner.add(this.getStreet());
        }
        if (isNotBlank(this.getHouse())) {
            joiner.add("д. " + this.getHouse());
        }
        if (isNotBlank(this.getConstruction())) {
            joiner.add("строение " + this.getConstruction());
        }
        if (isNotBlank(this.getHousing())) {
            joiner.add("корпус " + this.getHousing());
        }
        if (isNotBlank(this.getApartment())) {
            joiner.add("кв. " + this.getApartment());
        }
        return joiner.toString();
    }

    public static String ensureNotNull(String inputString) {
        return isNotBlank(inputString) ? inputString : "(не указано)";
    }

}
