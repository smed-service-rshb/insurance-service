package ru.softlab.efr.services.insurance.utils;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.softlab.efr.services.insurance.model.db.BaseIndex;
import ru.softlab.efr.services.insurance.model.db.Quote;
import ru.softlab.efr.services.insurance.model.db.ShareEntity;
import ru.softlab.efr.services.insurance.model.db.Strategy;
import ru.softlab.efr.services.insurance.pojo.ParseShareResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class InvestmentDictUpdateHelper {

    private static final Logger LOGGER = Logger.getLogger(InvestmentDictUpdateHelper.class);

    private static final int DATE_CELL_COLUMN_INDEX = 0;
    private static final int BASE_INDEX_CELL_COLUMN_INDEX = 1;
    private static final int SHARE_NAME_ROW_INDEX = 1;
    private static final int SHARE_DESCRIPTION_ROW_INDEX = 2;

    private InvestmentDictUpdateHelper() {
    }

    /**
     * Получить результат репликации справочника акций.
     *
     * @param path            путь по которому расположен excel файл
     * @param existShareList  список акций, которые уже есть в системе
     * @param existStrategies список стратегий, которые есть в системе
     * @return результат репликации, который содержит поле shares - список новых акций для сохранения со списком котировок;
     * quotes - список котировок существующих акций для обновления или создания
     */
    public static ParseShareResult getShareEntityListFromXLSX(String path,
                                                              List<ShareEntity> existShareList,
                                                              List<Strategy> existStrategies) throws IOException {
        ParseShareResult result = new ParseShareResult();
        //мапа наименование стратегии - список стратегий
        Map<String, Set<Strategy>> strategyMap = getStrategyMap(existStrategies);
        //мапа наименование акции - сущность акции
        Map<String, ShareEntity> existShareMap = new HashMap<>();
        existShareList.forEach(i -> existShareMap.put(i.getName(), i));

        try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(path))) {
            for (Sheet aMyExcelBook : myExcelBook) {
                XSSFSheet myExcelSheet = (XSSFSheet) aMyExcelBook;
                // в названии вкладки листа указано наименование стратегии,
                // для всех стратегий с данным именем и типом COUPON необходимо привязать акции расположенные на данном листе
                Set<Strategy> strategies = strategyMap.get(myExcelSheet.getSheetName());
                if (strategies == null) {
                    LOGGER.warn("При репликации справочника информации по котировкам акций произошла ошибка." +
                            " Стратегия не найдена. Информация пропускается.");
                    continue;
                }
                for (Row row : myExcelSheet) {
                    //первая строка пустая, третья содержит описание колонок
                    if (row.getRowNum() == 0 || row.getRowNum() == 2 || row.getRowNum() == 3) continue;
                    //вторая строка содержит наименование акций
                    if (row.getRowNum() == SHARE_NAME_ROW_INDEX) {
                        Iterator<Cell> cellIterator = row.cellIterator();
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            if (cell.getColumnIndex() == DATE_CELL_COLUMN_INDEX) continue;
                            //проверяем присутствует ли данная акция в списке сохраненных
                            ShareEntity shareEntity = existShareMap.get(cell.getStringCellValue());
                            if (shareEntity == null) {
                                shareEntity = new ShareEntity(cell.getStringCellValue());
                            }
                            shareEntity.setDescription(myExcelSheet.getRow(SHARE_DESCRIPTION_ROW_INDEX)
                                    .getCell(cell.getColumnIndex()).getStringCellValue());
                            //обновляем список стратегий данной акции
                            shareEntity.addStrategy(strategies);
                            existShareMap.put(shareEntity.getName(), shareEntity);
                        }
                    } else {
                        Iterator<Cell> cellIterator = row.cellIterator();
                        LocalDate date = null;
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            if (cell.getColumnIndex() == DATE_CELL_COLUMN_INDEX) {
                                //прервый столбец содержит дату действия котировки
                                date = getLocalDateCellValue(cell);
                            } else {
                                //если ячейка не сожержит число, считаем, что информация по котировке отсутствует
                                if (isNumericCell(cell)) {
                                    //значение котировки
                                    BigDecimal quote = BigDecimal.valueOf(cell.getNumericCellValue());
                                    //наименование акции
                                    String shareName = myExcelSheet.getRow(SHARE_NAME_ROW_INDEX).getCell(cell.getColumnIndex()).getStringCellValue();
                                    if (existShareMap.get(shareName).getId() != null) {
                                        result.getQuotes().add(new Quote(date, quote, existShareMap.get(shareName)));
                                    } else {
                                        existShareMap.get(shareName).getQuotes().add(
                                                new Quote(date, quote, existShareMap.get(shareName)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        result.getShares().addAll(existShareMap.values().stream().filter(i -> i.getId() == null).collect(Collectors.toList()));

        return result;
    }

    /**
     * Получить список базовых индексов из файла excel.
     *
     * @param path            путь к файлу excel для репликации
     * @param existStrategies список стратегий, которые есть в системе
     * @return список базовых индексов в представлении БД
     */
    public static List<BaseIndex> getStrategyBaseIndexListFromXLSX(String path, List<Strategy> existStrategies) throws IOException {

        //мапа наименование стратегии - список стратегий
        Map<String, Set<Strategy>> strategyMap = getStrategyMap(existStrategies);
        List<BaseIndex> indexList = new ArrayList<>();
        try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(path))) {
            for (Sheet aMyExcelBook : myExcelBook) {
                XSSFSheet myExcelSheet = (XSSFSheet) aMyExcelBook;
                // в названии вкладки листа указано наименование стратегии,
                // для всех стратегий с данным именем и типами CLASSIC и LOCOMOTIVE необходимо привязать базовые индексы расположенные на данном листе
                String strategyName = myExcelSheet.getSheetName();
                Set<Strategy> strategy = strategyMap.get(myExcelSheet.getSheetName());
                if (strategy == null) {
                    LOGGER.warn("При репликации справочника информации по котировкам акций произошла ошибка." +
                            " Стратегия не найдена. Информация пропускается.");
                    continue;
                }
                for (Row row : myExcelSheet) {
                    if (row.getRowNum() == 0 || row.getRowNum() == 1) continue;
                    if (isNumericCell(row.getCell(1))) {
                        LocalDate date = getLocalDateCellValue(row.getCell(DATE_CELL_COLUMN_INDEX));
                        BigDecimal index = BigDecimal.valueOf(row.getCell(BASE_INDEX_CELL_COLUMN_INDEX).getNumericCellValue());
                        //если для индекса указано значение 0, считаем, что данные отсутствуют
                        if (BigDecimal.ZERO.compareTo(index) != 0) {
                            indexList.add(new BaseIndex(date, strategy, strategyName, index));
                        }
                    }
                }
            }
        }

        return indexList;
    }

    private static Map<String, Set<Strategy>> getStrategyMap(List<Strategy> strategies) {
        Map<String, Set<Strategy>> strategyMap = new HashMap<>();
        strategies.forEach(i -> {
            Set<Strategy> strategySet = strategyMap.get(i.getName());
            if (strategySet == null) {
                strategySet = new HashSet<>();
                strategySet.add(i);
                strategyMap.put(i.getName(), strategySet);
            } else {
                strategySet.add(i);
            }
        });
        return strategyMap;
    }

    private static boolean isNumericCell(Cell cell) {
        return cell != null && cell.getCellTypeEnum() == CellType.NUMERIC;
    }

    private static LocalDate getLocalDateCellValue(Cell cell) {
        return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}