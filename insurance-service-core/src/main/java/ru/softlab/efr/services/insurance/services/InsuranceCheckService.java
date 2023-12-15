package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.services.insurance.model.db.RequiredField;
import ru.softlab.efr.services.insurance.model.db.Risk;
import ru.softlab.efr.services.insurance.model.db.*;
import ru.softlab.efr.services.insurance.model.enums.IdentityDocTypeEnum;
import ru.softlab.efr.services.insurance.model.enums.PublicOfficialTypeEnum;
import ru.softlab.efr.services.insurance.model.rest.*;
import ru.softlab.efr.services.insurance.utils.AppUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Сервис проверок корректности заполнения договора страхования на соответствие настройкам программы страхования
 *
 * @author olshansky
 * @since 20.11.2018
 */
@Service
@PropertySource(value = "classpath:ValidationMessages.properties", encoding = "UTF-8")
public class InsuranceCheckService {

    private static final Logger LOGGER = Logger.getLogger(InsuranceCheckService.class);
    private static final List<String> STRUCTURE_NAME_LIST = Arrays.asList("documents", "phones", "addresses", "foreignerDoc", "migrationCard");
    private static final Set<String> notAlwaysRequiredFields = Stream.of("citizenshipCountry",
            "foreignPublicOfficialType",
            "russianPublicOfficialType"
    ).collect(Collectors.toSet());

    @Value("${required.field.error}")
    private String REQUIRED_FIELD_SHOULD_BE_FILLED;
    @Value("${required.holder}")
    private String REQUIRED_HOLDER;
    @Value("${required.insured}")
    private String REQUIRED_INSURED;
    @Value("${field.name.holderid}")
    private String HOLDER_ID_FIELD_NAME;
    @Value("${field.name.insuredid}")
    private String INSURED_ID_FIELD_NAME;
    @Value("${field.name.holderdata}")
    private String HOLDER_DATA_FIELD_NAME;
    @Value("${field.name.insureddata}")
    private String INSURED_DATA_FIELD_NAME;
    @Value("${field.name.recipient.list}")
    private String RECIPIENT_LIST_FIELD_NAME;
    @Value("${field.name.share}")
    private String SHARE_FIELD_NAME;
    @Value("${field.name.surname}")
    private String SURNAME_FIELD_NAME;
    @Value("${field.name.firstname}")
    private String FIRSTNAME_FIELD_NAME;
    @Value("${field.name.birthdate}")
    private String BIRTHDATE_FIELD_NAME;
    @Value("${field.name.main.phone}")
    private String MAINPHONE_FIELD_NAME;
    @Value("${recipient.list.share.should.100}")
    private String RECIPIENT_SHARE_SHOULD_BE_100;
    @Value("${field.not.exists}")
    private String FIELD_NOT_EXISTS;
    @Value("${document.not.exists}")
    private String DOCUMENT_NOT_EXISTS;
    @Value("${foreign.document.not.exists}")
    private String FOREIGN_DOC_NOT_EXISTS;
    @Value("${client.age.error}")
    private String CLIENT_AGE_ERROR;

    private AttachmentService attachmentService;
    private RequiredFieldService requiredFieldService;

    @Autowired
    public InsuranceCheckService(AttachmentService attachmentService, RequiredFieldService requiredFieldService) {
        this.attachmentService = attachmentService;
        this.requiredFieldService = requiredFieldService;
    }

    public List<CheckModel> getContractErrors(SetStatusInsuranceModel insuranceModel, Insurance insuranceContract, String nextStatus) {
        BaseInsuranceModel baseInsuranceModel = new BaseInsuranceModel();
        BeanUtils.copyProperties(insuranceModel, baseInsuranceModel);
        return getContractErrors(baseInsuranceModel, insuranceContract, nextStatus);
    }

    public List<CheckModel> getContractErrors(BaseInsuranceModel insuranceModel, Insurance insuranceContract, String nextStatus) {
        List<CheckModel> contractErrors = new ArrayList<>();
        contractErrors.addAll(getRequiredFieldsErrors(insuranceModel, insuranceContract));
        contractErrors.addAll(getRequiredDocumentsErrors(insuranceContract, nextStatus));
        contractErrors.addAll(getProgramSettingErrors(insuranceContract));
        return contractErrors;
    }

