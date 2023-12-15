
package ru.softlab.efr.services.insurance.model.client.unload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Сведения об адресе
 * 
 * <p>Java class for Адрес2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Адрес2"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="КодОКСМ" type="{}ОКСМТип" minOccurs="0"/&gt;
 *         &lt;element name="СтранаНаименование" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="КодСубъектаПоОКАТО" type="{}КодСубъектаПоОКАТОТип" minOccurs="0"/&gt;
 *         &lt;element name="Район" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="Пункт" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="Улица" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="Дом" type="{}Т60" minOccurs="0"/&gt;
 *         &lt;element name="Корп" type="{}Т20" minOccurs="0"/&gt;
 *         &lt;element name="Оф" type="{}Т20" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "\u0410\u0434\u0440\u0435\u04412", propOrder = {
    "oksmCode",
    "countryName",
    "entityOkatoCode",
    "area",
    "point",
    "street",
    "house",
    "building",
    "office"
})
public class Address2 {

    @XmlElement(name = "\u041a\u043e\u0434\u041e\u041a\u0421\u041c")
    protected String oksmCode;
    @XmlElement(name = "\u0421\u0442\u0440\u0430\u043d\u0430\u041d\u0430\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435")
    protected String countryName;
    @XmlElement(name = "\u041a\u043e\u0434\u0421\u0443\u0431\u044a\u0435\u043a\u0442\u0430\u041f\u043e\u041e\u041a\u0410\u0422\u041e")
    protected String entityOkatoCode;
    @XmlElement(name = "\u0420\u0430\u0439\u043e\u043d")
    protected String area;
    @XmlElement(name = "\u041f\u0443\u043d\u043a\u0442")
    protected String point;
    @XmlElement(name = "\u0423\u043b\u0438\u0446\u0430")
    protected String street;
    @XmlElement(name = "\u0414\u043e\u043c")
    protected String house;
    @XmlElement(name = "\u041a\u043e\u0440\u043f")
    protected String building;
    @XmlElement(name = "\u041e\u0444")
    protected String office;

    /**
     * Gets the value of the oksmCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOksmCode() {
        return oksmCode;
    }

    /**
     * Sets the value of the oksmCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOksmCode(String value) {
        this.oksmCode = value;
    }

    /**
     * Gets the value of the countryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the value of the countryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryName(String value) {
        this.countryName = value;
    }

    /**
     * Gets the value of the entityOkatoCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntityOkatoCode() {
        return entityOkatoCode;
    }

    /**
     * Sets the value of the entityOkatoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityOkatoCode(String value) {
        this.entityOkatoCode = value;
    }

    /**
     * Gets the value of the area property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArea() {
        return area;
    }

    /**
     * Sets the value of the area property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArea(String value) {
        this.area = value;
    }

    /**
     * Gets the value of the point property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoint() {
        return point;
    }

    /**
     * Sets the value of the point property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoint(String value) {
        this.point = value;
    }

    /**
     * Gets the value of the street property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the value of the street property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreet(String value) {
        this.street = value;
    }

    /**
     * Gets the value of the house property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHouse() {
        return house;
    }

    /**
     * Sets the value of the house property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHouse(String value) {
        this.house = value;
    }

    /**
     * Gets the value of the building property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuilding() {
        return building;
    }

    /**
     * Sets the value of the building property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuilding(String value) {
        this.building = value;
    }

    /**
     * Gets the value of the office property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOffice() {
        return office;
    }

    /**
     * Sets the value of the office property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOffice(String value) {
        this.office = value;
    }

}
