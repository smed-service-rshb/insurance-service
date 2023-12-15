
package ru.softlab.efr.services.insurance.model.client.unload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Сведения о лицензии на право осуществления деятельности, подлежащей лицензированию.
 * 
 * <p>Java class for СведенияЛицензия complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="СведенияЛицензия"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ВидЛицензии" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="НомерЛицензии" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="ДатаВыдачиЛицензии" type="{}ДатаТип" minOccurs="0"/&gt;
 *         &lt;element name="КемВыданаЛицензия" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="НачалоДействияЛицензии" type="{}ДатаТип" minOccurs="0"/&gt;
 *         &lt;element name="КонецДействияЛицензии" type="{}ДатаТип" minOccurs="0"/&gt;
 *         &lt;element name="ПереченьВидовЛицДеят" type="{}Т500" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "\u0421\u0432\u0435\u0434\u0435\u043d\u0438\u044f\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u044f", propOrder = {
    "licenseKind",
    "licenseNumber",
    "licenseIssuedDate",
    "licenseIssuedBy",
    "licenseStartDate",
    "licenseEndDate",
    "licenseActivityKindList"
})
public class LicenseInfo {

    @XmlElement(name = "\u0412\u0438\u0434\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u0438")
    protected String licenseKind;
    @XmlElement(name = "\u041d\u043e\u043c\u0435\u0440\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u0438")
    protected String licenseNumber;
    @XmlElement(name = "\u0414\u0430\u0442\u0430\u0412\u044b\u0434\u0430\u0447\u0438\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u0438")
    protected String licenseIssuedDate;
    @XmlElement(name = "\u041a\u0435\u043c\u0412\u044b\u0434\u0430\u043d\u0430\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u044f")
    protected String licenseIssuedBy;
    @XmlElement(name = "\u041d\u0430\u0447\u0430\u043b\u043e\u0414\u0435\u0439\u0441\u0442\u0432\u0438\u044f\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u0438")
    protected String licenseStartDate;
    @XmlElement(name = "\u041a\u043e\u043d\u0435\u0446\u0414\u0435\u0439\u0441\u0442\u0432\u0438\u044f\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u0438")
    protected String licenseEndDate;
    @XmlElement(name = "\u041f\u0435\u0440\u0435\u0447\u0435\u043d\u044c\u0412\u0438\u0434\u043e\u0432\u041b\u0438\u0446\u0414\u0435\u044f\u0442")
    protected String licenseActivityKindList;

    /**
     * Gets the value of the licenseKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseKind() {
        return licenseKind;
    }

    /**
     * Sets the value of the licenseKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseKind(String value) {
        this.licenseKind = value;
    }

    /**
     * Gets the value of the licenseNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseNumber() {
        return licenseNumber;
    }

    /**
     * Sets the value of the licenseNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseNumber(String value) {
        this.licenseNumber = value;
    }

    /**
     * Gets the value of the licenseIssuedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseIssuedDate() {
        return licenseIssuedDate;
    }

    /**
     * Sets the value of the licenseIssuedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseIssuedDate(String value) {
        this.licenseIssuedDate = value;
    }

    /**
     * Gets the value of the licenseIssuedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseIssuedBy() {
        return licenseIssuedBy;
    }

    /**
     * Sets the value of the licenseIssuedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseIssuedBy(String value) {
        this.licenseIssuedBy = value;
    }

    /**
     * Gets the value of the licenseStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseStartDate() {
        return licenseStartDate;
    }

    /**
     * Sets the value of the licenseStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseStartDate(String value) {
        this.licenseStartDate = value;
    }

    /**
     * Gets the value of the licenseEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseEndDate() {
        return licenseEndDate;
    }

    /**
     * Sets the value of the licenseEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseEndDate(String value) {
        this.licenseEndDate = value;
    }

    /**
     * Gets the value of the licenseActivityKindList property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseActivityKindList() {
        return licenseActivityKindList;
    }

    /**
     * Sets the value of the licenseActivityKindList property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseActivityKindList(String value) {
        this.licenseActivityKindList = value;
    }

}