    private List<CheckModel> getRequiredFieldsErrors(BaseInsuranceModel insuranceModel, Insurance insuranceContract) {
        List<CheckModel> contractErrors = new ArrayList<>();

        if (insuranceModel.getHolderId() == null && insuranceModel.getHolderData() == null) {
            contractErrors.add(new CheckModel(HOLDER_ID_FIELD_NAME, REQUIRED_HOLDER, CheckModelErrorType.CRITICAL));
        }

        if (insuranceModel.getInsuredId() == null && insuranceModel.getInsuredData() == null && !insuranceModel.isHolderEqualsInsured()) {
            contractErrors.add(new CheckModel(INSURED_ID_FIELD_NAME, REQUIRED_INSURED, CheckModelErrorType.CRITICAL));
        }
        contractErrors.addAll(checkClientRequiredFields(insuranceModel.getHolderData(), HOLDER_DATA_FIELD_NAME, insuranceContract));
        if (!Boolean.TRUE.equals(insuranceModel.isHolderEqualsInsured())) {
            contractErrors.addAll(checkClientRequiredFields(insuranceModel.getInsuredData(), INSURED_DATA_FIELD_NAME, insuranceContract));
        }
        return contractErrors;
    }

    private List<CheckModel> checkClientRequiredFields(Client client, String clientTypeFieldName, Insurance insuranceContract) {
        List<CheckModel> clientErrors = new ArrayList<>();
        if (client != null) {
            List<String> requiredFields = insuranceContract.getProgramSetting().getProgram().getType().getRequiredFields();
            requiredFields.forEach(requiredField -> {
                try {
                    CheckModel criticalError = new CheckModel(clientTypeFieldName.concat(".".concat(requiredField)),
                            REQUIRED_FIELD_SHOULD_BE_FILLED, CheckModelErrorType.CRITICAL);

                    if (isStructure(requiredField)) {
                        if (requiredField.equalsIgnoreCase("phones")) {
                            if (CollectionUtils.isEmpty(client.getPhones())
                                    || client.getPhones().stream().noneMatch(Phone::isMain)) {
                                clientErrors.add(criticalError);
                            }
                        } else {
                            Object requiredStructure = getObject(requiredField, client);
                            if ((requiredStructure instanceof ArrayList && CollectionUtils.isEmpty((ArrayList) requiredStructure))) {
                                clientErrors.add(criticalError);
                            }
                        }
                    } else if (AppUtils.isNullOrWhitespace(String.valueOf(getObject(requiredField, client)))) {
                        clientErrors.add(criticalError);
                    }
                } catch (Exception ex) {
                    LOGGER.error("Во время проверки безусловно обязательных полей клиента произошла ошибка, причина:", ex);
                }
            });

            clientErrors.addAll(getErrorsInClientEntityByProgramSetting(clientTypeFieldName, insuranceContract));
        }
        return clientErrors;
    }

    private List<CheckModel> getErrorsInClientEntityByProgramSetting(String clientTypeFieldName, Insurance insuranceContract) {
        List<CheckModel> clientErrors = new ArrayList<>();
        if (insuranceContract.getProgramSetting() != null
                && !CollectionUtils.isEmpty(insuranceContract.getProgramSetting().getRequiredFieldList())
                && !(clientTypeFieldName.equalsIgnoreCase(INSURED_DATA_FIELD_NAME)
                && insuranceContract.getHolderEqualsInsured())) {

            insuranceContract.getProgramSetting().getRequiredFieldList().forEach(requiredField -> {
                CheckModel error = new CheckModel(clientTypeFieldName.concat(".".concat(requiredField.getStrId())),
                        String.format(REQUIRED_FIELD_SHOULD_BE_FILLED, requiredField.getName()),
                        CheckModelErrorType.MAYBE_SAVE_BUT_NOT_MAKE_CONCLUSION);
                try {
                    if (clientTypeFieldName.equalsIgnoreCase(HOLDER_DATA_FIELD_NAME)) {
                        clientErrors.addAll(getErrorsByClientEntity(insuranceContract.getHolder(), requiredField, error));
                    } else if (clientTypeFieldName.equalsIgnoreCase(INSURED_DATA_FIELD_NAME)) {
                        clientErrors.addAll(getErrorsByClientEntity(insuranceContract.getInsured(), requiredField, error));
                    }
                } catch (Exception e) {
                    LOGGER.error(String.format(FIELD_NOT_EXISTS, requiredField.getStrId()), e);
                    clientErrors.add(error);
                }

            });
        }
        return clientErrors;
    }

