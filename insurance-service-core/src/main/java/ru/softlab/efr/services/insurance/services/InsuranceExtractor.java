package ru.softlab.efr.services.insurance.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.dict.exchange.model.Currency;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.services.auth.EmployeesClient;
import ru.softlab.efr.services.auth.exchange.model.EmployeeDataForList;
import ru.softlab.efr.services.auth.exchange.model.OfficeData;
import ru.softlab.efr.services.insurance.exception.InteractionException;
import ru.softlab.efr.services.insurance.model.db.Extract;
import ru.softlab.efr.services.insurance.model.enums.*;
import ru.softlab.efr.services.insurance.repositories.InsuranceExtract;
import ru.softlab.efr.services.insurance.utils.ExcelReportExtractor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Сервис формирования выписки для выгрузки в Excel
 */
@Service
public class InsuranceExtractor {

    private static final Logger LOGGER = Logger.getLogger(InsuranceExtractor.class);

    private static final DateTimeFormatter ddMMyyyy = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final List<String> BASE_UNIVERSAL_TITLE = Arrays.asList("id договора", "Номер договора", "Вид страхования", "Программа страхования", "Код продукта",
            "Статус договора", "Дата заключения договора", "Дата начала договора", "Дата окончания договора", "Срок действия договора",
            "Дата анулирования договора", "Дата расторжения договора", "Дата расторжения в период охлаждения договора",
            "Размер возврата (выплаты выкупной суммы) при расторжении договора", "Дата возврата (выплаты выкупной суммы)",
            "Премия в валюте договора", "Валюта договора", "Курс валюты договора на дату заключения, руб.", "Премия в рублях",
            "Дата начала периода оплаты взносов", "Дата окончания периода оплаты взносов", "Периодичность оплаты взносов",
            "Плановая дата оплаты взносов", "Фактическая дата поступления страхового взноса", "id Страхователя", "Тип страхователя",
            "ФИО/Наименование Страхователя", "Гражданство Страхователя", "Налоговое резидентство Страхователя", "Дата рождения Страхователя",
            "Пол Страхователя", "ИНН/TIN Страхователя", "СНИЛС (для ФЛ)", "КПП Страхователя (для ЮЛ и ИП)", "Страна рождения  Страхователя",
            "Место рождения Страхователя", "E-mail Страхователя", "Телефон Страхователя", "Прописка Страхователя. Индекс", "Прописка Страхователя. Республика, край, область",
            "Прописка Страхователя. Район", "Прописка Страхователя. Город", "Прописка Страхователя. Населенный пункт", "Прописка Страхователя. Улица",
            "Прописка Страхователя. Дом", "Прописка Страхователя. Копус", "Прописка Страхователя. Строение", "Прописка Страхователя. Квартира",
            "Место пребывания Страхователя. Индекс", "Место пребывания Страхователя. Республика, край, область",
            "Место пребывания Страхователя. Район", "Место пребывания Страхователя. Город", "Место пребывания Страхователя. Населенный пункт",
            "Место пребывания Страхователя. Улица", "Место пребывания Страхователя. Дом", "Место пребывания Страхователя. Копус", "Место пребывания Страхователя. Строение",
            "Место пребывания Страхователя. Квартира", "Тип основного документа Страхователя", "Серия документа Страхователя", "Номер документа Страхователя",
            "Дата выдачи документа Страхователя", "Кем выдан документ  Страхователя", "Код подразделения выдачи документа  Страхователя",
            "Данные миграционной карты Страхователя. Серия", "Данные миграционной карты Страхователя. Номер", "Данные миграционной карты Страхователя. Дата начала срока пребывания",
            "Данные миграционной карты Страхователя. Дата окончания срока пребывания", "Наименование документа Страхователя, подтверждающего право пребывания в РФ",
            "Дата начала срока пребывания Страхователя в РФ", "Дата окончания срока пребывания Страхователя в РФ", "ПДЛ Страхователь (да/нет)",
            "Должность ПДЛ Страхователя", "Работодатель ПДЛ Страхователя", "Источник денежных средств ПДЛ Страхователя", "Сведения о бенефициарном владельце. Страхователь",
            "Сведения о характере деловых отношений. Страхователь", "Сведения о целях ФХД. Страхователь", "Сведения о целях установления  деловых отношений. Страхователь",
            "Уровень риска. Страхователь", "Сведения о деловой репутации. Страхователь", "Сведения о финансовом положении. Страхователь",
            "Сведения об источниках происхождения денежных средств. Страхователь", "id Застрахованного", "ФИО Застрахованного", "Гражданство Застрахованного",
            "Налоговое резидентство Застрахованного", "Дата рождения Застрахованного", "Пол Застрахованного", "ИНН/TIN Застрахованного",
            "СНИЛС Застрахованного", "Страна рождения  Застрахованного", "Место рождения Застрахованного", "E-mail Застрахованного", "Телефон Застрахованного",
            "Прописка Застрахованного. Индекс", "Прописка Застрахованного. Республика, край, область", "Прописка Застрахованного. Район",
            "Прописка Застрахованного. Город", "Прописка Застрахованного. Населенный пункт", "Прописка Застрахованного. Улица", "Прописка Застрахованного. Дом",
            "Прописка Застрахованного. Копус", "Прописка Застрахованного. Строение", "Прописка Застрахованного. Квартира",
            "Место пребывания Застрахованного. Индекс", "Место пребывания Застрахованного. Республика, край, область", "Место пребывания Застрахованного. Район",
            "Место пребывания Застрахованного. Город", "Место пребывания Застрахованного. Населенный пункт", "Место пребывания Застрахованного. Улица",
            "Место пребывания Застрахованного. Дом", "Место пребывания Застрахованного. Копус", "Место пребывания Застрахованного. Строение",
            "Место пребывания Застрахованного. Квартира", "Тип основного документа Застрахованного", "Серия документа Застрахованного", "Номер документа Застрахованного",
            "Дата выдачи документа Застрахованного", "Кем выдан документ Застрахованного", "Код подразделения выдачи документа Застрахованного", "Данные миграционной карты Застрахованного. Серия",
            "Данные миграционной карты Застрахованного. Номер", "Данные миграционной карты Застрахованного. Дата начала срока пребывания",
            "Данные миграционной карты Застрахованного. Дата окончания срока пребывания", "Наименование документа Застрахованного, подтверждающего право пребывания в РФ",
            "Дата начала срока пребывания Застрахованного в РФ", "Дата окончания срока пребывания Застрахованного в РФ", "ПДЛ Застрахованный (да/нет)",
            "Должность ПДЛ Застрахованного", "Работодатель ПДЛ Застрахованного", "Источник денежных средств ПДЛ Застрахованного", "Сведения о бенефициарном владельце. Застрахованный",
            "Сведения о характере деловых отношений. Застрахованный", "Сведения о целях ФХД. Застрахованный", "Сведения о целях установления  деловых отношений. Застрахованный",
            "Уровень риска. Застрахованный", "Сведения о деловой репутации. Застрахованный", "Сведения о финансовом положении. Застрахованный", "Сведения об источниках происхождения денежных средств. Застрахованный");

    private static final List<String> RESIDENCE_TITLES = Arrays.asList("Выгодоприобретатель 1 по риску Дожитие (ВП 1 Дожитие)", "Дата рождения ВП1 Дожитие", "Место рождения ВП1 Дожитие", "Адрес места жительства ВП1 Дожитие",
            "Налоговое резидентство ВП1 Дожитие", "Отношение к Застрахованного к ВП1 Дожитие", "Доля ВП1 Дожитие", "Выгодоприобретатель 1 по риску Смерть по любой причине (ВП 1 СЛП)",
            "Дата рождения ВП1 СЛП", "Место рождения ВП1 СЛП", "Адрес места жительства ВП1 СЛП", "Налоговое резидентство ВП1 СЛП", "Отношение к Застрахованного к ВП1 СЛП",
            "Доля ВП1 СЛП", "Выгодоприобретатель 2 по риску Смерть по любой причине (ВП 1 СЛП)", "Дата рождения ВП2 СЛП", "Место рождения ВП2 СЛП", "Адрес места жительства ВП2 СЛП",
            "Налоговое резидентство ВП2 СЛП", "Отношение к Застрахованного к ВП2 СЛП", "Доля ВП2 СЛП", "Выгодоприобретатель 3 по риску Смерть по любой причине (ВП 1 СЛП)",
            "Дата рождения ВП3 СЛП", "Место рождения ВП3 СЛП", "Адрес места жительства ВП3 СЛП", "Налоговое резидентство ВП3 СЛП", "Отношение к Застрахованного к ВП3 СЛП",
            "Доля ВП3 СЛП", "Выгодоприобретатель 4 по риску Смерть по любой причине (ВП 1 СЛП)", "Дата рождения ВП4 СЛП", "Место рождения ВП4 СЛП", "Адрес места жительства ВП4 СЛП",
            "Налоговое резидентство ВП4 СЛП", "Отношение к Застрахованного к ВП4 СЛП", "Доля ВП4 СЛП", "Выгодоприобретатель 5 по риску Смерть по любой причине (ВП 1 СЛП)",
            "Дата рождения ВП5 СЛП", "Место рождения ВП5 СЛП", "Адрес места жительства ВП5 СЛП", "Налоговое резидентство ВП5 СЛП", "Отношение к Застрахованного к ВП5 СЛП",
            "Доля ВП5 СЛП");
            
