
package ru.softlab.efr.services.insurance.model.client.unload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Сведения о документе, удостоверяющем личность
 * 
 * <p>Java class for ДокУдост complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ДокУдост"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ВидДокКод" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="2[1-8]|3[1-9]|4[0-2]"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ВидДокНаименование" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="СерияДок" type="{}Т50П" minOccurs="0"/&gt;
 *         &lt;element name="НомДок" type="{}Т50П" minOccurs="0"/&gt;
 *         &lt;element name="ДатВыдачиДок" type="{}ДатаТип" minOccurs="0"/&gt;
 *         &lt;element name="КемВыданДок" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="КодПодр" type="{}Т50П" minOccurs="0"/&gt;
 *         &lt;element name="ИноеНаименованиеДок" type="{}Т250" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "\u0414\u043e\u043a\u0423\u0434\u043e\u0441\u0442", propOrder = {
    "identityDocKindCode",
    "identityDocName",
    "identityDocSeries",
    "identityDocNumber",
    "identityDocIssuedDate",
    "identityDocIssuedBy",
    "identityDocDivisionCode",
    "identityDocAnotherName"
})
public class IdentityDocInfo {

    @XmlElement(name = "\u0412\u0438\u0434\u0414\u043e\u043a\u041a\u043e\u0434")
    protected String identityDocKindCode;
    @XmlElement(name = "\u0412\u0438\u0434\u0414\u043e\u043a\u041d\u0430\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435")
    protected String identityDocName;
    @XmlElement(name = "\u0421\u0435\u0440\u0438\u044f\u0414\u043e\u043a")
    protected String identityDocSeries;
    @XmlElement(name = "\u041d\u043e\u043c\u0414\u043e\u043a")
    protected String identityDocNumber;
    @XmlElement(name = "\u0414\u0430\u0442\u0412\u044b\u0434\u0430\u0447\u0438\u0414\u043e\u043a")
    protected String identityDocIssuedDate;
    @XmlElement(name = "\u041a\u0435\u043c\u0412\u044b\u0434\u0430\u043d\u0414\u043e\u043a")
    protected String identityDocIssuedBy;
    @XmlElement(name = "\u041a\u043e\u0434\u041f\u043e\u0434\u0440")
    protected String identityDocDivisionCode;
    @XmlElement(name = "\u0418\u043d\u043e\u0435\u041d\u0430\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435\u0414\u043e\u043a")
    protected String identityDocAnotherName;

    /**
     * Gets the value of the identityDocKindCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentityDocKindCode() {
        return identityDocKindCode;
    }

    /**
     * Sets the value of the identityDocKindCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentityDocKindCode(String value) {
        this.identityDocKindCode = value;
    }

    /**
     * Gets the value of the identityDocName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentityDocName() {
        return identityDocName;
    }

    /**
     * Sets the value of the identityDocName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentityDocName(String value) {
        this.identityDocName = value;
    }

    /**
     * Gets the value of the identityDocSeries property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentityDocSeries() {
        return identityDocSeries;
    }

    /**
     * Sets the value of the identityDocSeries property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentityDocSeries(String value) {
        this.identityDocSeries = value;
    }

    /**
     * Gets the value of the identityDocNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentityDocNumber() {
        return identityDocNumber;
    }

    /**
     * Sets the value of the identityDocNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentityDocNumber(String value) {
        this.identityDocNumber = value;
    }

    /**
     * Gets the value of the identityDocIssuedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentityDocIssuedDate() {
        return identityDocIssuedDate;
    }

    /**
     * Sets the value of the identityDocIssuedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentityDocIssuedDate(String value) {
        this.identityDocIssuedDate = value;
    }

    /**
     * Gets the value of the identityDocIssuedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentityDocIssuedBy() {
        return identityDocIssuedBy;
    }

    /**
     * Sets the value of the identityDocIssuedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentityDocIssuedBy(String value) {
        this.identityDocIssuedBy = value;
    }

    /**
     * Gets the value of the identityDocDivisionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentityDocDivisionCode() {
        return identityDocDivisionCode;
    }

    /**
     * Sets the value of the identityDocDivisionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentityDocDivisionCode(String value) {
        this.identityDocDivisionCode = value;
    }

    /**
     * Gets the value of the identityDocAnotherName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentityDocAnotherName() {
        return identityDocAnotherName;
    }

    /**
     * Sets the value of the identityDocAnotherName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentityDocAnotherName(String value) {
        this.identityDocAnotherName = value;
    }

}
