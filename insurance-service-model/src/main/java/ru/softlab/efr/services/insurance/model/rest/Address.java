package ru.softlab.efr.services.insurance.model.rest;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import ru.softlab.efr.services.insurance.model.rest.AddressType;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Адрес
 */
@ApiModel(description = "Адрес")
@Validated
public class Address   {
    @JsonProperty("addressType")
    private AddressType addressType = null;

    @JsonProperty("country")
    private String country = null;

    @JsonProperty("countryCode")
    private String countryCode = null;

    @JsonProperty("region")
    private String region = null;

    @JsonProperty("okato")
    private String okato = null;

    @JsonProperty("area")
    private String area = null;

    @JsonProperty("city")
    private String city = null;

    @JsonProperty("locality")
    private String locality = null;

    @JsonProperty("street")
    private String street = null;

    @JsonProperty("house")
    private String house = null;

    @JsonProperty("construction")
    private String construction = null;

    @JsonProperty("housing")
    private String housing = null;

    @JsonProperty("apartment")
    private String apartment = null;

    @JsonProperty("index")
    private String index = null;

    @JsonProperty("registrationPeriodStart")
    private LocalDate registrationPeriodStart = null;

    @JsonProperty("registrationPeriodEnd")
    private LocalDate registrationPeriodEnd = null;

    @JsonProperty("fullAddress")
    private String fullAddress = null;


    /**
     * Создает пустой экземпляр класса
     */
    public Address() {}

    /**
     * Создает экземпляр класса
     * @param addressType Тип адреса
     * @param country Страна
     * @param countryCode Код страны по классификатору ОКСМО
     * @param region Регион
     * @param okato Код ОКАТО региона
     * @param area Район
     * @param city Город
     * @param locality Населенный пункт
     * @param street Улица
     * @param house Номер дома
     * @param construction Строение
     * @param housing Корпус
     * @param apartment Номер квартиры
     * @param index Индекс
     * @param registrationPeriodStart Дата регистрации
     * @param registrationPeriodEnd Дата окончания регистрации
     * @param fullAddress Полный адрес (одной строкой)
     */
    public Address(AddressType addressType, String country, String countryCode, String region, String okato, String area, String city, String locality, String street, String house, String construction, String housing, String apartment, String index, LocalDate registrationPeriodStart, LocalDate registrationPeriodEnd, String fullAddress) {
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
        this.index = index;
        this.registrationPeriodStart = registrationPeriodStart;
        this.registrationPeriodEnd = registrationPeriodEnd;
        this.fullAddress = fullAddress;
    }

    /**
     * Тип адреса
    * @return Тип адреса
    **/
    @ApiModelProperty(value = "Тип адреса")
    
