package ru.softlab.efr.services.insurance.validation;

/**
 * Константы
 * @author basharin
 * @since 02.03.2018
 */
public class ContextVariablesNames {
    public static final String CONTRACT_NUMBER = "contractNumber";

    public static final String CONTRACT_ID = "contractId";
    public static final String CONTRACT_DATA = "contractData";
    public static final String TARGET_STATUS = "targetStatus";
    public static final String HOLDER_EQUALS_INSURED = "holderEqualsInsured";
    public static final String PRINCIPAL_DATA = "principalData";
    public static final String NEED_SHOW_CHANGE_STATUS = "needShowChangeStatus";
    public static final String ERROR_NOTIFY = "errorNotify";
    //тип ru.softlab.efr.services.insurance.utils.OwnerType:
    // "HOLDER"  - страхователь
    // "INSURED" - застрахованный
    public static final String OWNER_TYPE = "ownerType";
    public static final String CLIENT_DECISION_DATA = "clientDecisionData";

    public static final String CLIENT_ID = "clientId";
    public static final String SYSTEM_ID = "systemId";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String SEARCH_CLIENT_DATA = "searchClientData";
    public static final String EMPLOYEE_DECISION = "employeeDecision";

    public static final String NEED_EMPLOYEE_ANSWER = "needEmployeeAnswer";
    public static final String NEED_CHECK_PASSPORT_DATA = "needCheckPassportData";
    public static final String DECISION_DATA = "decisionData";
    public static final String CLIENT_DATA = "clientData";
    public static final String PROGRAM_KIND = "programKind";
    public static final String INSURANCE_DECISION_DATA = "insuranceDecisionData";
    //LocalDate Фиксированная дата начала
    public static final String FIXED_DATE_START = "fixedDateStart";
    //Set<Long> Набор id проверок страхователя по разного рода спискам
    public static final String HOLDER_CHECK_IDS = "holderCheckIds";
    //Set<Long> Набор id проверок застрахованного по разного рода спискам
    public static final String INSURED_CHECK_IDS = "insuredCheckIds";
    //Set<Long> Набор id проверок для использования в подпроцессе
    public static final String CHECK_IDS = "checkIds";

    public static final String TM_RESPONSE = "tmResponse";
    public static final String TM_ERROR = "tmError";

    public static final String IS_FINISH = "isFinish";

    public static final String FOUND_DUPLICATE_CLIENT = "foundDuplicateClient";
    public static final String EDIT_CONTRACT_MODE = "editContractMode";
}
