
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
 * Сведения о физическом лице, индивидуальном предпринимателе
 * 
 * <p>Java class for СведенияФЛИП complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="СведенияФЛИП"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ФИОФЛИП" type="{}ФИО" minOccurs="0"/&gt;
 *         &lt;element name="ИННФЛИП" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[0-9]{12}|"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ОКВЭДИП" type="{}ОКВЭДТип" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="НаименРегОргана" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="ОГРНИП" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{}ОГРНИПТип"&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ОКПО" type="{}ОКПОТип" minOccurs="0"/&gt;
 *         &lt;element name="ДатаРегИП" type="{}ДатаТип" minOccurs="0"/&gt;
 *         &lt;element name="КодОКСМ" type="{}ОКСМТип" minOccurs="0"/&gt;
 *         &lt;element name="СтранаНаименование" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="ДатаРождения" type="{}ДатаТип" minOccurs="0"/&gt;
 *         &lt;element name="МестоРожд" type="{}МестоРождения" minOccurs="0"/&gt;
 *         &lt;element name="ВидГражданства" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[1-5]"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="СведДокУдЛичн" type="{}ДокУдост"/&gt;
 *         &lt;element name="СведМигрКарта" type="{}ДокКарта" minOccurs="0"/&gt;
 *         &lt;element name="СведДокПраво" type="{}ДокПраво" minOccurs="0"/&gt;
 *         &lt;element name="ПризнакПринПубЛицо" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[0-5]"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ПризнакРоссПубЛицо" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[0-5]"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ПризнакИнострПубЛицо" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[0-9]|1[0-3]"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ИнойПризнак" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="Родство" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[1-8]"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Должность" type="{}Т250" minOccurs="0"/&gt;
 *         &lt;element name="ВидИдентификации" type="{}ВидИдентТип" minOccurs="0"/&gt;
 *         &lt;element name="ТипФЛЧастнаяПрактика" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="1|2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="РегнНомер" type="{}Т50" minOccurs="0"/&gt;
 *         &lt;element name="СНИЛСФЛИП" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[0-9]{3}-[0-9]{3}-[0-9]{3}(-| )[0-9]{2}"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ОКВЭД2ИП" type="{}ОКВЭДТип" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="Работа" type="{}СведенияРабота" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "\u0421\u0432\u0435\u0434\u0435\u043d\u0438\u044f\u0424\u041b\u0418\u041f", propOrder = {
    "individualCustomerFullName",
    "inn",
    "okved",
    "regAgencyName",
    "ogrn",
    "okpo",
    "regDate",
    "oksmCode",
    "countryName",
    "birthDate",
    "birthPlace",
    "citizenshipKind",
    "identityDoc",
    "migrationCardInfo",
    "foreignDocInfo",
    "isPublicOfficial",
    "isRussianPublicOfficial",
    "isForeignPublicOfficial",
    "isAnotherKind",
    "relations",
    "position",
    "identityKind",
    "individualCustomerKind",
    "regNumber",
    "snlis",
    "okved2",
    "work"
})
public class IndividualCustomerInfo {