  @Valid


    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }


    /**
     * Страна
    * @return Страна
    **/
    @ApiModelProperty(value = "Страна")
    
 @Size(min=1,max=50)

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    /**
     * Код страны по классификатору ОКСМО
    * @return Код страны по классификатору ОКСМО
    **/
    @ApiModelProperty(value = "Код страны по классификатору ОКСМО")
    


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    /**
     * Регион
    * @return Регион
    **/
    @ApiModelProperty(value = "Регион")
    
 @Pattern(regexp="([a-zA-Z0-9\\-')(\\s\\\\/.]{1,150}|[а-яА-ЯёЁ0-9\\-')(\\s\\\\/.]{1,150})")

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    /**
     * Код ОКАТО региона
    * @return Код ОКАТО региона
    **/
    @ApiModelProperty(value = "Код ОКАТО региона")
    


    public String getOkato() {
        return okato;
    }

    public void setOkato(String okato) {
        this.okato = okato;
    }


    /**
     * Район
    * @return Район
    **/
    @ApiModelProperty(value = "Район")
    
 @Pattern(regexp="([a-zA-Z0-9\\-')(\\s\\\\/.]{1,150}|[а-яА-ЯёЁ0-9\\-')(\\s\\\\/.]{1,150})")

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    /**
     * Город
    * @return Город
    **/
    @ApiModelProperty(value = "Город")
    
 @Pattern(regexp="([a-zA-Z0-9\\-')(\\s\\\\/.]{1,150}|[а-яА-ЯёЁ0-9\\-')(\\s\\\\/.]{1,150})")

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    /**
     * Населенный пункт
    * @return Населенный пункт
    **/
    @ApiModelProperty(value = "Населенный пункт")
    
 @Pattern(regexp="([a-zA-Z0-9\\-')(\\s\\\\/.]{1,100}|[а-яА-ЯёЁ0-9\\-')(\\s\\\\/.]{1,100})")

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }


    /**
     * Улица
    * @return Улица
    **/
    @ApiModelProperty(value = "Улица")
    
 @Pattern(regexp="([a-zA-Z0-9\\-')(\\s\\\\/.]{1,150}|[а-яА-ЯёЁ0-9\\-')(\\s\\\\/.]{1,150})")

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


    /**
     * Номер дома
    * @return Номер дома
    **/
    @ApiModelProperty(value = "Номер дома")
    
 @Size(min=1,max=10)

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }


    /**
     * Строение
    * @return Строение
    **/
    @ApiModelProperty(value = "Строение")
    
 @Size(max=10)

    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }


    /**
     * Корпус
    * @return Корпус
    **/
    @ApiModelProperty(value = "Корпус")
    
 @Size(max=10)

    public String getHousing() {
        return housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }


    /**
     * Номер квартиры
    * @return Номер квартиры
    **/
    @ApiModelProperty(value = "Номер квартиры")
    
 @Size(min=1,max=10)

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }


    /**
     * Индекс
    * @return Индекс
    **/
    @ApiModelProperty(value = "Индекс")
    


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }


    /**
     * Дата регистрации
    * @return Дата регистрации
    **/
    @ApiModelProperty(value = "Дата регистрации")
    
  @Valid


    public LocalDate getRegistrationPeriodStart() {
        return registrationPeriodStart;
    }

    public void setRegistrationPeriodStart(LocalDate registrationPeriodStart) {
        this.registrationPeriodStart = registrationPeriodStart;
    }


    /**
     * Дата окончания регистрации
    * @return Дата окончания регистрации
    **/
    @ApiModelProperty(value = "Дата окончания регистрации")
    
  @Valid


    public LocalDate getRegistrationPeriodEnd() {
        return registrationPeriodEnd;
    }

    public void setRegistrationPeriodEnd(LocalDate registrationPeriodEnd) {
        this.registrationPeriodEnd = registrationPeriodEnd;
    }


    /**
     * Полный адрес (одной строкой)
    * @return Полный адрес (одной строкой)
    **/
    @ApiModelProperty(value = "Полный адрес (одной строкой)")
    


    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(this.addressType, address.addressType) &&
            Objects.equals(this.country, address.country) &&
            Objects.equals(this.countryCode, address.countryCode) &&
            Objects.equals(this.region, address.region) &&
            Objects.equals(this.okato, address.okato) &&
            Objects.equals(this.area, address.area) &&
            Objects.equals(this.city, address.city) &&
            Objects.equals(this.locality, address.locality) &&
            Objects.equals(this.street, address.street) &&
            Objects.equals(this.house, address.house) &&
            Objects.equals(this.construction, address.construction) &&
            Objects.equals(this.housing, address.housing) &&
            Objects.equals(this.apartment, address.apartment) &&
            Objects.equals(this.index, address.index) &&
            Objects.equals(this.registrationPeriodStart, address.registrationPeriodStart) &&
            Objects.equals(this.registrationPeriodEnd, address.registrationPeriodEnd) &&
            Objects.equals(this.fullAddress, address.fullAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressType, country, countryCode, region, okato, area, city, locality, street, house, construction, housing, apartment, index, registrationPeriodStart, registrationPeriodEnd, fullAddress);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Address {\n");
        
        sb.append("    addressType: ").append(toIndentedString(addressType)).append("\n");
        sb.append("    country: ").append(toIndentedString(country)).append("\n");
        sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
        sb.append("    region: ").append(toIndentedString(region)).append("\n");
        sb.append("    okato: ").append(toIndentedString(okato)).append("\n");
        sb.append("    area: ").append(toIndentedString(area)).append("\n");
        sb.append("    city: ").append(toIndentedString(city)).append("\n");
        sb.append("    locality: ").append(toIndentedString(locality)).append("\n");
        sb.append("    street: ").append(toIndentedString(street)).append("\n");
        sb.append("    house: ").append(toIndentedString(house)).append("\n");
        sb.append("    construction: ").append(toIndentedString(construction)).append("\n");
        sb.append("    housing: ").append(toIndentedString(housing)).append("\n");
        sb.append("    apartment: ").append(toIndentedString(apartment)).append("\n");
        sb.append("    index: ").append(toIndentedString(index)).append("\n");
        sb.append("    registrationPeriodStart: ").append(toIndentedString(registrationPeriodStart)).append("\n");
        sb.append("    registrationPeriodEnd: ").append(toIndentedString(registrationPeriodEnd)).append("\n");
        sb.append("    fullAddress: ").append(toIndentedString(fullAddress)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
          return "null";
        }
        return o.toString().replace("\n", "\n    ");
        }
    }

