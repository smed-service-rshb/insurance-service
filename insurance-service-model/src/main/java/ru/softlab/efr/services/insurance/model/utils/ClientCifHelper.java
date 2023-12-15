package ru.softlab.efr.services.insurance.model.utils;

import ru.softlab.efr.clients.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.softlab.efr.clients.model.Constants.isCodeRF;

/**
 * Хелпер для клиентских данных
 * @author krutikov
 * @since 25.06.2018
 */
public class ClientCifHelper {
    
    /**
     * Обновление данных по клиенту
     * @param client клиент, которого необходимо обновить
     * @param cifClient новые клиентские данные
     */
    public static void update(Client client, Client cifClient) {
        client.setSurName(cifClient.getSurName());
        client.setFirstName(cifClient.getFirstName());
        client.setMiddleName(cifClient.getMiddleName());
        client.setBirthDate(cifClient.getBirthDate());
        client.setGender(cifClient.getGender());
        client.setBirthPlace(cifClient.getBirthPlace());
        client.setResident(cifClient.getResident());
        client.setResidentRF(isCodeRF(client.getResident()));
        client.setEmail(cifClient.getEmail());
        client.setInn(cifClient.getInn());
        client.setSnils(cifClient.getSnils());
        client.setLicenseDriver(cifClient.getLicenseDriver());

        updateNationalities(client, cifClient);
        updateDocuments(client, cifClient);
        updatePhones(client, cifClient);
        updateAddresses(client, cifClient);
        updateAgreements(client, cifClient);
    }

    /**
     * Обновление согласий клиента
     * @param client клиент, которого необходимо обновить
     * @param cifClient новые клиентские данные
     */
    public static void updateAgreements(Client client, Client cifClient) {
        if (cifClient.getAgreements() == null){
            return;
        }

        if (client.getAgreements() == null) {
            client.setAgreements(cifClient.getAgreements());
            return;
        }

        List<Agreement> newAgreements = new ArrayList<>();
        cifClient.getAgreements().forEach(cifAgreement -> {
            Optional<Agreement> agreement = client.getAgreements().stream()
                    .filter(a -> fieldsEquals(a.getType(), cifAgreement.getType())
                            && fieldsEquals(a.isIsRecall(), cifAgreement.isIsRecall()))
                    .findFirst();
            if (agreement.isPresent()){
                updateAgreement(agreement.get(), cifAgreement);
            } else {
                newAgreements.add(cifAgreement);
            }
        });
        client.getAgreements().addAll(newAgreements);
    }

    private static void updateAgreement(Agreement agreement, Agreement aNew) {
        agreement.setStartDate(aNew.getStartDate());
        agreement.setEndDate(aNew.getEndDate());
    }

    /**
     * Обновление гражданств клиента
     * @param client - Клиент, которого необходимо обновить
     * @param cifClient - Новые клиентские данные
     */
    public static void updateNationalities(Client client, Client cifClient) {
        if (client.getNationalities() == null) client.setNationalities(new ArrayList<>());
        if (cifClient.getNationalities() == null) cifClient.setNationalities(new ArrayList<>());
        List<String> nationalities = client.getNationalities();
        for (String nationality: cifClient.getNationalities()) {
            if (!nationalities.contains(nationality)) {
                nationalities.add(nationality);
            }
        }
    }

    /**
     * Обновление документов клиента
     * @param client клиент, которого необходимо обновить
     * @param cifClient новые клиентские данные
     */
    public static void updateDocuments(Client client, Client cifClient) {
        if (cifClient.getDocuments() == null){
            return;
        }

        if (client.getDocuments() == null || client.getDocuments().isEmpty()) {
            client.setDocuments(cifClient.getDocuments());
            return;
        }

        List<Document> newDocuments = new ArrayList<>();
        cifClient.getDocuments().forEach(cifDocument -> {
            Optional<Document> document = client.getDocuments().stream()
                    .filter(d -> fieldsEquals(d.getDocType(), cifDocument.getDocType())
                            && fieldsEquals(d.getDocSeries(), cifDocument.getDocSeries())
                            && fieldsEquals(d.getDocNumber(), cifDocument.getDocNumber()))
                    .findFirst();
            if (document.isPresent()){
                updateDocument(document.get(), cifDocument);
            } else {
                newDocuments.add(cifDocument);
            }
        });
        client.getDocuments().addAll(newDocuments);
    }

    private static void updateDocument(Document document, Document cifD) {
        document.setIssuedDate(cifD.getIssuedDate());
        document.setIssuedEndDate(cifD.getIssuedEndDate());
        document.setIssuedBy(cifD.getIssuedBy());
        document.setDivisionCode(cifD.getDivisionCode());
        document.setIsValidDocument(cifD.isIsValidDocument());
        document.setIsMain(cifD.isIsMain());
    }

    /**
     * Обновление телефонов клиента
     * @param client клиент, которого необходимо обновить
     * @param cifClient новые клиентские данные
     */
    public static void updatePhones(Client client, Client cifClient) {
        if (cifClient.getPhones() == null){
            return;
        }

        if (client.getPhones() == null) {
            client.setPhones(cifClient.getPhones());
            return;
        }

        List<Phone> newPhones = new ArrayList<>();
        cifClient.getPhones().forEach(cifPhone -> {
            Optional<Phone> phone = client.getPhones().stream()
                    .filter(p -> fieldsEquals(p.getNumber(), cifPhone.getNumber()))
                    .findFirst();
            if (phone.isPresent()){
                updatePhone(phone.get(), cifPhone);
            } else {
                newPhones.add(cifPhone);
            }
        });
        client.getPhones().addAll(newPhones);
    }

    private static void updatePhone(Phone phone, Phone cifP) {
        phone.setType(cifP.getType());
        phone.setMain(cifP.isMain());
        phone.setNotification(cifP.isNotification());
        phone.setVerified(cifP.isVerified());
    }

    /**
     * Обновление данных по клиенту
     * @param client клиент, которого необходимо обновить
     * @param cifClient новые клиентские данные
     */
    public static void updateAddresses(Client client, Client cifClient) {
        if (cifClient.getAddresses() == null){
            return;
        }

        if (client.getAddresses() == null) {
            client.setAddresses(cifClient.getAddresses());
            return;
        }

        List<Address> newAddresses = new ArrayList<>();
        cifClient.getAddresses().forEach(cifAddress -> {
            Optional<Address> address = client.getAddresses().stream()
                    .filter(a -> fieldsEquals(a.getAddressType(), cifAddress.getAddressType() ))
                    .findFirst();
            if (address.isPresent()){
                updateAddress(address.get(), cifAddress);
            } else {
                newAddresses.add(cifAddress);
            }
        });
        client.getAddresses().addAll(newAddresses);
    }

    private static void updateAddress(Address address, Address aNew) {
        address.setCountry(aNew.getCountry());
        address.setRegion(aNew.getRegion());
        address.setArea(aNew.getArea());
        address.setCity(aNew.getCity());
        address.setLocality(aNew.getLocality());
        address.setStreet(aNew.getStreet());
        address.setHouse(aNew.getHouse());
        address.setConstruction(aNew.getConstruction());
        address.setHousing(aNew.getHousing());
        address.setApartment(aNew.getApartment());
        address.setIndex(aNew.getIndex());
    }

    private static boolean fieldsEquals(Object field1, Object field2) {
        if (field1 == null) {
            return field2 == null;
        }
        return field1.equals(field2);
    }
}
