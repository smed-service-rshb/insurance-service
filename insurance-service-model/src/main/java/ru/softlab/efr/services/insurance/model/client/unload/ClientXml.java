
package ru.softlab.efr.services.insurance.model.client.unload;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Сведения о клиенте
 * 
 * <p>Java class for СведКлиент element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="СведКлиент"&gt;
 *   &lt;complexType&gt;
 *     &lt;complexContent&gt;
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="ИнфКлиент" maxOccurs="unbounded"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                   &lt;sequence&gt;
 *                     &lt;element name="КлиентАктив" minOccurs="0"&gt;
 *                       &lt;simpleType&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                           &lt;pattern value="0|1"/&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/simpleType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="ДатаИдент" type="{}ДатаТип" minOccurs="0"/&gt;
 *                     &lt;element name="ТипКлиента" minOccurs="0"&gt;
 *                       &lt;simpleType&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                           &lt;pattern value="1|2|3|4|5"/&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/simpleType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="ПризнакРезидент" minOccurs="0"&gt;
 *                       &lt;simpleType&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                           &lt;pattern value="0|1"/&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/simpleType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="ПризнакКонтрагент" minOccurs="0"&gt;
 *                       &lt;simpleType&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                           &lt;pattern value="0|1"/&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/simpleType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="СведОрг"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;choice&gt;
 *                               &lt;element name="СведенияЮЛ" type="{}СведенияЮЛ"/&gt;
 *                               &lt;element name="СведенияФЛИП" type="{}СведенияФЛИП"/&gt;
 *                               &lt;element name="СведенияИНБОЮЛ" type="{}СведенияИНБОЮЛ"/&gt;
 *                             &lt;/choice&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="Телефон" type="{}ТелефонТип" minOccurs="0"/&gt;
 *                     &lt;element name="АдрРег" type="{}Адрес" minOccurs="0"/&gt;
 *                     &lt;element name="АдрПреб" type="{}Адрес" minOccurs="0"/&gt;
 *                     &lt;element name="ПризнакИдентКлиента" minOccurs="0"&gt;
 *                       &lt;simpleType&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                           &lt;pattern value="[0-2]"/&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/simpleType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="КодОснМер" minOccurs="0"&gt;
 *                       &lt;simpleType&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                           &lt;pattern value="1|2"/&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/simpleType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="ДатаПеречня" type="{}ДатаТип" minOccurs="0"/&gt;
 *                     &lt;element name="НомерПеречня" type="{}Т250" minOccurs="0"/&gt;
 *                     &lt;element name="НомерЗаписиПеречень" type="{}Т250" minOccurs="0"/&gt;
 *                     &lt;element name="ДатаРешения" type="{}ДатаТип" minOccurs="0"/&gt;
 *                     &lt;element name="НомерРешения" type="{}Т250" minOccurs="0"/&gt;
 *                     &lt;element name="ДатаРезультат" type="{}ДатаТип" minOccurs="0"/&gt;
 *                     &lt;element name="ДатаНачалоОтн" type="{}ДатаТип" minOccurs="0"/&gt;
 *                     &lt;element name="ДатаЗаполнения" type="{}ДатаТип" minOccurs="0"/&gt;
 *                     &lt;element name="ИнаяИнф" type="{}Т2000" minOccurs="0"/&gt;
 *                     &lt;element name="ИнфСтепеньРиск" type="{}Т2000" minOccurs="0"/&gt;
 *                     &lt;element name="ПаспортВалид" minOccurs="0"&gt;
 *                       &lt;simpleType&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                           &lt;pattern value="1|0"/&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/simpleType&gt;
 *                     &lt;/element&gt;
 *                     &lt;element name="ИнфЦельОтношения" type="{}Т2000" minOccurs="0"/&gt;
 *                     &lt;element name="ИнфХарактерОтношения" type="{}Т2000" minOccurs="0"/&gt;
 *                     &lt;element name="ИнфЦельФХД" type="{}Т2000" minOccurs="0"/&gt;
 *                     &lt;element name="ИнфРепутация" type="{}Т2000" minOccurs="0"/&gt;
 *                     &lt;element name="ИнфФинансы" type="{}Т2000" minOccurs="0"/&gt;
 *                     &lt;element name="ИнфПроисхождениеДеньги" type="{}Т2000" minOccurs="0"/&gt;
 *                     &lt;element name="ФИОСотрудника" type="{}ФИО" minOccurs="0"/&gt;
 *                     &lt;element name="ДолжностьСотрудника" type="{}Т250" minOccurs="0"/&gt;
 *                     &lt;element name="СтепеньРиска" minOccurs="0"&gt;
 *                       &lt;simpleType&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                           &lt;pattern value="1|2|3|4"/&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/simpleType&gt;
 *                     &lt;/element&gt;
 *                   &lt;/sequence&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *         &lt;/sequence&gt;
 *       &lt;/restriction&gt;
 *     &lt;/complexContent&gt;
 *   &lt;/complexType&gt;
 * &lt;/element&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "clientInfo"
})
@XmlRootElement(name = "\u0421\u0432\u0435\u0434\u041a\u043b\u0438\u0435\u043d\u0442")
public class ClientXml {