    private List<CheckModel> getErrorsByClientEntity(ClientEntity clientEntity, RequiredField requiredField, CheckModel error) throws Exception {
        List<CheckModel> clientErrors = new ArrayList<>();
        if (clientEntity != null) {
            if (isStructure(requiredField)) {
                clientErrors.addAll(getClassEmptyContentErrors(requiredField, clientEntity, error));
            } else {
                String fieldName = requiredField.getStrId();
                if (notAlwaysRequiredFields.contains(fieldName)) {
                    boolean isForeignResident = isForeignResident(clientEntity.getResident());
                    //Страна гражданства является обязательным полем только для иностранцев
                    if ("citizenshipCountry".equalsIgnoreCase(requiredField.getStrId()) && !isForeignResident) {
                        return clientErrors;
                    }
                    //Признак ИПДЛ является обязательным полем только для иностранцев
                    if ("foreignPublicOfficialType".equalsIgnoreCase(requiredField.getStrId())) {
                        if (isForeignResident &&
                                (PublicOfficialTypeEnum.FOREIGN.equals(clientEntity.getPublicOfficialStatus()) || PublicOfficialTypeEnum.FOREIGNRELATIVE.equals(clientEntity.getPublicOfficialStatus()))) {
                            clientErrors.addAll(getEmptyPropertyError(requiredField.getStrId(), clientEntity, error));
                        }
                        return clientErrors;
                    }
                    //Признак РПДЛ является обязательным полем только для рооссийских граждан
                    if ("russianPublicOfficialType".equalsIgnoreCase(requiredField.getStrId())) {
                        if (!isForeignResident &&
                                (PublicOfficialTypeEnum.RUSSIAN.equals(clientEntity.getPublicOfficialStatus()) || PublicOfficialTypeEnum.RUSSIANRELATIVE.equals(clientEntity.getPublicOfficialStatus()))) {
                            clientErrors.addAll(getEmptyPropertyError(requiredField.getStrId(), clientEntity, error));
                        }
                        return clientErrors;
                    }
                } else {
                    clientErrors.addAll(getEmptyPropertyError(requiredField.getStrId(), clientEntity, error));
                }
            }
        }
        return clientErrors;
    }

    private boolean isStructure(String requiredField) {
        return STRUCTURE_NAME_LIST.contains(requiredField);
    }

    private boolean isStructure(RequiredField requiredField) {
        return isStructure(requiredField.getStrId());
    }

    private List<CheckModel> getEmptyPropertyError(String requiredField, Object entity, CheckModel error) throws Exception {
        List<CheckModel> clientErrors = new ArrayList<>();
        Object object = getObject(requiredField, entity);
        if ((object == null
                || (object instanceof String && AppUtils.isWhitespace(object.toString()))
                || (object instanceof ArrayList && CollectionUtils.isEmpty((ArrayList) object)))) {
            clientErrors.add(error);
        }
        return clientErrors;
    }


    private boolean isForeignResident(String resident) {
        return AppUtils.isNotNullOrWhitespace(resident)
                && resident.equalsIgnoreCase(CitizenshipType.FOREIGN.name());
    }

