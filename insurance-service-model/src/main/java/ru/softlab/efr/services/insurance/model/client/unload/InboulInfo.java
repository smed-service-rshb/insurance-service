
package ru.softlab.efr.services.insurance.model.client.unload;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Сведения о ИНБОЮЛ
 * 
 * <p>Java class for СведенияИНБОЮЛ complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="СведенияИНБОЮЛ"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ПолнНаимИНБОЮЛ" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{}Т500"&gt;
 *               &lt;maxLength value="500"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="КратНаимИНБОЮЛ" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="ИностранноеНаимИНБОЮЛ" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="ОКОПФИНБОЮЛ" type="{}ОКОПФЮЛТип" minOccurs="0"/&gt;
 *         &lt;element name="ОКВЭДИНБОЮЛ" type="{}ОКВЭДТип" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ОКВЭД2ИНБОЮЛ" type="{}ОКВЭДТип" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="КодИНБОЮЛ" type="{}Т50" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="НомерИНБОЮЛ" type="{}Т50" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ПризнакОргФормаИНБОЮЛ" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[0-4]"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Учредитель" type="{}УчредительИНБОЮЛ" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "\u0421\u0432\u0435\u0434\u0435\u043d\u0438\u044f\u0418\u041d\u0411\u041e\u042e\u041b", propOrder = {
    "inboulFullName",
    "inboulShortName",
    "inboulEngName",
    "inboulOKOPF",
    "inboulOKVED",
    "inboul2OKVED",
    "inboulCode",
    "inboulNumber",
    "inboulOrgFormFlag",
    "founder"
})
public class InboulInfo {

    @XmlElement(name = "\u041f\u043e\u043b\u043d\u041d\u0430\u0438\u043c\u0418\u041d\u0411\u041e\u042e\u041b")
    protected String inboulFullName;
    @XmlElement(name = "\u041a\u0440\u0430\u0442\u041d\u0430\u0438\u043c\u0418\u041d\u0411\u041e\u042e\u041b")
    protected String inboulShortName;
    @XmlElement(name = "\u0418\u043d\u043e\u0441\u0442\u0440\u0430\u043d\u043d\u043e\u0435\u041d\u0430\u0438\u043c\u0418\u041d\u0411\u041e\u042e\u041b")
    protected String inboulEngName;
    @XmlElement(name = "\u041e\u041a\u041e\u041f\u0424\u0418\u041d\u0411\u041e\u042e\u041b")
    protected String inboulOKOPF;
    @XmlElement(name = "\u041e\u041a\u0412\u042d\u0414\u0418\u041d\u0411\u041e\u042e\u041b")
    protected List<String> inboulOKVED;
    @XmlElement(name = "\u041e\u041a\u0412\u042d\u04142\u0418\u041d\u0411\u041e\u042e\u041b")
    protected List<String> inboul2OKVED;
    @XmlElement(name = "\u041a\u043e\u0434\u0418\u041d\u0411\u041e\u042e\u041b")
    protected List<String> inboulCode;
    @XmlElement(name = "\u041d\u043e\u043c\u0435\u0440\u0418\u041d\u0411\u041e\u042e\u041b")
    protected List<String> inboulNumber;
    @XmlElement(name = "\u041f\u0440\u0438\u0437\u043d\u0430\u043a\u041e\u0440\u0433\u0424\u043e\u0440\u043c\u0430\u0418\u041d\u0411\u041e\u042e\u041b")
    protected String inboulOrgFormFlag;
    @XmlElement(name = "\u0423\u0447\u0440\u0435\u0434\u0438\u0442\u0435\u043b\u044c")
    protected List<Founder> founder;

    /**
     * Gets the value of the inboulFullName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboulFullName() {
        return inboulFullName;
    }

    /**
     * Sets the value of the inboulFullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboulFullName(String value) {
        this.inboulFullName = value;
    }

    /**
     * Gets the value of the inboulShortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboulShortName() {
        return inboulShortName;
    }

    /**
     * Sets the value of the inboulShortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboulShortName(String value) {
        this.inboulShortName = value;
    }

    /**
     * Gets the value of the inboulEngName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboulEngName() {
        return inboulEngName;
    }

    /**
     * Sets the value of the inboulEngName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboulEngName(String value) {
        this.inboulEngName = value;
    }

    /**
     * Gets the value of the inboulOKOPF property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboulOKOPF() {
        return inboulOKOPF;
    }

    /**
     * Sets the value of the inboulOKOPF property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboulOKOPF(String value) {
        this.inboulOKOPF = value;
    }

    /**
     * Gets the value of the inboulOKVED property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inboulOKVED property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInboulOKVED().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getInboulOKVED() {
        if (inboulOKVED == null) {
            inboulOKVED = new ArrayList<String>();
        }
        return this.inboulOKVED;
    }

    /**
     * Gets the value of the inboul2OKVED property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inboul2OKVED property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInboul2OKVED().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getInboul2OKVED() {
        if (inboul2OKVED == null) {
            inboul2OKVED = new ArrayList<String>();
        }
        return this.inboul2OKVED;
    }

    /**
     * Gets the value of the inboulCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inboulCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInboulCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getInboulCode() {
        if (inboulCode == null) {
            inboulCode = new ArrayList<String>();
        }
        return this.inboulCode;
    }

    /**
     * Gets the value of the inboulNumber property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inboulNumber property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInboulNumber().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getInboulNumber() {
        if (inboulNumber == null) {
            inboulNumber = new ArrayList<String>();
        }
        return this.inboulNumber;
    }

    /**
     * Gets the value of the inboulOrgFormFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboulOrgFormFlag() {
        return inboulOrgFormFlag;
    }

    /**
     * Sets the value of the inboulOrgFormFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboulOrgFormFlag(String value) {
        this.inboulOrgFormFlag = value;
    }

    /**
     * Gets the value of the founder property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the founder property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFounder().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Founder }
     * 
     * 
     */
    public List<Founder> getFounder() {
        if (founder == null) {
            founder = new ArrayList<Founder>();
        }
        return this.founder;
    }

}