    private static final int AUTH_SERVICE_TIMEOUT = 90;
    
    @Autowired
    private ExtractProcessInfoService extractProcessInfoService;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private CurrencyCachedClientService currencyService;

    @Autowired
    private OrgUnitCachedClientService orgUnitService;

    @Autowired
    private EmployeesClient employeesClient;


    /**
     * Сформировать и сохранить отчет о продажам в асинхронном режиме
     *
     * @param startDate             дата начала периода отчета
     * @param endDate               дата окончания периода отчета
     * @param programKind           вид программы страхования
     * @param employeeIdFilter      id пользователя формирующего отчет, если null отчет будет сформирован по всем пользователям
     * @param employeeOfficesFilter фильтр офисов, по которым необходимо сформировать отчет
     * @param employeeGroupFilter   список групп пользователей, к которым относится пользователь, от имени которого запускается генерация отчёта
     * @param uuid                  идентификатор процесса формирования отчета
     */
    @Async("extractThreadPoolTaskExecutor")
    @Transactional
    public void createExtractAsync(boolean isAdmin, LocalDate startDate, LocalDate endDate, ProgramKind programKind,
                                   Long employeeIdFilter, Set<Long> employeeOfficesFilter, List<String> employeeGroupFilter, Set<Long> offices, String uuid) {
        createAndSaveExtract(isAdmin, startDate, endDate, programKind, employeeIdFilter, employeeOfficesFilter, employeeGroupFilter, offices, uuid);
    }

    /**
     * Сформировать и сохранить универсальный отчет в асинхронном режиме
     *
     * @param startDate             дата начала периода отчета
     * @param endDate               дата окончания периода отчета
     * @param programKind           вид программы страхования
     * @param employeeIdFilter      id пользователя формирующего отчет, если null отчет будет сформирован по всем пользователям
     * @param employeeOfficesFilter фильтр офисов, по которым необходимо сформировать отчет
     * @param employeeGroupFilter   фильтр групп пользователей, по которым необходимо сформировать отчет
     * @param uuid                  идентификатор процесса формирования отчета
     */
    @Async("extractThreadPoolTaskExecutor")
    @Transactional
    public void createUniversalExtractAsync(boolean isAdmin, LocalDate startDate, LocalDate endDate, ProgramKind programKind,
                                            Long employeeIdFilter, Set<Long> employeeOfficesFilter, List<String> employeeGroupFilter, Set<Long> offices, String uuid) {
        createAndSaveUniversalExtract(isAdmin, startDate, endDate, programKind, employeeIdFilter, employeeOfficesFilter, employeeGroupFilter, offices, uuid);
    }

    /**
     * Сформировать и сохранить отчет о продажам
     *
     * @param startDate             дата начала периода отчета
     * @param endDate               дата окончания периода отчета
     * @param programKind           вид программы страхования
     * @param employeeIdFilter      id пользователя формирующего отчет, если null отчет будет сформирован по всем пользователям
     * @param employeeOfficesFilter фильтр офисов, по которым необходимо сформировать отчет
     * @param employeeGroupFilter   список групп пользователей, к которым относится пользователь, от имени которого запускается генерация отчёта
     * @param extractUuid           идентификатор процесса формирования отчета
     */
    @Transactional
    public void createAndSaveExtract(boolean isAdmin, final LocalDate startDate, final LocalDate endDate, final ProgramKind programKind,
                                     final Long employeeIdFilter, final Set<Long> employeeOfficesFilter, final List<String> employeeGroupFilter, final Set<Long> offices, final String extractUuid) {

        try (ExcelReportExtractor extractor = new ExcelReportExtractor()) {
            String title = String.format("Договоры на страхование, оформленные с %s по %s",
                    startDate.format(ddMMyyyy), endDate.format(ddMMyyyy));
            final AtomicInteger index = new AtomicInteger(extractor.addTitle(title, getTitleExtract()));
            Map<Long, EmployeeDataForList> employeeMap = getEmployeesWithOutPermissions();
            Extract extractEntity = extractProcessInfoService.getExtractProcessInfoWithWriteLock(extractUuid);
            extractEntity.setStatus(ExtractStatus.SAVE);
            Set<String> vsp = new HashSet<>();
            try (Stream<InsuranceExtract> insurances = (employeeIdFilter == null) ?
                    insuranceService.findAllByConclusionDateBetween(isAdmin, startDate, endDate, programKind, employeeGroupFilter, offices) :
                    insuranceService.findAllByConclusionDateBetween(isAdmin, employeeOfficesFilter, employeeGroupFilter,
                            offices,
                            employeeIdFilter, startDate, endDate, programKind)) {
                insurances.forEach(insuranceExtract ->
                        extractor.addRow(convertToExtract(insuranceExtract, employeeMap, index.intValue(), vsp), index.getAndIncrement()));
            } catch (Exception ex) {
                LOGGER.error(String.format("Произошла ошибка при формировании отчета по продажам." +
                                " Дата начала периода %s, дата окончания периода %s, вид программы %s",
                        startDate, endDate, programKind), ex);
                extractEntity.setStatus(ExtractStatus.ERROR);
            }
            extractProcessInfoService.saveContentAndExtract(extractEntity, extractor.getByteArrayFromExcel());
        }
    }

    /**
     * Сформировать и сохранить универсальный отчет
     *
     * @param startDate             дата начала периода отчета
     * @param endDate               дата окончания периода отчета
     * @param programKind           вид программы страхования
     * @param employeeIdFilter      id пользователя формирующего отчет, если null отчет будет сформирован по всем пользователям
     * @param employeeOfficesFilter фильтр офисов, по которым необходимо сформировать отчет
     * @param employeeGroupFilter   фильтр групп пользователей, по которым надо сформировать отчёт
     * @param extractUuid           идентификатор процесса формирования отчета
     */
    @Transactional
    public void createAndSaveUniversalExtract(boolean isAdmin, final LocalDate startDate, final LocalDate endDate, final ProgramKind programKind,
                                              final Long employeeIdFilter, final Set<Long> employeeOfficesFilter,
                                              final List<String> employeeGroupFilter, final Set<Long> offices, final String extractUuid) {

        try (ExcelReportExtractor extractor = new ExcelReportExtractor()) {
            String title = String.format("Договоры на страхование, оформленные с %s по %s",
                    startDate.format(ddMMyyyy), endDate.format(ddMMyyyy));
            final AtomicInteger index = new AtomicInteger(extractor.addTitle(title, getHead(programKind)));
            Map<Long, EmployeeDataForList> employeeMap = getEmployeesWithOutPermissions();
            Extract extractEntity = extractProcessInfoService.getExtractProcessInfoWithWriteLock(extractUuid);
            extractEntity.setStatus(ExtractStatus.SAVE);
            List<ProgramKind> kinds = (programKind == ProgramKind.RENT || programKind == ProgramKind.NSJ) ?
                    Arrays.asList(ProgramKind.RENT, ProgramKind.NSJ) : Collections.singletonList(programKind);
            try (Stream<InsuranceExtract> insurances = (employeeIdFilter == null) ?
                    insuranceService.findAllFullByConclusionDateBetween(isAdmin, startDate, endDate, kinds, employeeGroupFilter, offices) :
                    insuranceService.findAllFullByConclusionDateBetween(isAdmin, employeeOfficesFilter,
                            employeeGroupFilter, offices, employeeIdFilter, startDate, endDate, kinds)) {
                insurances.forEach(insuranceExtract ->
                        extractor.addRow(convertToUniversalExtract(insuranceExtract, programKind, employeeMap), index.getAndIncrement()));
            } catch (Exception ex) {
                LOGGER.error(String.format("Произошла ошибка при формировании универсального отчета." +
                                " Дата начала периода %s, дата окончания периода %s, вид программы %s",
                        startDate, endDate, programKind), ex);
                extractEntity.setStatus(ExtractStatus.ERROR);
            }
            extractProcessInfoService.saveContentAndExtract(extractEntity, extractor.getByteArrayFromExcel());
        }
    }