    private List<CheckModel> getClassEmptyContentErrors(RequiredField requiredField, ClientEntity clientEntity, CheckModel parentError) {
        List<CheckModel> clientErrors = new ArrayList<>();
        CheckModel foreignDocError = new CheckModel(parentError.getKey(),
                FOREIGN_DOC_NOT_EXISTS,
                parentError.getErrorType()
        );
        if (requiredField.getStrId().equalsIgnoreCase("documents")) {
            List<DocumentForClientEntity> filteredDocs = clientEntity.getDocuments().stream()
                    .filter(f -> !IdentityDocTypeEnum.MIGRATION_CARD.equals(f.getDocType())
                            && !IdentityDocTypeEnum.TEMPORARY_RESIDENCE_PERMIT.equals(f.getDocType())
                    ).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(filteredDocs)) {
                clientErrors.add(parentError);
            } else {
                clientErrors.addAll(getErrorsByArray(filteredDocs, parentError, requiredField));
            }
        } else if (requiredField.getStrId().equalsIgnoreCase("phones")) {
            clientErrors.addAll(getErrorsByArray(clientEntity.getPhones(), parentError, requiredField));
        } else if (requiredField.getStrId().equalsIgnoreCase("addresses")) {
            clientErrors.addAll(getErrorsByArray(clientEntity.getAddresses(), parentError, requiredField));
        } else if (requiredField.getStrId().equalsIgnoreCase("foreignerDoc")) {
            List<DocumentForClientEntity> filteredDocs = clientEntity.getDocuments().stream()
                    .filter(f -> IdentityDocTypeEnum.TEMPORARY_RESIDENCE_PERMIT.equals(f.getDocType()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(filteredDocs) && isForeignResident(clientEntity.getResident())) {
                clientErrors.add(foreignDocError);
            } else {
                clientErrors.addAll(getErrorsByArray(filteredDocs, parentError, requiredField));
            }
        } else if (requiredField.getStrId().equalsIgnoreCase("migrationCard")) {
            List<DocumentForClientEntity> filteredDocs = clientEntity.getDocuments().stream()
                    .filter(f -> IdentityDocTypeEnum.MIGRATION_CARD.equals(f.getDocType()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(filteredDocs) && isForeignResident(clientEntity.getResident())) {
                clientErrors.add(foreignDocError);
            } else {
                clientErrors.addAll(getErrorsByArray(filteredDocs, parentError, requiredField));
            }
        }
        return clientErrors;
    }

    private <ClassEntity> List<CheckModel> getErrorsByArray(List<ClassEntity> entities, CheckModel parentError, RequiredField requiredField) {
        List<CheckModel> clientErrors = new ArrayList<>();
        if (CollectionUtils.isEmpty(entities)) {
            clientErrors.add(parentError);
        } else {
            for (int i = 0; i < entities.size(); i++) {
                clientErrors.addAll(getErrorsByChildrenFields(entities.get(i), requiredField,
                        parentError.getKey().concat("[".concat(String.valueOf(i)).concat("]"))));
            }
        }
        return clientErrors;
    }

    private List<CheckModel> getErrorsByChildrenFields(Object object, RequiredField requiredField, String parentKey) {
        List<CheckModel> clientErrors = new ArrayList<>();
        List<RequiredField> children = requiredFieldService.getChildren(requiredField);
        if (requiredField.getStrId().equalsIgnoreCase("documents") && object instanceof DocumentForClientEntity) {
            DocumentForClientEntity document = (DocumentForClientEntity) object;
            if (!IdentityDocTypeEnum.PASSPORT_RF.equals(document.getDocType())) {
                children = children.stream().filter(i -> !i.getStrId().equals("divisionCode")).collect(Collectors.toList());
            }
        }
        if (!CollectionUtils.isEmpty(children) && !parentKey.contains("addresses[2]")) {
            children.forEach(childField -> {
                CheckModel fieldErr = new CheckModel(
                        parentKey.concat(".".concat(childField.getStrId())),
                        String.format(REQUIRED_FIELD_SHOULD_BE_FILLED, childField.getName()),
                        CheckModelErrorType.MAYBE_SAVE_BUT_NOT_MAKE_CONCLUSION);
                try {
                    clientErrors.addAll(getEmptyPropertyError(childField.getStrId(), object, fieldErr));
                } catch (Exception e) {
                    LOGGER.error("Во время выполнения договора в сервисе проверок (этап проверки дочерних полей) произошла ошибка, причина: ", e);
                    clientErrors.add(fieldErr);
                }
            });
        }
        return clientErrors;
    }

    private Object getObject(String requiredField, Object entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String methodName;
        if ("citizenship".equals(requiredField)) {
            requiredField = "resident";
        }
        if (requiredField.startsWith("is") && !requiredField.startsWith("iss") && !Character.isLowerCase(requiredField.charAt(2))) {
            methodName = requiredField;
        } else {
            methodName = "get".concat(upCaseFirstChar(requiredField));
        }
        Method classMethod = entity.getClass().getMethod(methodName);
        return classMethod.invoke(entity);
    }

    private String upCaseFirstChar(String input) {
        return AppUtils.isNotNullOrWhitespace(input) ? input.substring(0, 1).toUpperCase().concat(input.substring(1)) : "";
    }

    private List<CheckModel> getRequiredDocumentsErrors(Insurance insuranceContract, String nextStatus) {
        List<CheckModel> contractErrors = new ArrayList<>();
        if (insuranceContract.getProgramSetting() != null && nextStatus != null
                && !CollectionUtils.isEmpty(insuranceContract.getProgramSetting().getRequiredDocumentList())) {

            insuranceContract.getProgramSetting().getRequiredDocumentList()
                    .stream().filter(document -> !Boolean.TRUE.equals(document.getRequiredDocument().getDeleted())
                    && nextStatus.equals(document.getStatus()))
                    .forEach(requiredDocument -> {
                        boolean documentNotExist;
                        if (insuranceContract.getId() == null) {
                            documentNotExist = attachmentService.isNotExistAttachByDocTypeAndContract(requiredDocument.getRequiredDocument().getId(), insuranceContract.getUuid());
                        } else {
                            documentNotExist = attachmentService.isNotExistAttachByDocTypeAndContract(requiredDocument.getRequiredDocument().getId(), insuranceContract.getId());
                        }
                        if (documentNotExist) {
                            contractErrors.add(new CheckModel("scan_".concat(requiredDocument.getRequiredDocument().getId().toString()),
                                    String.format(DOCUMENT_NOT_EXISTS, requiredDocument.getRequiredDocument().getType()),
                                    CheckModelErrorType.MAYBE_SAVE_BUT_NOT_MAKE_CONCLUSION));
                        }
                    });
        }
        return contractErrors;
    }

    private List<CheckModel> getProgramSettingErrors(Insurance insuranceContract) {
        List<CheckModel> clientErrors = new ArrayList<>();
        clientErrors.addAll(checkRecipientShare(insuranceContract));
        clientErrors.addAll(checkClientProps(insuranceContract));
        return clientErrors;
    }

    private List<CheckModel> checkRecipientShare(Insurance insuranceContract) {
        List<CheckModel> clientErrors = new ArrayList<>();
        if (insuranceContract != null) {
            List<Risk> risks = insuranceContract.getProgramSetting().getRequiredRiskSettingList().stream()
                    .map(RiskSetting::getRisk).collect(Collectors.toList());
            if (risks.size() > 0) {
                BigDecimal sumShare = getSumShare(insuranceContract.getRecipientList());
                if (isTotalShareNot100Percent(sumShare)) {
                    clientErrors.add(new CheckModel(RECIPIENT_LIST_FIELD_NAME.concat(".".concat(SHARE_FIELD_NAME)),
                            String.format(RECIPIENT_SHARE_SHOULD_BE_100, sumShare.toString()), CheckModelErrorType.MAYBE_SAVE_BUT_NOT_MAKE_CONCLUSION));
                }
            }
        }
        return clientErrors;
    }

    private boolean isTotalShareNot100Percent(BigDecimal shareSum) {
        return shareSum.compareTo(BigDecimal.valueOf(100)) != 0;
    }

    private BigDecimal getSumShare(List<InsuranceRecipientEntity> insuranceRecipientEntities) {
        if (CollectionUtils.isEmpty(insuranceRecipientEntities)) {
            return BigDecimal.ZERO;
        }
        return insuranceRecipientEntities.stream().map(InsuranceRecipientEntity::getShare).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private List<CheckModel> checkClientProps(Insurance insuranceContract) {
        List<CheckModel> clientErrors = new ArrayList<>();
        if (insuranceContract != null) {

            clientErrors.addAll(checkAgeClient(HOLDER_DATA_FIELD_NAME.concat(".".concat(BIRTHDATE_FIELD_NAME)),
                    insuranceContract.getHolder().getBirthDate(),
                    insuranceContract.getProgramSetting().getMinAgeHolder(),
                    insuranceContract.getProgramSetting().getMaxAgeHolder()));

            if (insuranceContract.getHolderEqualsInsured()) {
                clientErrors.addAll(checkAgeClient(INSURED_DATA_FIELD_NAME.concat(".".concat(BIRTHDATE_FIELD_NAME)),
                        insuranceContract.getHolder().getBirthDate(),
                        insuranceContract.getProgramSetting().getMinAgeInsured(),
                        insuranceContract.getProgramSetting().getMaxAgeInsured()));
            } else {
                clientErrors.addAll(checkAgeClient(INSURED_DATA_FIELD_NAME.concat(".".concat(BIRTHDATE_FIELD_NAME)),
                        insuranceContract.getInsured().getBirthDate(),
                        insuranceContract.getProgramSetting().getMinAgeInsured(),
                        insuranceContract.getProgramSetting().getMaxAgeInsured()));
            }
        }
        return clientErrors;
    }

    private List<CheckModel> checkAgeClient(String fieldKey, LocalDate birthDate, Integer minAge, Integer maxAge) {
        List<CheckModel> clientErrors = new ArrayList<>();
        Integer clientAge = AppUtils.getAgeByToday(birthDate);
        if (clientAge != null && ((maxAge != null && clientAge > maxAge) || (minAge != null && clientAge < minAge))) {
            clientErrors.add(new CheckModel(fieldKey,
                    String.format(CLIENT_AGE_ERROR, clientAge, minAge, maxAge),
                    CheckModelErrorType.MAYBE_SAVE_BUT_NOT_MAKE_CONCLUSION));
        }
        return clientErrors;
    }
}
