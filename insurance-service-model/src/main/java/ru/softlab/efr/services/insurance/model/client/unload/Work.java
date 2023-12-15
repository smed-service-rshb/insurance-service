
package ru.softlab.efr.services.insurance.model.client.unload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Сведения о лицензии на право осуществления деятельности, подлежащей лицензированию.
 * 
 * <p>Java class for СведенияРабота complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="СведенияРабота"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ДолжностьКлиента" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="НименованиеРаботодатель" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="АдрРаботодатель" type="{}Адрес2" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "\u0421\u0432\u0435\u0434\u0435\u043d\u0438\u044f\u0420\u0430\u0431\u043e\u0442\u0430", propOrder = {
    "position",
    "employerName",
    "employerAddress"
})
public class Work {

    @XmlElement(name = "\u0414\u043e\u043b\u0436\u043d\u043e\u0441\u0442\u044c\u041a\u043b\u0438\u0435\u043d\u0442\u0430")
    protected String position;
    @XmlElement(name = "\u041d\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435\u0420\u0430\u0431\u043e\u0442\u043e\u0434\u0430\u0442\u0435\u043b\u044c")
    protected String employerName;
    @XmlElement(name = "\u0410\u0434\u0440\u0420\u0430\u0431\u043e\u0442\u043e\u0434\u0430\u0442\u0435\u043b\u044c")
    protected Address2 employerAddress;

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosition(String value) {
        this.position = value;
    }

    /**
     * Gets the value of the employerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployerName() {
        return employerName;
    }

    /**
     * Sets the value of the employerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployerName(String value) {
        this.employerName = value;
    }

    /**
     * Gets the value of the employerAddress property.
     * 
     * @return
     *     possible object is
     *     {@link Address2 }
     *     
     */
    public Address2 getEmployerAddress() {
        return employerAddress;
    }

    /**
     * Sets the value of the employerAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link Address2 }
     *     
     */
    public void setEmployerAddress(Address2 value) {
        this.employerAddress = value;
    }

}