    @XmlElement(name = "\u0424\u0418\u041e\u0424\u041b\u0418\u041f")
    protected FullName individualCustomerFullName;
    @XmlElement(name = "\u0418\u041d\u041d\u0424\u041b\u0418\u041f")
    protected String inn;
    @XmlElement(name = "\u041e\u041a\u0412\u042d\u0414\u0418\u041f")
    protected List<String> okved;
    @XmlElement(name = "\u041d\u0430\u0438\u043c\u0435\u043d\u0420\u0435\u0433\u041e\u0440\u0433\u0430\u043d\u0430")
    protected String regAgencyName;
    @XmlElement(name = "\u041e\u0413\u0420\u041d\u0418\u041f")
    protected String ogrn;
    @XmlElement(name = "\u041e\u041a\u041f\u041e")
    protected String okpo;
    @XmlElement(name = "\u0414\u0430\u0442\u0430\u0420\u0435\u0433\u0418\u041f")
    protected String regDate;
    @XmlElement(name = "\u041a\u043e\u0434\u041e\u041a\u0421\u041c")
    protected String oksmCode;
    @XmlElement(name = "\u0421\u0442\u0440\u0430\u043d\u0430\u041d\u0430\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435")
    protected String countryName;
    @XmlElement(name = "\u0414\u0430\u0442\u0430\u0420\u043e\u0436\u0434\u0435\u043d\u0438\u044f")
    protected String birthDate;
    @XmlElement(name = "\u041c\u0435\u0441\u0442\u043e\u0420\u043e\u0436\u0434")
    protected BirthPlace birthPlace;
    @XmlElement(name = "\u0412\u0438\u0434\u0413\u0440\u0430\u0436\u0434\u0430\u043d\u0441\u0442\u0432\u0430")
    protected String citizenshipKind;
    @XmlElementRef(name = "\u0421\u0432\u0435\u0434\u0414\u043e\u043a\u0423\u0434\u041b\u0438\u0447\u043d", type = IndividualCustomerInfo.IdentityDoc.class)
    protected IndividualCustomerInfo.IdentityDoc identityDoc;
    @XmlElement(name = "\u0421\u0432\u0435\u0434\u041c\u0438\u0433\u0440\u041a\u0430\u0440\u0442\u0430")
    protected MigrationCardInfo migrationCardInfo;
    @XmlElement(name = "\u0421\u0432\u0435\u0434\u0414\u043e\u043a\u041f\u0440\u0430\u0432\u043e")
    protected ForeignDocInfo foreignDocInfo;
    @XmlElement(name = "\u041f\u0440\u0438\u0437\u043d\u0430\u043a\u041f\u0440\u0438\u043d\u041f\u0443\u0431\u041b\u0438\u0446\u043e")
    protected String isPublicOfficial;
    @XmlElement(name = "\u041f\u0440\u0438\u0437\u043d\u0430\u043a\u0420\u043e\u0441\u0441\u041f\u0443\u0431\u041b\u0438\u0446\u043e")
    protected String isRussianPublicOfficial;
    @XmlElement(name = "\u041f\u0440\u0438\u0437\u043d\u0430\u043a\u0418\u043d\u043e\u0441\u0442\u0440\u041f\u0443\u0431\u041b\u0438\u0446\u043e")
    protected String isForeignPublicOfficial;
    @XmlElement(name = "\u0418\u043d\u043e\u0439\u041f\u0440\u0438\u0437\u043d\u0430\u043a")
    protected String isAnotherKind;
    @XmlElement(name = "\u0420\u043e\u0434\u0441\u0442\u0432\u043e")
    protected String relations;
    @XmlElement(name = "\u0414\u043e\u043b\u0436\u043d\u043e\u0441\u0442\u044c")
    protected String position;
    @XmlElement(name = "\u0412\u0438\u0434\u0418\u0434\u0435\u043d\u0442\u0438\u0444\u0438\u043a\u0430\u0446\u0438\u0438")
    protected String identityKind;
    @XmlElement(name = "\u0422\u0438\u043f\u0424\u041b\u0427\u0430\u0441\u0442\u043d\u0430\u044f\u041f\u0440\u0430\u043a\u0442\u0438\u043a\u0430")
    protected String individualCustomerKind;
    @XmlElement(name = "\u0420\u0435\u0433\u043d\u041d\u043e\u043c\u0435\u0440")
    protected String regNumber;
    @XmlElement(name = "\u0421\u041d\u0418\u041b\u0421\u0424\u041b\u0418\u041f")
    protected String snlis;
    @XmlElement(name = "\u041e\u041a\u0412\u042d\u04142\u0418\u041f")
    protected List<String> okved2;
    @XmlElement(name = "\u0420\u0430\u0431\u043e\u0442\u0430")
    protected Work work;

    /**
     * Gets the value of the individualCustomerFullName property.
     * 
     * @return
     *     possible object is
     *     {@link FullName }
     *     
     */
    public FullName getIndividualCustomerFullName() {
        return individualCustomerFullName;
    }