    /**
     * Конвертация договора в представление строки выписки для универсального отчета.
     *
     * @param insurance   данные страхового договора
     * @param programKind вид программы страхования
     * @return список значений столбцов строки выписки.
     */
    private List<Object> convertToUniversalExtract(InsuranceExtract insurance, ProgramKind programKind, Map<Long, EmployeeDataForList> employeeMap) {
        switch (programKind) {
            case KSP:
                return convertToUniversalKspExtract(insurance, employeeMap);
            case RENT:
            case NSJ:
                return convertToUniversalRentExtract(insurance, employeeMap);
            case ISJ:
                return convertToUniversalIsjExtract(insurance, employeeMap);
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Конвертация договора в представление строки выписки.
     *
     * @param insurance договор
     * @return список значений столбцов строки выписки.
     */
    private List<Object> convertToUniversalKspExtract(InsuranceExtract insurance, Map<Long, EmployeeDataForList> employeeMap) {
        List<Object> data = new ArrayList<>();
        data.add(insurance.getBranchName()); //Филиал банка
        data.add(insurance.getSubdivisionName()); //ВСП
        data.add(insurance.getEmployeeName()); //"ФИО продавца"
        setPersonalNumberEmployee(insurance, data, employeeMap);//Табельный номер продавца
        data.add(insurance.getContractNumber()); //Номер договора
        data.add(insurance.getCreationDate().format(ddMMyyyy)); //Дата заключения договора
        data.add(insurance.getHolderName()); //ФИО Страхователя
        data.add(getDateAsString(insurance.getHolderBirthDate()));  //"Дата рождения"
        data.add(insurance.getHolderPhoneNumber()); //"Телефон"
        data.add(insurance.getHolderEmail()); //"E-mail"
        data.add(insurance.getProgramName()); //"Наименование страхового продукта"
        data.add(insurance.getCallCenterBranchName());      // "Филиал сотрудника колл-центра"
        data.add(insurance.getCallCenterSubdivisionName()); //"ВСП сотрудника колл-центра"
        data.add(insurance.getCallCenterEmployeeName());    //"ФИО сотрудника колл-центра"
        data.add(insurance.getCallCenterEmployeeNumber());  //"Табельный номер сотрудника колл-центра"
        if (insurance.getSource() != null) { //Источник продажи продукта
            data.add(insurance.getSource().getDescription());
        } else {
            data.add("Офис");
        }
        data.add(insurance.getType().toString()); //"Вид программы"
        data.add(insurance.getStrategyName()); //"Стратегия договора",
        data.add(getDateAsString(insurance.getConclusionDate())); //"Дата оформления договора"
        data.add(getDateAsString(insurance.getStartDate())); //"Дата начала договора"
        data.add(getDateAsString(insurance.getEndDate())); //Дата окончания договора
        data.add(getDateAsString(insurance.getCloseDate())); //Дата закрытия договора
        data.add(insurance.getStatus()); //"Статус договора"
        setCity(insurance, data); //Город оформления договора
        data.add(insurance.getDuration()); //"Период действия договора"
        data.add(insurance.getCalendarUnit() != null ? insurance.getCalendarUnit().toString() : ""); //"Календарная единица периода действия"
        data.add(""); //todo //"Период оплаты договора"
        data.add(insurance.getPeriodicity() != null ? insurance.getPeriodicity().toString() : ""); // Периодичность оплаты (в месяцах)
        setCurrency(insurance, data);// Валюта договора
        data.add(insurance.getPremium()); // Страховая премия в валюте договора
        data.add(insurance.getRurPremium()); //Страховая премия в национальной валюте
        data.add(insurance.getAmount());     // Страховой взнос в валюте договора
        data.add(insurance.getRurAmount());  // Страховой взнос в национальной валюте
        data.add(""); //todo // Категория продавца (ФК/розница), Значение сегмента из справочника пользователей.
        data.add(insurance.getExchangeRate()); // Курс валюты договора на дату заключения.
        return data;
    }

    /**
     * Конвертация договора в представление строки выписки.
     *
     * @param insurance договор
     * @return список значений столбцов строки выписки.
     */
    private List<Object> convertToUniversalIsjExtract(InsuranceExtract insurance, Map<Long, EmployeeDataForList> employeeMap) {
        List<Object> data = new ArrayList<>();

        setBaseUniversalDataFirstPart(insurance, data);
        data.add(insurance.getPremium());     //  "Премия в валюте договора",
        setCurrency(insurance, data);// "Валюта договора",
        data.add(insurance.getExchangeRate());// "Курс валюты договора на дату заключения, руб.",
        data.add(insurance.getRurPremium());// "Премия в рублях",
        data.add(getDateAsString(insurance.getConclusionDate())); //  "Дата начала периода оплаты взносов",
        data.add("");// "Дата окончания периода оплаты взносов",
        data.add(insurance.getPeriodicity() != null ? insurance.getPeriodicity().toString() : ""); //"Периодичность оплаты взносов",
        data.add("");      // "Плановая дата оплаты взносов",
        setBaseUniversalDataSecondPart(insurance, data);
        for (int i = 1; i <= 10; i++) {
            setRiskAmount(insurance, data, i);
        }
        data.add(insurance.getAmount());// СС по Дожитию",//СС по риску Дожитие - это СС по договору
        data.add(insurance.getRisk2Amount());// "СС   в валюте договора по риску Смерть по любой причине",
        data.add(insurance.getRisk2RurAmount());// "СС   в рублях по риску Смерть по любой причине",
        data.add(insurance.getRisk3Amount());// "СС   в валюте договора по риску НС",
        data.add(insurance.getRisk3RurAmount());// "СС в рублях по риску НС",
        data.add(insurance.getRisk4Amount());// "СС   в валюте договора по риску кораблекрушение",
        data.add(insurance.getRisk4RurAmount());// "СС в рублях по риску кораблекрушение",
        setRecipientData(insurance, data);
        data.add(insurance.getStrategyName());// "Стратегия инвестирования",
        data.add(insurance.getStrategyRate());// •	Коэффициент участия 1
        data.add(getDateAsString(insurance.getNzbaDate()));// •	Дата НЗБИ
        data.add(getDateAsString(insurance.getExpirationDate()));// •	Дата экспирации
        data.add(insurance.getRate2());// •	Коэффициент участия 2
        data.add(getDateAsString(insurance.getNzbaDate2()));// •	Дата НЗБИ 2
        data.add(getDateAsString(insurance.getExpirationDate2()));// •	Дата экспирации 2
        data.add("");// "Валюта гарантийного фонда",
        data.add("");// "Валюта рискового фонда",
        data.add("");// "Величина инвестиционного купона",
        setEmploeeData(insurance, data, employeeMap);
        data.add(insurance.getSegment());// "Сегмент продукта",
        data.add("");// "Получено согласие на участие в конкурсе (да/нет)",
        data.add(Boolean.TRUE.equals(insurance.getFullSetDocument()) ? "Да" : "Нет");// "Документы в ПО прикреплены (да/нет)",
        data.add(insurance.getCommentForNotFullSetDocument());// "Комментарии к прикрепленным документам",
        data.add("");// "Наличие медицинской анкеты",
        data.add("");// "Наличие финансовой анкеты"

        return data;
    }

    /**
     * Конвертация договора в представление строки выписки.
     *
     * @param insurance договор
     * @return список значений столбцов строки выписки.
     */
    private List<Object> convertToUniversalRentExtract(InsuranceExtract insurance, Map<Long, EmployeeDataForList> employeeMap) {
        List<Object> data = new ArrayList<>();
        setBaseUniversalDataFirstPart(insurance, data);
        BigDecimal duration = BigDecimal.valueOf(getDurationInYear(insurance.getCalendarUnit(), insurance.getDuration()));
        data.add(insurance.getPremium().multiply(duration));     //  "Премия в валюте договора",
        setCurrency(insurance, data);// "Валюта договора",
        data.add(insurance.getExchangeRate());// "Курс валюты договора на дату заключения, руб.",
        data.add(insurance.getRurPremium().multiply(duration));// "Премия в рублях",
        data.add(insurance.getPremium()); //Взнос в валюте договора
        data.add(insurance.getRurPremium()); //Взнос в  рублях
        data.add(getDateAsString(insurance.getConclusionDate())); //  "Дата начала периода оплаты взносов",
        data.add(getDateAsString(insurance.getEndDate()));// "Дата окончания периода оплаты взносов",
        data.add(insurance.getPeriodicity() != null ? insurance.getPeriodicity().toString() : ""); //"Периодичность оплаты взносов",
        data.add(getDateAsString(insurance.getStartDate()));      // "Плановая дата оплаты взносов",
        setBaseUniversalDataSecondPart(insurance, data);
        LocalDate conclusionDate = insurance.getConclusionDate();
        LocalDate cumulativePeriodStart = null;
        LocalDate cumulativePeriodEnd = null;
        if (conclusionDate != null) {
            int coolingPeriod = insurance.getCoolingPeriod() == null ? 0 : insurance.getCoolingPeriod();
            cumulativePeriodStart = conclusionDate.plusDays(coolingPeriod);
            cumulativePeriodEnd = getEndDateByCalendarUnit(cumulativePeriodStart, insurance.getCalendarUnit(), insurance.getDuration());
        }
        //Общий размер страховой ренты, подлежащей выплате в течение всего Периода выплаты ренты.
        if (insurance.getType().equals(ProgramKind.NSJ)){
            data.add(insurance.getPremium().multiply(duration));
        } else {
            data.add(getRentAmount(insurance.getPaymentTerm(), insurance.getAmount()));// "СС по риску Дожитие",
        }
        //(дата оформления договора + срок накопительного периода + период охлаждения
        data.add(getDateAsString(cumulativePeriodEnd));// "Дата дожития",
        data.add(insurance.getPremium());// "СС по риску СЛП в Накопительный период",
        data.add("");// "СС по риску СЛП в период выплаты ренты", //не заполняем
        data.add(insurance.getAccidentRiskAmount());// "СС по риску Смерть от НС",
        data.add(insurance.getPremium().multiply(duration).subtract(insurance.getPremium()));// "СС по риску Инвалидность I группы в результате НС (ОУСВ)", //не заполняем
        data.add(insurance.getPremium().multiply(duration));// "СС по риску Инвалидность I, II группы в результате НС",//не заполняем
        data.add("");// "СС по риску 7",//не заполняем
        data.add("");// "СС по риску 8",//не заполняем
        data.add("");// "СС по риску 9",//не заполняем
        data.add("");// "СС по риску 10",//не заполняем
        data.add("");// "СС по риску 11",//не заполняем
        data.add("");// "СС по риску 12",//не заполняем
        data.add("");// "СС по риску 13",//не заполняем
        data.add("");// "СС по риску 14",//не заполняем
        data.add("");// "СС по риску 15",//не заполняем
        data.add("");// "СС по риску 16",//не заполняем
        data.add("");// "СС по риску 17",//не заполняем
        data.add("");// "СС по риску 18",//не заполняем
        data.add("");// "СС по риску 19",//не заполняем
        data.add("");// "СС по риску 20",//не заполняем
        data.add("");// "СС по риску 21",//не заполняем
        data.add("");// "СС по риску 22"//не заполняем
        setRecipientData(insurance, data);

        if (conclusionDate != null) {
            data.add(getDateAsString(cumulativePeriodStart));// "Дата начала накопительного периода",
            data.add(getDateAsString(cumulativePeriodEnd));// "Дата окончания накопительного периода",
//            data.add(cumulativePeriodEnd != null ? getDateAsString(cumulativePeriodEnd.plusDays(1)) : "");// "Дата начала периода выплаты ренты",
            data.add("");// "Дата начала периода выплаты ренты",
            Integer payTerm = insurance.getPaymentTerm();
            LocalDate rentContractEndDate = payTerm != null && cumulativePeriodEnd != null ? cumulativePeriodEnd.plusYears(payTerm) : null;
            data.add(getDateAsString(rentContractEndDate));// "Дата окончания периода выплаты ренты",
        } else {
            data.add("");// "Дата начала накопительного периода",
            data.add("");// "Дата окончания накопительного периода",
            data.add("");// "Дата начала периода выплаты ренты",
            data.add("");// "Дата окончания периода выплаты ренты",
        }
//        data.add(insurance.getAmount());// "Размер ежемесячной страховой ренты",
        data.add("");// "Размер ежемесячной страховой ренты",
        setEmploeeData(insurance, data, employeeMap);
        data.add(insurance.getSegment());// "Сегмент продукта",
        data.add("");// "Получено согласие на участие в конкурсе (да/нет)",
        data.add(Boolean.TRUE.equals(insurance.getFullSetDocument()) ? "Да" : "Нет");// "Документы в ПО прикреплены (да/нет)",
        data.add(insurance.getCommentForNotFullSetDocument());// "Комментарии к прикрепленным документам",
        data.add("");// "Наличие медицинской анкеты",
        data.add("");// "Наличие финансовой анкеты"

        return data;
    }

    /**
     * Конвертация договора в представление строки выписки отчета по продажам.
     *
     * @param insurance договор
     * @return список значений столбцов строки выписки.
     */
    private List<Object> convertToExtract(InsuranceExtract insurance, Map<Long, EmployeeDataForList> employeeMap, int index, Set<String> vsp) {

        List<Object> data = new ArrayList<>();
        data.add(index-2);                 //№ п/п.
        if (insurance.getSource() != null) { //Источник продажи продукта
            data.add(insurance.getSource().getDescription());
        } else {
            data.add("Офис");
        }
        data.add(insurance.getProgramName());                     //Наименование программы.
        data.add(insurance.getContractNumber());                  //Уникальный код клиента
        data.add(insurance.getHolderName());                      //ФИО клиента.
        data.add(insurance.getHolderPhoneNumber());               //Номер мобильного телефона клиента.
        data.add(insurance.getConclusionDate().format(ddMMyyyy)); //Дата оформления договора.
        data.add(getDateAsString(insurance.getEndDate()));        //Дата окончания договора.
        data.add(insurance.getStatus());                          //"Статус договора"
        data.add(insurance.getBranchName());                      //РФ
        data.add(insurance.getSubdivisionName());                 //ВСП
        setCity(insurance, data);                                 //Город оформления договора
        data.add(insurance.getDuration());                        //"Период действия договора"
        data.add(insurance.getAmount());                          //Сумма оплаты по договору.
        data.add(insurance.getEmployeeName());                    //ФИО сотрудника, заключившего договор
        setPersonalNumberEmployee(insurance, data, employeeMap);  //Табельный номер сотрудника.

        vsp.add(insurance.getSubdivisionName());
        return data;
    }

    private void setEmploeeData(InsuranceExtract insurance, List<Object> data, Map<Long, EmployeeDataForList> employeeMap) {
        data.add("");//"Наименование агента",
        data.add("");// "% КВ Агенту",
        data.add("");// "КВ Агенту в рублях",
        data.add(insurance.getEmployeeName());// "ФИО оформившего сотрудника",
        setPersonalNumberEmployee(insurance, data, employeeMap);// "Таб. номер сотрудника",
        data.add("");// "Категория продавца (ФК/Розница)",
        data.add(insurance.getSubdivisionName());// "ВСП",
        setCity(insurance, data); //Город
        data.add(insurance.getBranchName());// "Филиал",
    }

    private void setBaseUniversalDataFirstPart(InsuranceExtract insurance, List<Object> data) {
        data.add(insurance.getId()); //id договора",
        data.add(insurance.getContractNumber()); //Номер договора
        data.add(insurance.getType().toString());// "Вид страхования"
        data.add(insurance.getProgramName());// "Программа страхования",
        data.add(insurance.getProgramCode()); // "Код продукта",
        data.add(insurance.getStatus()); //"Статус договора"
        LocalDate conclusionDate = insurance.getConclusionDate() == null ? insurance.getCreationDate().toLocalDate() : insurance.getConclusionDate();
        data.add(conclusionDate.format(ddMMyyyy));// "Дата заключения договора",
        data.add(getDateAsString(insurance.getStartDate()));// "Дата начала договора",
        data.add(getDateAsString(insurance.getEndDate())); // "Дата окончания договора",
        data.add(insurance.getDuration()); // "Срок действия договора",
        data.add("");//не заполняем //"Дата анулирования договора",
        data.add("");//не заполняем //"Дата расторжения договора",
        data.add("");//не заполняем //"Расторгнут в период охлаждения договора (да/нет)",
        data.add("");//не заполняем //"Размер возврата (выплаты выкупной суммы) при расторжении договора",
        data.add("");//не заполняем //"Дата возврата (выплаты выкупной суммы)",
    }

    private void setBaseUniversalDataSecondPart(InsuranceExtract insurance, List<Object> data) {
        data.add("");//не заполняем //"Фактическая дата поступления страхового взноса",
        data.add(insurance.getHolderId());// "id Страхователя",
        data.add("Физическое лицо");// "Тип страхователя",
        data.add(insurance.getHolderName()); //  "ФИО/Наименование Страхователя"
        data.add(insurance.getHolderResident());// "Гражданство Страхователя",
        data.add(insurance.getHolderTaxResidence() != null ? insurance.getHolderTaxResidence().toString() : "");// "Налоговое резидентство Страхователя",
        data.add(getDateAsString(insurance.getHolderBirthDate())); // "Дата рождения Страхователя",
        data.add(insurance.getHolderGender() != null ? insurance.getHolderGender().toString() : "");  //  "Пол Страхователя",
        data.add(insurance.getHolderTaxPayerNumber());// "ИНН/TIN Страхователя",
        data.add(insurance.getHolderSnils());// "СНИЛС (для ФЛ)",
        data.add("");// "КПП Страхователя (для ЮЛ и ИП)",
        data.add(insurance.getHolderBirthCountry());// "Страна рождения  Страхователя",
        data.add(insurance.getHolderBirthPlace());// "Место рождения Страхователя",
        data.add(insurance.getHolderEmail()); // "E-mail Страхователя",
        data.add(insurance.getHolderPhoneNumber());// "Телефон Страхователя",
        data.add(insurance.getHolderRegistrationIndex());// "Прописка Страхователя. Индекс",
        data.add(concatAddress(insurance.getHolderRegistrationRegion(), "Регион"));// "Прописка Страхователя. Республика, край, область",
        data.add(concatAddress(insurance.getHolderRegistrationArea(), "Район"));// "Прописка Страхователя. Район",
        data.add(concatAddress(insurance.getHolderRegistrationCity(), "Город"));// "Прописка Страхователя. Город",
        data.add(concatAddress(insurance.getHolderRegistrationLocality(), "Населенный пункт"));// "Прописка Страхователя. Населенный пункт",
        data.add(concatAddress(insurance.getHolderRegistrationStreet(), "Улица"));// "Прописка Страхователя. Улица",
        data.add(concatAddress(insurance.getHolderRegistrationHouse(), "Дом"));// "Прописка Страхователя. Дом",
        data.add(concatAddress(insurance.getHolderRegistrationConstruction(), "Корпус"));// "Прописка Страхователя. Копус",
        data.add(concatAddress(insurance.getHolderRegistrationHousing(), "Строение"));// "Прописка Страхователя. Строение",
        data.add(concatAddress(insurance.getHolderRegistrationApartment(), "Квартира"));// "Прописка Страхователя. Квартира",
        data.add(insurance.getHolderResidenceIndex());// "Место пребывания Страхователя. Индекс",
        data.add(concatAddress(insurance.getHolderResidenceRegion(), "Регион"));// "Место пребывания Страхователя. Республика, край, область",
        data.add(concatAddress(insurance.getHolderResidenceArea(), "Район"));//   "Место пребывания Страхователя. Район",
        data.add(concatAddress(insurance.getHolderResidenceCity(), "Город"));// "Место пребывания Страхователя. Город",
        data.add(concatAddress(insurance.getHolderResidenceLocality(), "Населенный пункт"));// "Место пребывания Страхователя. Населенный пункт",
        data.add(concatAddress(insurance.getHolderResidenceStreet(), "Улица"));//     "Место пребывания Страхователя. Улица",
        data.add(concatAddress(insurance.getHolderResidenceHouse(), "Дом"));// "Место пребывания Страхователя. Дом",
        data.add(concatAddress(insurance.getHolderResidenceConstruction(), "Корпус"));// "Место пребывания Страхователя. Копус",
        data.add(concatAddress(insurance.getHolderResidenceHousing(), "Строение"));// "Место пребывания Страхователя. Строение",
        data.add(concatAddress(insurance.getHolderResidenceApartment(), "Квартира"));//   "Место пребывания Страхователя. Квартира",
        data.add(insurance.getHolderDocType());// "Тип основного документа Страхователя",
        data.add(insurance.getHolderDocSeria());// "Серия документа Страхователя",
        data.add(insurance.getHolderDocNumber());// "Номер документа Страхователя",
        data.add(getDateAsString(insurance.getHolderDocIssuedDate()));// "Дата выдачи документа Страхователя",
        data.add(insurance.getHolderDocIssuedBy());// "Кем выдан документ  Страхователя",
        data.add(formatDivisionCode(insurance.getHolderDocDivisionCode()));//"Код подразделения выдачи документа  Страхователя",
        data.add(insurance.getHolderMigrationCardSeriesy());// "Данные миграционной карты Страхователя. Серия",
        data.add(insurance.getHolderMigrationCardNumber());// "Данные миграционной карты Страхователя. Номер",
        data.add(getDateAsString(insurance.getHolderMigrationCardStartDate()));// "Данные миграционной карты Страхователя. Дата начала срока пребывания",
        data.add(getDateAsString(insurance.getHolderMigrationCardEndDate()));// "Данные миграционной карты Страхователя. Дата окончания срока пребывания",
        data.add(insurance.getHolderForeignRegDocName());// "Наименование документа Страхователя, подтверждающего право пребывания в РФ",
        data.add(getDateAsString(insurance.getHolderForeignRegDocStartDate()));//   "Дата начала срока пребывания Страхователя в РФ",
        data.add(getDateAsString(insurance.getHolderForeignRegDocEndDate()));// "Дата окончания срока пребывания Страхователя в РФ",
        data.add(insurance.getHolderPublicOfficial());// "ПДЛ Страхователь (да/нет)",
        data.add(insurance.getHolderPublicOfficialPosition());//  "Должность ПДЛ Страхователя",
        data.add("");// отсутствует поле //"Работодатель ПДЛ Страхователя",
        data.add("");// отсутствует поле // "Источник денежных средств ПДЛ Страхователя",
        data.add(insurance.getHolderBeneficialOwner());// "Сведения о бенефициарном владельце. Страхователь",
        data.add(insurance.getHolderBusinessRelations());// "Сведения о характере деловых отношений. Страхователь",
        data.add(insurance.getHolderActivitiesGoal());// "Сведения о целях ФХД. Страхователь",
        data.add(insurance.getHolderBusinessRelationsGoal());// "Сведения о целях установления  деловых отношений. Страхователь",
        data.add(insurance.getHolderRiskLevel());// "Уровень риска. Страхователь",
        data.add(insurance.getHolderBusinessReputation());// "Сведения о деловой репутации. Страхователь",
        data.add(insurance.getHolderFinancialStability());// "Сведения о финансовом положении. Страхователь",
        data.add(insurance.getHolderFinancesSource());// "Сведения об источниках происхождения денежных средств. Страхователь",
        data.add(insurance.getInsuredId());// "id Застрахованного",
        data.add(insurance.getInsuredName());// "ФИО Застрахованного",
        data.add(insurance.getInsuredResident());// "Гражданство Застрахованного",
        data.add(insurance.getInsuredTaxResidence() != null ? insurance.getInsuredTaxResidence().toString() : "");//   "Налоговое резидентство Застрахованного",
        data.add(getDateAsString(insurance.getInsuredBirthDate()));// "Дата рождения Застрахованного",
        data.add(insurance.getInsuredGender() != null ? insurance.getInsuredGender().toString() : "");// "Пол Застрахованного",
        data.add(insurance.getInsuredTaxPayerNumber());// "ИНН/TIN Застрахованного",
        data.add(insurance.getInsuredSnils());//   "СНИЛС Застрахованного",
        data.add(insurance.getInsuredBirthCountry());// "Страна рождения  Застрахованного",
        data.add(insurance.getInsuredBirthPlace());// "Место рождения Застрахованного",
        data.add(insurance.getInsuredEmail());// "E-mail Застрахованного",
        data.add(insurance.getInsuredPhoneNumber());// "Телефон Застрахованного",
        data.add(insurance.getInsuredRegistrationIndex());//  "Прописка Застрахованного. Индекс",
        data.add(concatAddress(insurance.getInsuredRegistrationRegion(), "Регион"));// "Прописка Застрахованного. Республика, край, область",
        data.add(concatAddress(insurance.getInsuredRegistrationArea(), "Район"));// "Прописка Застрахованного. Район",
        data.add(concatAddress(insurance.getInsuredRegistrationCity(), "Город"));//      "Прописка Застрахованного. Город",
        data.add(concatAddress(insurance.getInsuredRegistrationLocality(), "Населенный пункт"));// "Прописка Застрахованного. Населенный пункт",
        data.add(concatAddress(insurance.getInsuredRegistrationStreet(), "Улица"));// "Прописка Застрахованного. Улица",
        data.add(concatAddress(insurance.getInsuredRegistrationHouse(), "Дом"));// "Прописка Застрахованного. Дом",
        data.add(concatAddress(insurance.getInsuredRegistrationConstruction(), "Корпус"));//   "Прописка Застрахованного. Копус",
        data.add(concatAddress(insurance.getInsuredRegistrationHousing(), "Строение"));// "Прописка Застрахованного. Строение",
        data.add(concatAddress(insurance.getInsuredRegistrationApartment(), "Квартира"));// "Прописка Застрахованного. Квартира",
        data.add(insurance.getInsuredResidenceIndex());      // "Место пребывания Застрахованного. Индекс",
        data.add(concatAddress(insurance.getInsuredResidenceRegion(), "Регион"));// "Место пребывания Застрахованного. Республика, край, область",
        data.add(concatAddress(insurance.getInsuredResidenceArea(), "Район"));// "Место пребывания Застрахованного. Район",
        data.add(concatAddress(insurance.getInsuredResidenceCity(), "Город"));//"Место пребывания Застрахованного. Город",
        data.add(concatAddress(insurance.getInsuredResidenceLocality(), "Населенный пункт"));// "Место пребывания Застрахованного. Населенный пункт",
        data.add(concatAddress(insurance.getInsuredResidenceStreet(), "Улица"));// "Место пребывания Застрахованного. Улица",
        data.add(concatAddress(insurance.getInsuredResidenceHouse(), "Дом"));//"Место пребывания Застрахованного. Дом",
        data.add(concatAddress(insurance.getInsuredResidenceConstruction(), "Корпус"));// "Место пребывания Застрахованного. Копус",
        data.add(concatAddress(insurance.getInsuredResidenceHousing(), "Строение"));// "Место пребывания Застрахованного. Строение",
        data.add(concatAddress(insurance.getInsuredResidenceApartment(), "Квартира"));//"Место пребывания Застрахованного. Квартира",
        data.add(insurance.getInsuredDocType());// "Тип основного документа Застрахованного",
        data.add(insurance.getInsuredDocSeria());// "Серия документа Застрахованного",
        data.add(insurance.getInsuredDocNumber());// "Номер документа Застрахованного",
        data.add(getDateAsString(insurance.getInsuredDocIssuedDate()));//"Дата выдачи документа Застрахованного",
        data.add(insurance.getInsuredDocIssuedBy());// "Кем выдан документ Застрахованного",
        data.add(formatDivisionCode(insurance.getInsuredDocDivisionCode())); // "Код подразделения выдачи документа Застрахованного",
        data.add(insurance.getInsuredMigrationCardSeriesy());// "Данные миграционной карты Застрахованного. Серия",
        data.add(insurance.getInsuredMigrationCardNumber());//"Данные миграционной карты Застрахованного. Номер",
        data.add(insurance.getInsuredMigrationCardStartDate());// "Данные миграционной карты Застрахованного. Дата начала срока пребывания",
        data.add(insurance.getInsuredMigrationCardEndDate());//"Данные миграционной карты Застрахованного. Дата окончания срока пребывания",
        data.add(insurance.getInsuredForeignRegDocName());// "Наименование документа Застрахованного, подтверждающего право пребывания в РФ",
        data.add(insurance.getInsuredForeignRegDocStartDate());//"Дата начала срока пребывания Застрахованного в РФ",
        data.add(insurance.getInsuredForeignRegDocEndDate());// "Дата окончания срока пребывания Застрахованного в РФ",
        data.add(insurance.getInsuredPublicOfficial());// "ПДЛ Застрахованный (да/нет)",
        data.add(insurance.getInsuredPublicOfficialPosition());//"Должность ПДЛ Застрахованного",
        data.add("");// отсутствует поле// "Работодатель ПДЛ Застрахованного",
        data.add("");// отсутствует поле// "Источник денежных средств ПДЛ Застрахованного",
        data.add(insurance.getInsuredBeneficialOwner());// "Сведения о бенефициарном владельце. Страхователь",
        data.add(insurance.getInsuredBusinessReputation());//"Сведения о характере деловых отношений. Страхователь",
        data.add(insurance.getInsuredActivitiesGoal());// "Сведения о целях ФХД. Страхователь",
        data.add(insurance.getInsuredBusinessRelationsGoal());// "Сведения о целях установления  деловых отношений. Страхователь",
        data.add(insurance.getInsuredRiskLevel());//"Уровень риска. Страхователь",
        data.add(insurance.getInsuredBusinessReputation());// "Сведения о деловой репутации. Страхователь",
        data.add(insurance.getInsuredFinancialStability());// "Сведения о финансовом положении. Страхователь",
        data.add(insurance.getInsuredFinancesSource());// "Сведения об источниках происхождения денежных средств. Страхователь",
    }

    private void setRecipientData(InsuranceExtract insurance, List<Object> data) {

        if (insurance.hasSurvivalRisk()) {
            data.add(insurance.getSurvivalRiskHasRecipient() ? insurance.getRecipient1().getName() : insurance.getInsuredName());//"Выгодоприобретатель 1 по риску Дожитие (ВП 1 Дожитие)",
            data.add(insurance.getSurvivalRiskHasRecipient() ? getDateAsString(insurance.getRecipient1().getBirthDate()) :
                    getDateAsString(insurance.getInsuredBirthDate()));// "Дата рождения ВП1 Дожитие",
            data.add(insurance.getSurvivalRiskHasRecipient() ? insurance.getRecipient1().getBirthPlace() : insurance.getInsuredBirthPlace());// "Место рождения ВП1 Дожитие",
            data.add("");// "Адрес места жительства ВП1 Дожитие",
            if (insurance.getSurvivalRiskHasRecipient()) {
                data.add(insurance.getRecipient1().getTaxResidence() != null ? insurance.getRecipient1().getTaxResidence().toString() : "");//"Налоговое резидентство ВП1 Дожитие",
            } else {
                data.add(insurance.getInsuredTaxResidence() != null ? insurance.getInsuredTaxResidence().toString() : "");//"Налоговое резидентство ВП1 Дожитие",
            }
            data.add("");// "Отношение к Застрахованного к ВП1 Дожитие",
            data.add(insurance.getSurvivalRiskHasRecipient() ? insurance.getRecipient1().getShare() : 100);// "Доля ВП1 Дожитие",
        } else {
            addEmptyData(data, 7);
        }
        if (insurance.hasDeathRisk()) {
            data.add(insurance.getDeathRiskHasRecipient() ? insurance.getRecipient1().getName() : insurance.getInsuredName());// "Выгодоприобретатель 1 по риску Смерть по любой причине (ВП 1 СЛП)",
            data.add(insurance.getDeathRiskHasRecipient() ? getDateAsString(insurance.getRecipient1().getBirthDate()) :
                    getDateAsString(insurance.getInsuredBirthDate()));//"Дата рождения ВП1 СЛП",
            data.add(insurance.getDeathRiskHasRecipient() ? insurance.getRecipient1().getBirthPlace() : insurance.getHolderBirthPlace());// "Место рождения ВП1 СЛП",
            data.add("");// "Адрес места жительства ВП1 СЛП",
            if (insurance.getDeathRiskHasRecipient()) {
                data.add(insurance.getRecipient1().getTaxResidence() != null ? insurance.getRecipient1().getTaxResidence().toString() : "");// "Налоговое резидентство ВП1 СЛП",
            } else {
                data.add(insurance.getInsuredTaxResidence() != null ? insurance.getInsuredTaxResidence().toString() : "");// "Налоговое резидентство ВП1 СЛП",
            }
            data.add("");// "Отношение к Застрахованного к ВП1 СЛП",
            data.add(insurance.getDeathRiskHasRecipient() ? insurance.getRecipient1().getShare() : 100);//"Доля ВП1 СЛП",
        } else {
            addEmptyData(data, 7);
        }

        if (insurance.hasDeathRisk() && insurance.getDeathRiskHasRecipient()) {
            setRecipientData(data, insurance.getRecipient2());
            setRecipientData(data, insurance.getRecipient3());
            setRecipientData(data, insurance.getRecipient4());
            setRecipientData(data, insurance.getRecipient5());
        } else {
            addEmptyData(data, 4 * 7);
        }
    }

    private void setRecipientData(List<Object> data, InsuranceExtract.Recipient recipient) {
        data.add(recipient.getName());// "Выгодоприобретатель  по риску Смерть по любой причине (ВП 1 СЛП)",
        data.add(getDateAsString(recipient.getBirthDate()));//"Дата рождения ВП СЛП",
        data.add(recipient.getBirthPlace());// "Место рождения ВП СЛП",
        data.add("");// "Адрес места жительства ВП СЛП",
        data.add(recipient.getTaxResidence() != null ? recipient.getTaxResidence().toString() : "");// "Налоговое резидентство ВП СЛП",
        data.add("");// "Отношение к Застрахованного к ВП СЛП",
        data.add(recipient.getShare());//"Доля ВП СЛП",
    }

    private void setRiskAmount(InsuranceExtract insurance, List<Object> data, int period) {
        if (insurance.getDuration() >= period && insurance.getCalculationType() == RiskCalculationTypeEnum.DEPENDS_ON_CONTRACT_PREMIUM
                && insurance.getPremium() != null) {
            if (insurance.getCalculationSumTypeEnum() == RiskCalculationSumTypeEnum.CONSTANT && insurance.getCalculationCoefficient() != null) {
                BigDecimal amount = insurance.getPremium().multiply(insurance.getCalculationCoefficient());
                data.add(amount);
                data.add(insurance.getExchangeRate() != null ? amount.multiply(insurance.getExchangeRate()) : amount);
                data.add(insurance.getStartDate().plusYears(period));
            } else if (insurance.getCalculationSumTypeEnum() == RiskCalculationSumTypeEnum.DECREASING) {
                BigDecimal amount = insurance.getPremium().multiply(new BigDecimal(insurance.getDuration())).subtract(insurance.getPremium().multiply(new BigDecimal(period)));
                data.add(amount);
                data.add(insurance.getExchangeRate() != null ? amount.multiply(insurance.getExchangeRate()) : amount);
                data.add(insurance.getStartDate().plusYears(period));
            } else if (insurance.getCalculationSumTypeEnum() == RiskCalculationSumTypeEnum.INCREASING) {
                BigDecimal amount = insurance.getPremium().multiply(new BigDecimal(period));
                data.add(amount);
                data.add(insurance.getExchangeRate() != null ? amount.multiply(insurance.getExchangeRate()) : amount);
                data.add(insurance.getStartDate().plusYears(period));
            } else {
                data.add("");
                data.add("");
                data.add("");
            }
        } else {
            data.add("");
            data.add("");
            data.add("");
        }
    }

    private List<String> getHead(ProgramKind programKind) {
        switch (programKind) {
            case KSP:
                return getTitleUniversalKspExtract();
            case RENT:
            case NSJ:
                return getTitleUniversalRentExtract();
            case ISJ:
                return getTitleUniversalIsjExtract();
            default:
                return new ArrayList<>();
        }
    }

    private List<String> getTitleExtract() {
        return Arrays.asList("№ п/п.", "Источник продажи программы", "Наименование программы", "Идентификационный номер Сертификата",
                "ФИО клиента", "Номер мобильного телефона клиента", "Дата оформления договора",
                "Дата окончания договора", "Статус договора", "РФ оформления договора", "ВСП оформления договора", "Город оформления договора",
                "Период действия договора", "Стоимость программы", "ФИО сотрудника, заключившего договор", "Табельный номер сотрудника");
    }

//    private List<String> getTitleExtract1() {
//        return Arrays.asList("Вид Страхования", "Дата оформления", "Номер договора страхования", "Программа/Страховой продукт", "РФ",
//                "Город", "Номер ВСП", "Адрес точки продаж ВСП", "Табельный номер продавца", "Категория продавца", "ФИО Продавца",
//                "Дата начала действия договора", "Дата окончания действия договора", "Статус", "Дата расторжения в период охлаждения договора",
//                "Дата расторжения", "Стратегия (для ИСЖ)", "Срок, лет",
//                "Валюта", "Страховой взнос \n(по договору) в валюте", "Поступивший страховой взнос за период, руб.", "Дата плановой оплаты страхового взноса",
//                "Дата фактического поступления страхового взноса", "Периодичность оплаты", "Наличие сканов  документов", "Наличие оригиналов  документов",
//                "ФИО Страхователя", "Дата рождения Страхователя", "Курс валюты договора на дату заключения, руб.", "Коэффициент участия/инвестиционный купон",
//                "Договор оформлен сотрудником КЦ (да/нет)");
//    }

    private List<String> getTitleUniversalKspExtract() {
        return Arrays.asList("Филиал банка", "ВСП", "ФИО продавца", "Табельный номер", "Номер договора",
                "Дата заключения договора", "ФИО", "Дата рождения", "Телефон", "E-mail", "Наименование страхового продукта",
                "Филиал сотрудника колл-центра", "ВСП сотрудника колл-центра", "ФИО сотрудника колл-центра", "Табельный номер сотрудника колл-центра",
                "Источник продажи продукта", "Вид страхования", "Стратегия договора",
                "Дата оформления договора", "Дата начала договора", "Дата окончания договора", "Дата закрытия договора",
                "Статус договора", "Город оформления договора", "Период действия договора", "Календарная единица периода действия",
                "Период оплаты договора", "Периодичность оплаты (в месяцах)", "Валюта договора", "Страховая премия в валюте договора",
                "Страховая премия в национальной валюте", "Страховая сумма в валюте договора", "Страховая сумма в национальной валюте",
                "Категория продавца", "Курс валюты договора на дату заключения");
    }

    private List<String> getTitleUniversalIsjExtract() {
        List<String> head = new ArrayList<>(BASE_UNIVERSAL_TITLE);
        head.addAll(Arrays.asList(
                "СС в валюте договора по риску Дожития 1 год", "СС в рублях по договору по риску Дожития 1 год", "Дата дожития 1 год", "СС  в валюте договора  по риску Дожития 2 года", "СС в рублях по договору по риску Дожития 2 год", "Дата дожития 2 года",
                "СС  в валюте договора  по риску Дожития 3 года", "СС в рублях по договору по риску Дожития 3 год", "Дата дожития 3 года", "СС  в валюте договора по риску Дожития 4 года",
                "СС в рублях по договору по риску Дожития 4 год", "Дата дожития 4 года", "СС  в валюте договора по риску Дожития 5 лет", "СС в рублях по договору по риску Дожития 5 год",
                "Дата дожития 5 лет", "СС  в валюте договора по риску Дожития 6 лет", "СС в рублях по договору по риску Дожития 6 год", "Дата дожития 6 лет", "СС  в валюте договора  по риску Дожития 7 лет", "СС в рублях по договору по риску Дожития 7 год", "Дата дожития 7 лет",
                "СС в валюте договора по риску Дожития 8 лет", "СС в рублях по договору по риску Дожития 8 год", "Дата дожития 8 лет",
                "СС в валюте договора по риску Дожития 9 лет", "СС в рублях по договору по риску Дожития 9 год", "Дата дожития 9 лет", "СС   в валюте договора по риску Дожития 10 лет",
                "СС в рублях по договору по риску Дожития 10 год", "Дата дожития 10 лет", "СС по Дожитию",
                "СС в валюте договора по риску Смерть по любой причине", "СС в рублях по договору по риску Смерть по любой причине",
                "СС в валюте договора по риску НС", "СС   в рублях по договору по риску НС",
                "СС в валюте договора по риску кораблекрушение", "СС в рублях по договору  по риску кораблекрушение"));
        head.addAll(RESIDENCE_TITLES);
        head.addAll(Arrays.asList(
                "Стратегия инвестирования", "Коэффициент участия 1", "Дата НЗБИ 1", "Дата экспирации 1", "Коэффициент участия 2", "Дата НЗБИ 2", "Дата экспирации 2",
                "Валюта гарантийного фонда", "Валюта рискового фонда", "Величина инвестиционного купона",
                "Наименование агента", "% КВ Агенту", "КВ Агенту в рублях", "ФИО оформившего сотрудника", "Таб. номер сотрудника", "Категория продавца (ФК/Розница)", "ВСП",
                "Город", "Филиал", "Сегмент продукта", "Получено согласие на участие в конкурсе (да/нет)", "Документы в ПО прикреплены (да/нет)", "Комментарии к прикрепленным документам", "Наличие медицинской анкеты", "Наличие финансовой анкеты"));
        return head;
    }

    private List<String> getTitleUniversalRentExtract() {
        List<String> head = new ArrayList<>(BASE_UNIVERSAL_TITLE.subList(0, 19));
        head.add("Взнос в валюте договора");
        head.add("Взнос в  рублях");
        head.addAll(BASE_UNIVERSAL_TITLE.subList(19, BASE_UNIVERSAL_TITLE.size()));
        head.addAll(Arrays.asList(
                "СС по риску Дожитие", "Дата дожития", "СС по риску СЛП в Накопительный период", "СС по риску СЛП в период выплаты ренты", "СС по риску Смерть от НС", "СС по риску Инвалидность I  группы в результате НС (ОУСВ)", "СС по риску Инвалидность I, II  группы в результате НС",
                "СС по риску 7", "СС по риску 8", "СС по риску 9", "СС по риску 10", "СС по риску 11", "СС по риску 12", "СС по риску 13", "СС по риску 14", "СС по риску 15", "СС по риску 16",
                "СС по риску 17", "СС по риску 18", "СС по риску 19", "СС по риску 20", "СС по риску 21", "СС по риску 22"));
        head.addAll(RESIDENCE_TITLES);
        head.addAll(Arrays.asList(
                "Дата начала накопительного периода", "Дата окончания накопительного периода", "Дата начала периода выплаты ренты", "Дата окончания периода выплаты ренты", "Размер ежемесячной страховой ренты",
                "Наименование агента", "% КВ Агенту", "КВ Агенту в рублях", "ФИО оформившего сотрудника", "Таб. номер сотрудника", "Категория продавца (ФК/Розница)", "ВСП",
                "Город", "Филиал", "Сегмент продукта", "Получено согласие на участие в конкурсе (да/нет)", "Документы в ПО прикреплены (да/нет)", "Комментарии к прикрепленным документам", "Наличие медицинской анкеты", "Наличие финансовой анкеты"));
        return head;
    }

    private void setCurrency(InsuranceExtract insurance, List<Object> data) {
        if (insurance.getCurrency() != null) {  // Валюта договора
            try {
                Currency currency = currencyService.getById(insurance.getCurrency());
                data.add(currency != null ? currency.getCurrencyName() : "");
            } catch (RestClientException e) {
                LOGGER.error("Ошибка получения валюты id=" + insurance.getCurrency() + " " + e.getMessage());
                throw new InteractionException(e);
            }
        } else {
            data.add("");
        }
    }

    private void setCity(InsuranceExtract insurance, List<Object> data) {
        if (insurance.getSubdivisionId() > 0) { //Город оформления договора Определяется по ВСП, к которому прикреплен договор.
            try {
                OfficeData officeData = orgUnitService.getById(insurance.getSubdivisionId());
                data.add(officeData != null ? officeData.getCity() : "");
            } catch (RestClientException e) {
                LOGGER.error("Ошибка получения ВСП id=" + insurance.getSubdivisionId() + " " + e.getMessage());
                data.add("");
            }
        } else {
            data.add("");
        }
    }

    private void setAddress(InsuranceExtract insurance, List<Object> data) {
        if (insurance.getSubdivisionId() > 0) { //Адрес ВСП Определяется по ВСП, к которому прикреплен договор.
            try {
                OfficeData officeData = orgUnitService.getById(insurance.getSubdivisionId());
                data.add(officeData != null ? officeData.getAddress() : "");
            } catch (RestClientException e) {
                LOGGER.error("Ошибка получения ВСП id=" + insurance.getSubdivisionId() + " " + e.getMessage());
                data.add("");
            }
        } else {
            data.add("");
        }
    }

    private void setPersonalNumberEmployee(InsuranceExtract insurance, List<Object> data, Map<Long, EmployeeDataForList> employeeMap) {
        if (insurance.getEmployeeId() != null) { //Табельный номер
            EmployeeDataForList employee = employeeMap.get(insurance.getEmployeeId());
            data.add(employee != null ? employee.getPersonnelNumber() : "");
        } else {
            data.add("");
        }
    }

    private Map<Long, EmployeeDataForList> getEmployeesWithOutPermissions() {
        Map<Long, EmployeeDataForList> employeeMap = new HashMap<>();
        try {
            List<EmployeeDataForList> employees = employeesClient.getEmployeesWithOutPermissionsWithDeleted(AUTH_SERVICE_TIMEOUT);
            employees.forEach(i -> employeeMap.put(i.getId(), i));
        } catch (RestClientException e) {
            LOGGER.error("Ошибка получения данных сотрудников " + e.getMessage());
        }
        return employeeMap;
    }

    private String concatAddress(String addressValue, String addressType) {
        return addressValue == null || addressValue.trim().length() == 0 ? "" : addressType + " " + addressValue;
    }

    private String formatDivisionCode(String value) {
        return (value != null && value.length() == 6) ? (value.substring(0, 3) + "-" + value.substring(3)) : value;
    }

    private static BigDecimal getRentAmount(Integer paymentTerm, BigDecimal monthlyRent) {
        return paymentTerm != null && monthlyRent != null ?
                monthlyRent.setScale(2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(paymentTerm * 12L)).setScale(2, RoundingMode.HALF_UP) : null;
    }

    private static LocalDate getEndDateByCalendarUnit(LocalDate startDate, CalendarUnitEnum calendarUnitEnum, int duration) {
        if (startDate == null) {
            return null;
        }
        if (calendarUnitEnum == null || calendarUnitEnum.equals(CalendarUnitEnum.YEAR)) {
            return startDate.plusYears(duration);
        } else if (calendarUnitEnum.equals(CalendarUnitEnum.MONTH)) {
            return startDate.plusMonths(duration);
        } else if (calendarUnitEnum.equals(CalendarUnitEnum.DAY)) {
            return startDate.plusDays(duration);
        }
        return null;
    }

    private static int getDurationInYear(CalendarUnitEnum calendarUnitEnum, int duration) {
        if (calendarUnitEnum == null || calendarUnitEnum.equals(CalendarUnitEnum.YEAR)) {
            return duration;
        } else if (calendarUnitEnum.equals(CalendarUnitEnum.MONTH)) {
            return duration / 12;
        } else if (calendarUnitEnum.equals(CalendarUnitEnum.DAY)) {
            return duration / 365;
        }
        return duration;
    }

    private static String getDateAsString(LocalDate date) {
        return date != null ? date.format(ddMMyyyy) : "";
    }

    private static void addEmptyData(List<Object> data, int count) {
        for (int i = 0; i < count; i++) {
            data.add("");
        }
    }
}
