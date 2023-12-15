
package ru.softlab.efr.services.insurance.model.client.unload;

import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.softlab.efr.services.insurance.model.client.unload package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.softlab.efr.services.insurance.model.client.unload
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Founder }
     * 
     */
    public Founder createFounder() {
        return new Founder();
    }

    /**
     * Create an instance of {@link ClientXml }
     * 
     */
    public ClientXml createClientXml() {
        return new ClientXml();
    }

    /**
     * Create an instance of {@link ClientXml.ClientInfo }
     * 
     */
    public ClientXml.ClientInfo createClientXmlClientInfo() {
        return new ClientXml.ClientInfo();
    }

    /**
     * Create an instance of {@link Address }
     * 
     */
    public Address createAddress() {
        return new Address();
    }

    /**
     * Create an instance of {@link Address2 }
     * 
     */
    public Address2 createAddress2() {
        return new Address2();
    }

    /**
     * Create an instance of {@link BirthPlace }
     * 
     */
    public BirthPlace createBirthPlace() {
        return new BirthPlace();
    }

    /**
     * Create an instance of {@link FullName }
     * 
     */
    public FullName createFullName() {
        return new FullName();
    }

    /**
     * Create an instance of {@link IdentityDocInfo }
     * 
     */
    public IdentityDocInfo createIdentityDocInfo() {
        return new IdentityDocInfo();
    }

    /**
     * Create an instance of {@link MigrationCardInfo }
     * 
     */
    public MigrationCardInfo createMigrationCardInfo() {
        return new MigrationCardInfo();
    }

    /**
     * Create an instance of {@link ForeignDocInfo }
     * 
     */
    public ForeignDocInfo createForeignDocInfo() {
        return new ForeignDocInfo();
    }

    /**
     * Create an instance of {@link CompanyInfo }
     * 
     */
    public CompanyInfo createCompanyInfo() {
        return new CompanyInfo();
    }

    /**
     * Create an instance of {@link InboulInfo }
     * 
     */
    public InboulInfo createInboulInfo() {
        return new InboulInfo();
    }

    /**
     * Create an instance of {@link IndividualCustomerInfo }
     * 
     */
    public IndividualCustomerInfo createIndividualCustomerInfo() {
        return new IndividualCustomerInfo();
    }

    /**
     * Create an instance of {@link LicenseInfo }
     * 
     */
    public LicenseInfo createLicenseInfo() {
        return new LicenseInfo();
    }

    /**
     * Create an instance of {@link Work }
     * 
     */
    public Work createWork() {
        return new Work();
    }

    /**
     * Create an instance of {@link Founder.FounderName }
     * 
     */
    public Founder.FounderName createFounderFounderName() {
        return new Founder.FounderName();
    }

    /**
     * Create an instance of {@link ClientXml.ClientInfo.OrgInfo }
     * 
     */
    public ClientXml.ClientInfo.OrgInfo createClientXmlClientInfoOrgInfo() {
        return new ClientXml.ClientInfo.OrgInfo();
    }

    /**
     * Create an instance of {@link IndividualCustomerInfo.IdentityDoc }}
     * 
     */
    @XmlElementDecl(namespace = "", name = "\u0421\u0432\u0435\u0434\u0414\u043e\u043a\u0423\u0434\u041b\u0438\u0447\u043d", scope = IndividualCustomerInfo.class)
    public IndividualCustomerInfo.IdentityDoc createIndividualCustomerInfoIdentityDoc(IdentityDocInfo value) {
        return new IndividualCustomerInfo.IdentityDoc(value);
    }

    /**
     * Create an instance of {@link CompanyInfo.License }}
     * 
     */
    @XmlElementDecl(namespace = "", name = "\u041b\u0438\u0446\u0435\u043d\u0437\u0438\u044f", scope = CompanyInfo.class)
    public CompanyInfo.License createCompanyInfoLicense(LicenseInfo value) {
        return new CompanyInfo.License(value);
    }

}