    /**
     * Sets the value of the individualCustomerFullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullName }
     *     
     */
    public void setIndividualCustomerFullName(FullName value) {
        this.individualCustomerFullName = value;
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
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthDate(String value) {
        this.birthDate = value;
    }

    /**
     * Gets the value of the birthPlace property.
     * 
     * @return
     *     possible object is
     *     {@link BirthPlace }
     *     
     */
    public BirthPlace getBirthPlace() {
        return birthPlace;
    }

    /**
     * Sets the value of the birthPlace property.
     * 
     * @param value
     *     allowed object is
     *     {@link BirthPlace }
     *     
     */
    public void setBirthPlace(BirthPlace value) {
        this.birthPlace = value;
    }

    /**
     * Gets the value of the citizenshipKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCitizenshipKind() {
        return citizenshipKind;
    }

    /**
     * Sets the value of the citizenshipKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCitizenshipKind(String value) {
        this.citizenshipKind = value;
    }

    /**
     * Gets the value of the identityDoc property.
     * 
     * @return
     *     possible object is
     *     {@link IndividualCustomerInfo.IdentityDoc }
     *     
     */
    public IndividualCustomerInfo.IdentityDoc getIdentityDoc() {
        return identityDoc;
    }

    /**
     * Sets the value of the identityDoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndividualCustomerInfo.IdentityDoc }
     *     
     */
    public void setIdentityDoc(IndividualCustomerInfo.IdentityDoc value) {
        this.identityDoc = value;
    }

    /**
     * Gets the value of the migrationCardInfo property.
     * 
     * @return
     *     possible object is
     *     {@link MigrationCardInfo }
     *     
     */
    public MigrationCardInfo getMigrationCardInfo() {
        return migrationCardInfo;
    }

    /**
     * Sets the value of the migrationCardInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link MigrationCardInfo }
     *     
     */
    public void setMigrationCardInfo(MigrationCardInfo value) {
        this.migrationCardInfo = value;
    }

    /**
     * Gets the value of the foreignDocInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ForeignDocInfo }
     *     
     */
    public ForeignDocInfo getForeignDocInfo() {
        return foreignDocInfo;
    }

    /**
     * Sets the value of the foreignDocInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForeignDocInfo }
     *     
     */
    public void setForeignDocInfo(ForeignDocInfo value) {
        this.foreignDocInfo = value;
    }

    /**
     * Gets the value of the isPublicOfficial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsPublicOfficial() {
        return isPublicOfficial;
    }

    /**
     * Sets the value of the isPublicOfficial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsPublicOfficial(String value) {
        this.isPublicOfficial = value;
    }

    /**
     * Gets the value of the isRussianPublicOfficial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsRussianPublicOfficial() {
        return isRussianPublicOfficial;
    }

    /**
     * Sets the value of the isRussianPublicOfficial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsRussianPublicOfficial(String value) {
        this.isRussianPublicOfficial = value;
    }

    /**
     * Gets the value of the isForeignPublicOfficial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsForeignPublicOfficial() {
        return isForeignPublicOfficial;
    }

    /**
     * Sets the value of the isForeignPublicOfficial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsForeignPublicOfficial(String value) {
        this.isForeignPublicOfficial = value;
    }

    /**
     * Gets the value of the isAnotherKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsAnotherKind() {
        return isAnotherKind;
    }

    /**
     * Sets the value of the isAnotherKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsAnotherKind(String value) {
        this.isAnotherKind = value;
    }

    /**
     * Gets the value of the relations property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelations() {
        return relations;
    }

    /**
     * Sets the value of the relations property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelations(String value) {
        this.relations = value;
    }

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
     * Gets the value of the identityKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentityKind() {
        return identityKind;
    }

    /**
     * Sets the value of the identityKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentityKind(String value) {
        this.identityKind = value;
    }

    /**
     * Gets the value of the individualCustomerKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndividualCustomerKind() {
        return individualCustomerKind;
    }

    /**
     * Sets the value of the individualCustomerKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndividualCustomerKind(String value) {
        this.individualCustomerKind = value;
    }

    /**
     * Gets the value of the regNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegNumber() {
        return regNumber;
    }

    /**
     * Sets the value of the regNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegNumber(String value) {
        this.regNumber = value;
    }

    /**
     * Gets the value of the snlis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSnlis() {
        return snlis;
    }

    /**
     * Sets the value of the snlis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSnlis(String value) {
        this.snlis = value;
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
     * Gets the value of the work property.
     * 
     * @return
     *     possible object is
     *     {@link Work }
     *     
     */
    public Work getWork() {
        return work;
    }

    /**
     * Sets the value of the work property.
     * 
     * @param value
     *     allowed object is
     *     {@link Work }
     *     
     */
    public void setWork(Work value) {
        this.work = value;
    }

    public static class IdentityDoc
        extends JAXBElement<IdentityDocInfo>
    {

        protected final static QName NAME = new QName("", "\u0421\u0432\u0435\u0434\u0414\u043e\u043a\u0423\u0434\u041b\u0438\u0447\u043d");

        public IdentityDoc(IdentityDocInfo value) {
            super(NAME, ((Class) IdentityDocInfo.class), IndividualCustomerInfo.class, value);
        }

        public IdentityDoc() {
            super(NAME, ((Class) IdentityDocInfo.class), IndividualCustomerInfo.class, null);
        }

    }

}
