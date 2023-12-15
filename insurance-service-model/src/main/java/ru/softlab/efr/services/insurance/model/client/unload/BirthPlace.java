
package ru.softlab.efr.services.insurance.model.client.unload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Сведения о месте рождения
 * 
 * <p>Java class for МестоРождения complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="МестоРождения"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="КодОКСМ" type="{}ОКСМТип" minOccurs="0"/&gt;
 *         &lt;element name="КодСубъектаПоОКАТО" type="{}КодСубъектаПоОКАТОТип" minOccurs="0"/&gt;
 *         &lt;element name="Район" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="Пункт" type="{}Т250" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "\u041c\u0435\u0441\u0442\u043e\u0420\u043e\u0436\u0434\u0435\u043d\u0438\u044f", propOrder = {
    "oksmCode",
    "entityOkatoCode",
    "area",
    "point"
})
public class BirthPlace {

    @XmlElement(name = "\u041a\u043e\u0434\u041e\u041a\u0421\u041c")
    protected String oksmCode;
    @XmlElement(name = "\u041a\u043e\u0434\u0421\u0443\u0431\u044a\u0435\u043a\u0442\u0430\u041f\u043e\u041e\u041a\u0410\u0422\u041e")
    protected String entityOkatoCode;
    @XmlElement(name = "\u0420\u0430\u0439\u043e\u043d")
    protected String area;
    @XmlElement(name = "\u041f\u0443\u043d\u043a\u0442")
    protected String point;

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

}
