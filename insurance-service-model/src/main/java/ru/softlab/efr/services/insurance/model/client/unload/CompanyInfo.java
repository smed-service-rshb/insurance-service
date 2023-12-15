
package ru.softlab.efr.services.insurance.model.client.unload;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * Сведения о юридическом лице
 * 
 * <p>Java class for СведенияЮЛ complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="СведенияЮЛ"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ПолнНаимЮЛ" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="КратНаимЮЛ" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="ИностранноеНаимЮЛ" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="ОКОПФЮЛ" type="{}ОКОПФЮЛТип" minOccurs="0"/&gt;
 *         &lt;element name="ИННЮЛ" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[0-9]{10}|[0-9]{5}"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="КППЮЛ" type="{}КППТип" minOccurs="0"/&gt;
 *         &lt;element name="ОКПОЮЛ" type="{}ОКПОЮЛТип" minOccurs="0"/&gt;
 *         &lt;element name="ОКВЭДЮЛ" type="{}ОКВЭДТип" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ОГРН" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{}ОГРНТип"&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="НаименРегОрганаЮл" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="ДатаРегЮл" type="{}ДатаТип" minOccurs="0"/&gt;
 *         &lt;element name="ПризнСтратег" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="0|1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ОКВЭД2ЮЛ" type="{}ОКВЭДТип" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="БИК" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{}БИКТип"&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Лицензия" type="{}СведенияЛицензия" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "\u0421\u0432\u0435\u0434\u0435\u043d\u0438\u044f\u042e\u041b", propOrder = {
    "companyFullName",
    "companyShortName",
    "companyEngName",
    "okopf",
    "inn",
    "kpp",
    "okpo",
    "okved",
    "ogrn",
    "regAgencyName",
    "regDate",
    "isStrategicEconomicSociety",
    "okved2",
    "bic",
    "license"
})
public class CompanyInfo {

    @XmlElement(name = "\u041f\u043e\u043b\u043d\u041d\u0430\u0438\u043c\u042e\u041b")
    protected String companyFullName;
    @XmlElement(name = "\u041a\u0440\u0430\u0442\u041d\u0430\u0438\u043c\u042e\u041b")
    protected String companyShortName;
    @XmlElement(name = "\u0418\u043d\u043e\u0441\u0442\u0440\u0430\u043d\u043d\u043e\u0435\u041d\u0430\u0438\u043c\u042e\u041b")
    protected String companyEngName;
    @XmlElement(name = "\u041e\u041a\u041e\u041f\u0424\u042e\u041b")
    protected String okopf;
    @XmlElement(name = "\u0418\u041d\u041d\u042e\u041b")
    protected String inn;
    @XmlElement(name = "\u041a\u041f\u041f\u042e\u041b")
    protected String kpp;
    @XmlElement(name = "\u041e\u041a\u041f\u041e\u042e\u041b")
    protected String okpo;
    @XmlElement(name = "\u041e\u041a\u0412\u042d\u0414\u042e\u041b")
    protected List<String> okved;
    @XmlElement(name = "\u041e\u0413\u0420\u041d")
    protected String ogrn;
    @XmlElement(name = "\u041d\u0430\u0438\u043c\u0435\u043d\u0420\u0435\u0433\u041e\u0440\u0433\u0430\u043d\u0430\u042e\u043b")
    protected String regAgencyName;
    @XmlElement(name = "\u0414\u0430\u0442\u0430\u0420\u0435\u0433\u042e\u043b")
    protected String regDate;
    @XmlElement(name = "\u041f\u0440\u0438\u0437\u043d\u0421\u0442\u0440\u0430\u0442\u0435\u0433")
    protected String isStrategicEconomicSociety;
    @XmlElement(name = "\u041e\u041a\u0412\u042d\u04142\u042e\u041b")
    protected List<String> okved2;
    @XmlElement(name = "\u0411\u0418\u041a")
    protected String bic;
    @XmlElementRef(name = "\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u044f", type = CompanyInfo.License.class, required = false)
    protected List<CompanyInfo.License> license;

    /**
     * Gets the value of the companyFullName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyFullName() {
        return companyFullName;
    }

    /**
     * Sets the value of the companyFullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyFullName(String value) {
        this.companyFullName = value;
    }

    /**
     * Gets the value of the companyShortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyShortName() {
        return companyShortName;
    }

    /**
     * Sets the value of the companyShortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyShortName(String value) {
        this.companyShortName = value;
    }

    /**
     * Gets the value of the companyEngName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyEngName() {
        return companyEngName;
    }

    /**
     * Sets the value of the companyEngName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyEngName(String value) {
        this.companyEngName = value;
    }

    /**
     * Gets the value of the okopf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOkopf() {
        return okopf;
    }

    /**
     * Sets the value of the okopf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOkopf(String value) {
        this.okopf = value;
    }

    /**
     * Gets the value of the inn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInn() {
        return inn;
    }

    /**
     * Sets the value of the inn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInn(String value) {
        this.inn = value;
    }

    /**
     * Gets the value of the kpp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKpp() {
        return kpp;
    }

    /**
     * Sets the value of the kpp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKpp(String value) {
        this.kpp = value;
    }

    /**
     * Gets the value of the okpo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOkpo() {
        return okpo;
    }

    /**
     * Sets the value of the okpo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOkpo(String value) {
        this.okpo = value;
    }

    /**
     * Gets the value of the okved property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the okved property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOkved().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOkved() {
        if (okved == null) {
            okved = new ArrayList<String>();
        }
        return this.okved;
    }

    /**
     * Gets the value of the ogrn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOgrn() {
        return ogrn;
    }

    /**
     * Sets the value of the ogrn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOgrn(String value) {
        this.ogrn = value;
    }

    /**
     * Gets the value of the regAgencyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegAgencyName() {
        return regAgencyName;
    }

    /**
     * Sets the value of the regAgencyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegAgencyName(String value) {
        this.regAgencyName = value;
    }

    /**
     * Gets the value of the regDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegDate() {
        return regDate;
    }

    /**
     * Sets the value of the regDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegDate(String value) {
        this.regDate = value;
    }

    /**
     * Gets the value of the isStrategicEconomicSociety property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsStrategicEconomicSociety() {
        return isStrategicEconomicSociety;
    }

    /**
     * Sets the value of the isStrategicEconomicSociety property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsStrategicEconomicSociety(String value) {
        this.isStrategicEconomicSociety = value;
    }

    /**
     * Gets the value of the okved2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the okved2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOkved2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOkved2() {
        if (okved2 == null) {
            okved2 = new ArrayList<String>();
        }
        return this.okved2;
    }

    /**
     * Gets the value of the bic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBic() {
        return bic;
    }

    /**
     * Sets the value of the bic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBic(String value) {
        this.bic = value;
    }

    /**
     * Gets the value of the license property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the license property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLicense().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CompanyInfo.License }
     * 
     * 
     */
    public List<CompanyInfo.License> getLicense() {
        if (license == null) {
            license = new ArrayList<CompanyInfo.License>();
        }
        return this.license;
    }

    public static class License
        extends JAXBElement<LicenseInfo>
    {

        protected final static QName NAME = new QName("", "\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u044f");

        public License(LicenseInfo value) {
            super(NAME, ((Class) LicenseInfo.class), CompanyInfo.class, value);
        }

        public License() {
            super(NAME, ((Class) LicenseInfo.class), CompanyInfo.class, null);
        }

    }

}
