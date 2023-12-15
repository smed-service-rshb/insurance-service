
package ru.softlab.efr.services.insurance.model.client.unload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Сведения о лицензии на право осуществления деятельности, подлежащей лицензированию.
 * 
 * <p>Java class for УчредительИНБОЮЛ complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="УчредительИНБОЮЛ"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ТипУчредителя" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="1|2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="НаимУчредитель" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice&gt;
 *                   &lt;element name="ЮрЛицо" type="{}Т500"/&gt;
 *                   &lt;element name="ФизЛицо" type="{}ФИО"/&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="СоставИмущества" type="{}Т2000" minOccurs="0"/&gt;
 *         &lt;element name="АдресУчредитель" type="{}Адрес2" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "\u0423\u0447\u0440\u0435\u0434\u0438\u0442\u0435\u043b\u044c\u0418\u041d\u0411\u041e\u042e\u041b", propOrder = {
    "founderType",
    "founderName",
    "goodsStruct",
    "founderAddress"
})
public class Founder {

    @XmlElement(name = "\u0422\u0438\u043f\u0423\u0447\u0440\u0435\u0434\u0438\u0442\u0435\u043b\u044f")
    protected String founderType;
    @XmlElement(name = "\u041d\u0430\u0438\u043c\u0423\u0447\u0440\u0435\u0434\u0438\u0442\u0435\u043b\u044c")
    protected Founder.FounderName founderName;
    @XmlElement(name = "\u0421\u043e\u0441\u0442\u0430\u0432\u0418\u043c\u0443\u0449\u0435\u0441\u0442\u0432\u0430")
    protected String goodsStruct;
    @XmlElement(name = "\u0410\u0434\u0440\u0435\u0441\u0423\u0447\u0440\u0435\u0434\u0438\u0442\u0435\u043b\u044c")
    protected Address2 founderAddress;

    /**
     * Gets the value of the founderType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFounderType() {
        return founderType;
    }

    /**
     * Sets the value of the founderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFounderType(String value) {
        this.founderType = value;
    }

    /**
     * Gets the value of the founderName property.
     * 
     * @return
     *     possible object is
     *     {@link Founder.FounderName }
     *     
     */
    public Founder.FounderName getFounderName() {
        return founderName;
    }

    /**
     * Sets the value of the founderName property.
     * 
     * @param value
     *     allowed object is
     *     {@link Founder.FounderName }
     *     
     */
    public void setFounderName(Founder.FounderName value) {
        this.founderName = value;
    }

    /**
     * Gets the value of the goodsStruct property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGoodsStruct() {
        return goodsStruct;
    }

    /**
     * Sets the value of the goodsStruct property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGoodsStruct(String value) {
        this.goodsStruct = value;
    }

    /**
     * Gets the value of the founderAddress property.
     * 
     * @return
     *     possible object is
     *     {@link Address2 }
     *     
     */
    public Address2 getFounderAddress() {
        return founderAddress;
    }

    /**
     * Sets the value of the founderAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link Address2 }
     *     
     */
    public void setFounderAddress(Address2 value) {
        this.founderAddress = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;choice&gt;
     *         &lt;element name="ЮрЛицо" type="{}Т500"/&gt;
     *         &lt;element name="ФизЛицо" type="{}ФИО"/&gt;
     *       &lt;/choice&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "companyName",
        "individualCustomerName"
    })
    public static class FounderName {

        @XmlElement(name = "\u042e\u0440\u041b\u0438\u0446\u043e")
        protected String companyName;
        @XmlElement(name = "\u0424\u0438\u0437\u041b\u0438\u0446\u043e")
        protected FullName individualCustomerName;

        /**
         * Gets the value of the companyName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCompanyName() {
            return companyName;
        }

        /**
         * Sets the value of the companyName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCompanyName(String value) {
            this.companyName = value;
        }

        /**
         * Gets the value of the individualCustomerName property.
         * 
         * @return
         *     possible object is
         *     {@link FullName }
         *     
         */
        public FullName getIndividualCustomerName() {
            return individualCustomerName;
        }

        /**
         * Sets the value of the individualCustomerName property.
         * 
         * @param value
         *     allowed object is
         *     {@link FullName }
         *     
         */
        public void setIndividualCustomerName(FullName value) {
            this.individualCustomerName = value;
        }

    }

}
