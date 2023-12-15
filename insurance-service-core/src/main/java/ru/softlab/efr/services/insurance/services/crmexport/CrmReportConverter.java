package ru.softlab.efr.services.insurance.services.crmexport;

import org.springframework.stereotype.Service;
import ru.softlab.efr.services.insurance.model.db.AddressForClientEntity;
import ru.softlab.efr.services.insurance.model.db.DocumentForClientEntity;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.PhoneForClaimEntity;
import ru.softlab.efr.services.insurance.model.enums.AddressTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.GenderTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.IdentityDocTypeEnum;
import ru.softlab.efr.services.insurance.model.rest.PhoneType;
import ru.softlab.efr.services.insurance.services.crmexport.models.CrmReportModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CrmReportConverter {
    public CrmReportModel getReportByInsurance(Insurance insurance) {
        CrmReportModel model = new CrmReportModel();
        model.setClientCode(insurance.getContractNumber());
        if (Objects.nonNull(insurance.getHolder().getGender())) {
            if (GenderTypeEnum.FEMALE.equals(insurance.getHolder().getGender())) {
                model.setClientGenderRequest("г-жа");
            } else if (GenderTypeEnum.MALE.equals(insurance.getHolder().getGender())) {
                model.setClientGenderRequest("г-н");
            }
        }
        model.setGotProgram(insurance.getProgramSetting().getProgram().getName());
        model.setClientSecondname(insurance.getHolder().getSurName());
        model.setClientName(insurance.getHolder().getFirstName());
        model.setClientThirdname(insurance.getHolder().getMiddleName());
        model.setClientBirthDate(insurance.getHolder().getBirthDate().toString());
        List<PhoneForClaimEntity> phones = insurance.getHolder().getPhones();
        if (phones.isEmpty()) {
            model.setClientMobilePhone(insurance.getHolder().getMobilePhone());
        } else {
            //List<PhoneForClaimEntity> otherPhones = new ArrayList<>();
            phones.forEach(phone -> {
                if (PhoneType.MOBILE.equals(phone.getPhoneType())) {
                    model.setClientMobilePhone(phone.getNumber());
                } /*else {
                    otherPhones.add(phone);
                }*/
            });
            /*if (!otherPhones.isEmpty()) {
                model.setClientOtherPhones(otherPhones
                        .stream()
                        .distinct()
                        .map(PhoneForClaimEntity::getNumber)
                        .collect(Collectors.joining(", ")));
            }*/
        }
        model.setClientEmail(insurance.getHolder().getEmail());
        model.setResource("РСХБ");
        model.setExported("да");
        model.setAvailability("да");
        List<AddressForClientEntity> addresses = insurance.getHolder().getAddresses();
        if (!addresses.isEmpty()) {
            addresses.forEach(address -> {
                if (AddressTypeEnum.REGISTRATION.equals(address.getAddressType())) {
                    List<String> addressElements = new ArrayList<>();
                    if(Objects.nonNull(address.getCountry())) {
                        addressElements.add(address.getCountry());
                    }
                    if(Objects.nonNull(address.getRegion())) {
                        model.setRegistrationAddressRegion(address.getRegion());
                    }
                    if(Objects.nonNull(address.getArea())) {
                        addressElements.add(address.getArea());
                    }
                    if(Objects.nonNull(address.getIndex())) {
                        addressElements.add(address.getIndex());
                    }
                    if(Objects.nonNull(address.getCity())) {
                        addressElements.add("г. " + address.getCity());
                    }
                    if(Objects.nonNull(address.getStreet())) {
                        addressElements.add("уд. " + address.getStreet());
                    }
                    if(Objects.nonNull(address.getHouse())) {
                        addressElements.add("д. " + address.getHouse());
                    }
                    if(Objects.nonNull(address.getConstruction())) {
                        addressElements.add("стр. " + address.getConstruction());
                    }
                    if(Objects.nonNull(address.getHousing())) {
                        addressElements.add("к. " +address.getHousing());
                    }
                    if(Objects.nonNull(address.getApartment())) {
                        addressElements.add("кв. " +address.getApartment());
                    }

                    model.setRegistrationAddress(String.join(" ", addressElements));
                }
            });
        }
        List<DocumentForClientEntity> documents = insurance.getHolder().getDocuments();
        if (!documents.isEmpty()) {
            documents.forEach(document -> {
                if (IdentityDocTypeEnum.PASSPORT_RF.equals(document.getDocType())) {
                    model.setPassportSeries(
                            Optional.ofNullable(document.getDocSeries()).orElse("") + Optional.ofNullable(document.getDocNumber()).orElse(""));
                    if (Objects.nonNull(document.getIssuedDate())) {
                        model.setPassportCreationDate(document.getIssuedDate().toString());
                    }
                    model.setPassportRegistrationChair(Optional.ofNullable(document.getIssuedBy()).orElse(""));
                    model.setPassportDivisionCode(Optional.ofNullable(document.getDivisionCode()).orElse(""));
                }
            });
        }
        model.setClientPayedSum(insurance.getRurAmount().toString());
        model.setPayedDate(insurance.getCreationDate().toString());
        model.setProgramKindName(insurance.getProgramSetting().getProgram().getType().toString());
        return model;
    }
}