    @XmlElement(name = "\u0418\u043d\u0444\u041a\u043b\u0438\u0435\u043d\u0442", required = true)
    protected List<ClientXml.ClientInfo> clientInfo;

    /**
     * Gets the value of the clientInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clientInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClientInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClientXml.ClientInfo }
     * 
     * 
     */
    public List<ClientXml.ClientInfo> getClientInfo() {
        if (clientInfo == null) {
            clientInfo = new ArrayList<ClientXml.ClientInfo>();
        }
        return this.clientInfo;
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
     *       &lt;sequence&gt;
     *         &lt;element name="КлиентАктив" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="0|1"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ДатаИдент" type="{}ДатаТип" minOccurs="0"/&gt;
     *         &lt;element name="ТипКлиента" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="1|2|3|4|5"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ПризнакРезидент" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="0|1"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ПризнакКонтрагент" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="0|1"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="СведОрг"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;choice&gt;
     *                   &lt;element name="СведенияЮЛ" type="{}СведенияЮЛ"/&gt;
     *                   &lt;element name="СведенияФЛИП" type="{}СведенияФЛИП"/&gt;
     *                   &lt;element name="СведенияИНБОЮЛ" type="{}СведенияИНБОЮЛ"/&gt;
     *                 &lt;/choice&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="Телефон" type="{}ТелефонТип" minOccurs="0"/&gt;
     *         &lt;element name="АдрРег" type="{}Адрес" minOccurs="0"/&gt;
     *         &lt;element name="АдрПреб" type="{}Адрес" minOccurs="0"/&gt;
     *         &lt;element name="ПризнакИдентКлиента" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="[0-2]"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="КодОснМер" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="1|2"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ДатаПеречня" type="{}ДатаТип" minOccurs="0"/&gt;
     *         &lt;element name="НомерПеречня" type="{}Т250" minOccurs="0"/&gt;
     *         &lt;element name="НомерЗаписиПеречень" type="{}Т250" minOccurs="0"/&gt;
     *         &lt;element name="ДатаРешения" type="{}ДатаТип" minOccurs="0"/&gt;
     *         &lt;element name="НомерРешения" type="{}Т250" minOccurs="0"/&gt;
     *         &lt;element name="ДатаРезультат" type="{}ДатаТип" minOccurs="0"/&gt;
     *         &lt;element name="ДатаНачалоОтн" type="{}ДатаТип" minOccurs="0"/&gt;
     *         &lt;element name="ДатаЗаполнения" type="{}ДатаТип" minOccurs="0"/&gt;
     *         &lt;element name="ИнаяИнф" type="{}Т2000" minOccurs="0"/&gt;
     *         &lt;element name="ИнфСтепеньРиск" type="{}Т2000" minOccurs="0"/&gt;
     *         &lt;element name="ПаспортВалид" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="1|0"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ИнфЦельОтношения" type="{}Т2000" minOccurs="0"/&gt;
     *         &lt;element name="ИнфХарактерОтношения" type="{}Т2000" minOccurs="0"/&gt;
     *         &lt;element name="ИнфЦельФХД" type="{}Т2000" minOccurs="0"/&gt;
     *         &lt;element name="ИнфРепутация" type="{}Т2000" minOccurs="0"/&gt;
     *         &lt;element name="ИнфФинансы" type="{}Т2000" minOccurs="0"/&gt;
     *         &lt;element name="ИнфПроисхождениеДеньги" type="{}Т2000" minOccurs="0"/&gt;
     *         &lt;element name="ФИОСотрудника" type="{}ФИО" minOccurs="0"/&gt;
     *         &lt;element name="ДолжностьСотрудника" type="{}Т250" minOccurs="0"/&gt;
     *         &lt;element name="СтепеньРиска" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;pattern value="1|2|3|4"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "isActive",
        "dataIdent",
        "clientType",
        "isResident",
        "isContractor",
        "orgInfo",
        "phone",
        "regAddress",
        "residenceAddress",
        "isIdentClient",
        "baseTakeMeasureCode",
        "dateList",
        "listNumber",
        "recordNumber",
        "decisionDate",
        "decisionNumber",
        "resultDate",
        "relStartDate",
        "completionDate",
        "anotherInf",
        "riskDegreeInfo",
        "isValidPassport",
        "relationshipGoalInfo",
        "relationshipCharacterInfo",
        "activitiesGoalInfo",
        "reputationInfo",
        "financeInfo",
        "financesSourceInfo",
        "employeeName",
        "employeePosition",
        "riskDegree"
    })
    public static class ClientInfo {

        @XmlElement(name = "\u041a\u043b\u0438\u0435\u043d\u0442\u0410\u043a\u0442\u0438\u0432")
        protected String isActive;
        @XmlElement(name = "\u0414\u0430\u0442\u0430\u0418\u0434\u0435\u043d\u0442")
        protected String dataIdent;
        @XmlElement(name = "\u0422\u0438\u043f\u041a\u043b\u0438\u0435\u043d\u0442\u0430")
        protected String clientType;
        @XmlElement(name = "\u041f\u0440\u0438\u0437\u043d\u0430\u043a\u0420\u0435\u0437\u0438\u0434\u0435\u043d\u0442")
        protected String isResident;
        @XmlElement(name = "\u041f\u0440\u0438\u0437\u043d\u0430\u043a\u041a\u043e\u043d\u0442\u0440\u0430\u0433\u0435\u043d\u0442")
        protected String isContractor;
        @XmlElement(name = "\u0421\u0432\u0435\u0434\u041e\u0440\u0433", required = true)
        protected ClientXml.ClientInfo.OrgInfo orgInfo;
        @XmlElement(name = "\u0422\u0435\u043b\u0435\u0444\u043e\u043d")
        protected String phone;
        @XmlElement(name = "\u0410\u0434\u0440\u0420\u0435\u0433")
        protected Address regAddress;
        @XmlElement(name = "\u0410\u0434\u0440\u041f\u0440\u0435\u0431")
        protected Address residenceAddress;
        @XmlElement(name = "\u041f\u0440\u0438\u0437\u043d\u0430\u043a\u0418\u0434\u0435\u043d\u0442\u041a\u043b\u0438\u0435\u043d\u0442\u0430")
        protected String isIdentClient;
        @XmlElement(name = "\u041a\u043e\u0434\u041e\u0441\u043d\u041c\u0435\u0440")
        protected String baseTakeMeasureCode;
        @XmlElement(name = "\u0414\u0430\u0442\u0430\u041f\u0435\u0440\u0435\u0447\u043d\u044f")
        protected String dateList;
        @XmlElement(name = "\u041d\u043e\u043c\u0435\u0440\u041f\u0435\u0440\u0435\u0447\u043d\u044f")
        protected String listNumber;
        @XmlElement(name = "\u041d\u043e\u043c\u0435\u0440\u0417\u0430\u043f\u0438\u0441\u0438\u041f\u0435\u0440\u0435\u0447\u0435\u043d\u044c")
        protected String recordNumber;
        @XmlElement(name = "\u0414\u0430\u0442\u0430\u0420\u0435\u0448\u0435\u043d\u0438\u044f")
        protected String decisionDate;
        @XmlElement(name = "\u041d\u043e\u043c\u0435\u0440\u0420\u0435\u0448\u0435\u043d\u0438\u044f")
        protected String decisionNumber;
        @XmlElement(name = "\u0414\u0430\u0442\u0430\u0420\u0435\u0437\u0443\u043b\u044c\u0442\u0430\u0442")
        protected String resultDate;
        @XmlElement(name = "\u0414\u0430\u0442\u0430\u041d\u0430\u0447\u0430\u043b\u043e\u041e\u0442\u043d")
        protected String relStartDate;
        @XmlElement(name = "\u0414\u0430\u0442\u0430\u0417\u0430\u043f\u043e\u043b\u043d\u0435\u043d\u0438\u044f")
        protected String completionDate;
        @XmlElement(name = "\u0418\u043d\u0430\u044f\u0418\u043d\u0444")
        protected String anotherInf;
        @XmlElement(name = "\u0418\u043d\u0444\u0421\u0442\u0435\u043f\u0435\u043d\u044c\u0420\u0438\u0441\u043a")
        protected String riskDegreeInfo;
        @XmlElement(name = "\u041f\u0430\u0441\u043f\u043e\u0440\u0442\u0412\u0430\u043b\u0438\u0434")
        protected String isValidPassport;
        @XmlElement(name = "\u0418\u043d\u0444\u0426\u0435\u043b\u044c\u041e\u0442\u043d\u043e\u0448\u0435\u043d\u0438\u044f")
        protected String relationshipGoalInfo;
        @XmlElement(name = "\u0418\u043d\u0444\u0425\u0430\u0440\u0430\u043a\u0442\u0435\u0440\u041e\u0442\u043d\u043e\u0448\u0435\u043d\u0438\u044f")
        protected String relationshipCharacterInfo;
        @XmlElement(name = "\u0418\u043d\u0444\u0426\u0435\u043b\u044c\u0424\u0425\u0414")
        protected String activitiesGoalInfo;
        @XmlElement(name = "\u0418\u043d\u0444\u0420\u0435\u043f\u0443\u0442\u0430\u0446\u0438\u044f")
        protected String reputationInfo;
        @XmlElement(name = "\u0418\u043d\u0444\u0424\u0438\u043d\u0430\u043d\u0441\u044b")
        protected String financeInfo;
        @XmlElement(name = "\u0418\u043d\u0444\u041f\u0440\u043e\u0438\u0441\u0445\u043e\u0436\u0434\u0435\u043d\u0438\u0435\u0414\u0435\u043d\u044c\u0433\u0438")
        protected String financesSourceInfo;
        @XmlElement(name = "\u0424\u0418\u041e\u0421\u043e\u0442\u0440\u0443\u0434\u043d\u0438\u043a\u0430")
        protected FullName employeeName;
        @XmlElement(name = "\u0414\u043e\u043b\u0436\u043d\u043e\u0441\u0442\u044c\u0421\u043e\u0442\u0440\u0443\u0434\u043d\u0438\u043a\u0430")
        protected String employeePosition;
        @XmlElement(name = "\u0421\u0442\u0435\u043f\u0435\u043d\u044c\u0420\u0438\u0441\u043a\u0430")
        protected String riskDegree;

        /**
         * Gets the value of the isActive property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIsActive() {
            return isActive;
        }

        /**
         * Sets the value of the isActive property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIsActive(String value) {
            this.isActive = value;
        }

        /**
         * Gets the value of the dataIdent property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDataIdent() {
            return dataIdent;
        }

        /**
         * Sets the value of the dataIdent property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDataIdent(String value) {
            this.dataIdent = value;
        }

        /**
         * Gets the value of the clientType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getClientType() {
            return clientType;
        }

        /**
         * Sets the value of the clientType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setClientType(String value) {
            this.clientType = value;
        }

        /**
         * Gets the value of the isResident property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIsResident() {
            return isResident;
        }

        /**
         * Sets the value of the isResident property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIsResident(String value) {
            this.isResident = value;
        }

        /**
         * Gets the value of the isContractor property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIsContractor() {
            return isContractor;
        }

        /**
         * Sets the value of the isContractor property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIsContractor(String value) {
            this.isContractor = value;
        }

        /**
         * Gets the value of the orgInfo property.
         * 
         * @return
         *     possible object is
         *     {@link ClientXml.ClientInfo.OrgInfo }
         *     
         */
        public ClientXml.ClientInfo.OrgInfo getOrgInfo() {
            return orgInfo;
        }

        /**
         * Sets the value of the orgInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link ClientXml.ClientInfo.OrgInfo }
         *     
         */
        public void setOrgInfo(ClientXml.ClientInfo.OrgInfo value) {
            this.orgInfo = value;
        }

        /**
         * Gets the value of the phone property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPhone() {
            return phone;
        }

        /**
         * Sets the value of the phone property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPhone(String value) {
            this.phone = value;
        }

        /**
         * Gets the value of the regAddress property.
         * 
         * @return
         *     possible object is
         *     {@link Address }
         *     
         */
        public Address getRegAddress() {
            return regAddress;
        }

        /**
         * Sets the value of the regAddress property.
         * 
         * @param value
         *     allowed object is
         *     {@link Address }
         *     
         */
        public void setRegAddress(Address value) {
            this.regAddress = value;
        }

        /**
         * Gets the value of the residenceAddress property.
         * 
         * @return
         *     possible object is
         *     {@link Address }
         *     
         */
        public Address getResidenceAddress() {
            return residenceAddress;
        }

        /**
         * Sets the value of the residenceAddress property.
         * 
         * @param value
         *     allowed object is
         *     {@link Address }
         *     
         */
        public void setResidenceAddress(Address value) {
            this.residenceAddress = value;
        }

        /**
         * Gets the value of the isIdentClient property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIsIdentClient() {
            return isIdentClient;
        }

        /**
         * Sets the value of the isIdentClient property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIsIdentClient(String value) {
            this.isIdentClient = value;
        }

        /**
         * Gets the value of the baseTakeMeasureCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBaseTakeMeasureCode() {
            return baseTakeMeasureCode;
        }

        /**
         * Sets the value of the baseTakeMeasureCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBaseTakeMeasureCode(String value) {
            this.baseTakeMeasureCode = value;
        }

        /**
         * Gets the value of the dateList property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDateList() {
            return dateList;
        }

        /**
         * Sets the value of the dateList property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDateList(String value) {
            this.dateList = value;
        }

        /**
         * Gets the value of the listNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getListNumber() {
            return listNumber;
        }

        /**
         * Sets the value of the listNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setListNumber(String value) {
            this.listNumber = value;
        }

        /**
         * Gets the value of the recordNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRecordNumber() {
            return recordNumber;
        }

        /**
         * Sets the value of the recordNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRecordNumber(String value) {
            this.recordNumber = value;
        }

        /**
         * Gets the value of the decisionDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDecisionDate() {
            return decisionDate;
        }

        /**
         * Sets the value of the decisionDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDecisionDate(String value) {
            this.decisionDate = value;
        }

        /**
         * Gets the value of the decisionNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDecisionNumber() {
            return decisionNumber;
        }

        /**
         * Sets the value of the decisionNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDecisionNumber(String value) {
            this.decisionNumber = value;
        }

        /**
         * Gets the value of the resultDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getResultDate() {
            return resultDate;
        }

        /**
         * Sets the value of the resultDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setResultDate(String value) {
            this.resultDate = value;
        }

        /**
         * Gets the value of the relStartDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRelStartDate() {
            return relStartDate;
        }

        /**
         * Sets the value of the relStartDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRelStartDate(String value) {
            this.relStartDate = value;
        }

        /**
         * Gets the value of the completionDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCompletionDate() {
            return completionDate;
        }

        /**
         * Sets the value of the completionDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCompletionDate(String value) {
            this.completionDate = value;
        }

        /**
         * Gets the value of the anotherInf property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAnotherInf() {
            return anotherInf;
        }

        /**
         * Sets the value of the anotherInf property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAnotherInf(String value) {
            this.anotherInf = value;
        }

        /**
         * Gets the value of the riskDegreeInfo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRiskDegreeInfo() {
            return riskDegreeInfo;
        }

        /**
         * Sets the value of the riskDegreeInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRiskDegreeInfo(String value) {
            this.riskDegreeInfo = value;
        }

        /**
         * Gets the value of the isValidPassport property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIsValidPassport() {
            return isValidPassport;
        }

        /**
         * Sets the value of the isValidPassport property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIsValidPassport(String value) {
            this.isValidPassport = value;
        }

        /**
         * Gets the value of the relationshipGoalInfo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRelationshipGoalInfo() {
            return relationshipGoalInfo;
        }

        /**
         * Sets the value of the relationshipGoalInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRelationshipGoalInfo(String value) {
            this.relationshipGoalInfo = value;
        }

        /**
         * Gets the value of the relationshipCharacterInfo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRelationshipCharacterInfo() {
            return relationshipCharacterInfo;
        }

        /**
         * Sets the value of the relationshipCharacterInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRelationshipCharacterInfo(String value) {
            this.relationshipCharacterInfo = value;
        }

        /**
         * Gets the value of the activitiesGoalInfo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getActivitiesGoalInfo() {
            return activitiesGoalInfo;
        }

        /**
         * Sets the value of the activitiesGoalInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setActivitiesGoalInfo(String value) {
            this.activitiesGoalInfo = value;
        }

        /**
         * Gets the value of the reputationInfo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getReputationInfo() {
            return reputationInfo;
        }

        /**
         * Sets the value of the reputationInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setReputationInfo(String value) {
            this.reputationInfo = value;
        }

        /**
         * Gets the value of the financeInfo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFinanceInfo() {
            return financeInfo;
        }

        /**
         * Sets the value of the financeInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFinanceInfo(String value) {
            this.financeInfo = value;
        }

        /**
         * Gets the value of the financesSourceInfo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFinancesSourceInfo() {
            return financesSourceInfo;
        }

        /**
         * Sets the value of the financesSourceInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFinancesSourceInfo(String value) {
            this.financesSourceInfo = value;
        }

        /**
         * Gets the value of the employeeName property.
         * 
         * @return
         *     possible object is
         *     {@link FullName }
         *     
         */
        public FullName getEmployeeName() {
            return employeeName;
        }

        /**
         * Sets the value of the employeeName property.
         * 
         * @param value
         *     allowed object is
         *     {@link FullName }
         *     
         */
        public void setEmployeeName(FullName value) {
            this.employeeName = value;
        }

        /**
         * Gets the value of the employeePosition property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEmployeePosition() {
            return employeePosition;
        }

        /**
         * Sets the value of the employeePosition property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEmployeePosition(String value) {
            this.employeePosition = value;
        }

        /**
         * Gets the value of the riskDegree property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRiskDegree() {
            return riskDegree;
        }

        /**
         * Sets the value of the riskDegree property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRiskDegree(String value) {
            this.riskDegree = value;
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
         *         &lt;element name="СведенияЮЛ" type="{}СведенияЮЛ"/&gt;
         *         &lt;element name="СведенияФЛИП" type="{}СведенияФЛИП"/&gt;
         *         &lt;element name="СведенияИНБОЮЛ" type="{}СведенияИНБОЮЛ"/&gt;
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
            "companyInfo",
            "individualCustomerInfo",
            "inboulInfo"
        })
        public static class OrgInfo {

            @XmlElement(name = "\u0421\u0432\u0435\u0434\u0435\u043d\u0438\u044f\u042e\u041b")
            protected CompanyInfo companyInfo;
            @XmlElement(name = "\u0421\u0432\u0435\u0434\u0435\u043d\u0438\u044f\u0424\u041b\u0418\u041f")
            protected IndividualCustomerInfo individualCustomerInfo;
            @XmlElement(name = "\u0421\u0432\u0435\u0434\u0435\u043d\u0438\u044f\u0418\u041d\u0411\u041e\u042e\u041b")
            protected InboulInfo inboulInfo;

            /**
             * Gets the value of the companyInfo property.
             * 
             * @return
             *     possible object is
             *     {@link CompanyInfo }
             *     
             */
            public CompanyInfo getCompanyInfo() {
                return companyInfo;
            }

            /**
             * Sets the value of the companyInfo property.
             * 
             * @param value
             *     allowed object is
             *     {@link CompanyInfo }
             *     
             */
            public void setCompanyInfo(CompanyInfo value) {
                this.companyInfo = value;
            }

            /**
             * Gets the value of the individualCustomerInfo property.
             * 
             * @return
             *     possible object is
             *     {@link IndividualCustomerInfo }
             *     
             */
            public IndividualCustomerInfo getIndividualCustomerInfo() {
                return individualCustomerInfo;
            }

            /**
             * Sets the value of the individualCustomerInfo property.
             * 
             * @param value
             *     allowed object is
             *     {@link IndividualCustomerInfo }
             *     
             */
            public void setIndividualCustomerInfo(IndividualCustomerInfo value) {
                this.individualCustomerInfo = value;
            }

            /**
             * Gets the value of the inboulInfo property.
             * 
             * @return
             *     possible object is
             *     {@link InboulInfo }
             *     
             */
            public InboulInfo getInboulInfo() {
                return inboulInfo;
            }

            /**
             * Sets the value of the inboulInfo property.
             * 
             * @param value
             *     allowed object is
             *     {@link InboulInfo }
             *     
             */
            public void setInboulInfo(InboulInfo value) {
                this.inboulInfo = value;
            }

        }

    }

}
