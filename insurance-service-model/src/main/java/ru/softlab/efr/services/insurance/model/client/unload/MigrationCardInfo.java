
package ru.softlab.efr.services.insurance.model.client.unload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Сведения о документе, удостоверяющем личность
 * 
 * <p>Java class for ДокКарта complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ДокКарта"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="СерияДок" type="{}Т50П" minOccurs="0"/&gt;
 *         &lt;element name="НомДок" type="{}Т50П" minOccurs="0"/&gt;
 *         &lt;element name="ДатаНачала" type="{}ДатаТип" minOccurs="0"/&gt;
 *         &lt;element name="ДатаОкончания" type="{}ДатаТип" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "\u0414\u043e\u043a\u041a\u0430\u0440\u0442\u0430", propOrder = {
    "series",
    "number",
    "startDate",
    "endDate"
})
public class MigrationCardInfo {

    @XmlElement(name = "\u0421\u0435\u0440\u0438\u044f\u0414\u043e\u043a")
    protected String series;
    @XmlElement(name = "\u041d\u043e\u043c\u0414\u043e\u043a")
    protected String number;
    @XmlElement(name = "\u0414\u0430\u0442\u0430\u041d\u0430\u0447\u0430\u043b\u0430")
    protected String startDate;
    @XmlElement(name = "\u0414\u0430\u0442\u0430\u041e\u043a\u043e\u043d\u0447\u0430\u043d\u0438\u044f")
    protected String endDate;

    /**
     * Gets the value of the series property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeries() {
        return series;
    }

    /**
     * Sets the value of the series property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeries(String value) {
        this.series = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDate(String value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndDate(String value) {
        this.endDate = value;
    }

}
