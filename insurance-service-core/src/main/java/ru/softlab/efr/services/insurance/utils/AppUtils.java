package ru.softlab.efr.services.insurance.utils;

import com.ibm.icu.text.RuleBasedNumberFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.softlab.efr.services.insurance.exception.ImportInsuranceException;
import ru.softlab.efr.services.insurance.exception.ValidationException;
import ru.softlab.efr.services.insurance.model.db.ClientEntity;
import ru.softlab.efr.services.insurance.model.reportable.ReportableContract;
import ru.softlab.efr.services.insurance.model.rest.CheckModel;
import ru.softlab.efr.services.insurance.model.rest.CheckModelErrorType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.insurance.Constants.DDMMYYYY;
import static ru.softlab.efr.services.insurance.Constants.DDMMYYYYHHMMSS;

/**
 * Класс вспомогательных методов
 *
 * @author olshansky
 * @since 14.09.2018.
 */
public class AppUtils {

    public static boolean isMatch(String source, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(source);
        return matcher.matches();
    }

    public static String normalizeNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return number;
        }
        return number.replaceAll("\\D", StringUtils.EMPTY);
    }
    /**
     * Проверить, содержит ли строка что-нибудь, кроме null или непечатаемых символов (табы, пробелы, etc.)
     *
     * @param s Строка
     * @return boolean
     */
    public static boolean isNullOrWhitespace(String s) {
        return s == null || s.isEmpty() || isWhitespace(s);
    }

    public static boolean isWhitespace(String s) {
        int length = s.length();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isNotNullOrWhitespace(String s) {
        return Objects.nonNull(s) && !s.isEmpty() && !isWhitespace(s);
    }

    public static boolean isNotNullOrWhitespace(String... args) {
        return Arrays.stream(args).allMatch(AppUtils::isNotNullOrWhitespace);
    }

    public static boolean nonNull(Object... args) {
        return Arrays.stream(args).allMatch(Objects::nonNull);
    }

    public static boolean isNull(Object... args) {
        return Arrays.stream(args).allMatch(Objects::isNull);
    }

    public static boolean isNullAnything(Object... args) {
        return Arrays.stream(args).anyMatch(Objects::isNull);
    }

    public static boolean isBlankAnything(String... args) {
        return Arrays.stream(args).anyMatch(StringUtils::isBlank);
    }

    public static boolean isNullOrWhitespace(String[] args) {
        return Arrays.stream(args).allMatch(AppUtils::isNullOrWhitespace);
    }

    public static Object mapSimilarObjects(Object source, Object dest) {
        try {
            BeanUtils.copyProperties(dest, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Can't copy field to " + dest.getClass().getSimpleName(), e);
        }
        return dest;
    }

    public static LocalDate mapDate2LocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date mapLocalDate2Date(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate mapTimestamp2LocalDate(Timestamp timestamp) {
        return timestamp.toLocalDateTime().toLocalDate();
    }

    public static Timestamp mapLocalDate2Timestamp(LocalDate localDate) {
        return Timestamp.valueOf(localDate.atStartOfDay());
    }

    public static Integer getAgeByToday(LocalDate birthDate) {
        return calculateAge(birthDate, LocalDate.now());
    }

    public static Integer calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((Objects.nonNull(birthDate)) && (Objects.nonNull(currentDate))) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return null;
        }
    }

    public static LocalDate calculateDateByAge(LocalDate birthDate, Integer age) {
        if ((Objects.nonNull(birthDate)) && (Objects.nonNull(age))) {
            return birthDate.plusYears(age);
        } else {
            return null;
        }
    }

    public static <T, T2> List<T2> apply(List<T> entities, Function<T,T2> function) {
        return entities.stream().map(function).collect(Collectors.toList());
    }
    public static <T> List<T> filter(List<T> entities, Predicate<T> predicate) {
        return entities.stream().filter(predicate).collect(Collectors.toList());
    }
    public static <T> Optional<T> getFirst(List<T> entities, Predicate<T> predicate) {
        return entities.stream().filter(predicate).findFirst();
    }
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static String getRurStringAmount(BigDecimal amount){
        return getStringAmountByLang(amount, "ru");
    }

    public static String getStringAmountByLang(BigDecimal amount, String languageTag){
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.forLanguageTag(languageTag), RuleBasedNumberFormat.SPELLOUT);
        return nf.format(amount);
    }

    public static LocalDate getDateValue(Row row, int cellIndex, int titleRow) throws ImportInsuranceException {
        try {
            //при создании отчета дата сохраняется в виде строки, после ручного редактирования ячейки может принять значение даты
            if (row.getCell(cellIndex).getCellTypeEnum() == CellType.NUMERIC) {
                Date date = row.getCell(cellIndex).getDateCellValue();
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } else {
                String date = row.getCell(cellIndex).getStringCellValue();
                if (StringUtils.isNotEmpty(date)) {
                    return LocalDate.parse(date, DDMMYYYY);
                }
            }
            return null;
        } catch (Exception ex) {
            throw new ImportInsuranceException("Поле \"" + getCellName(row, cellIndex, titleRow) + "\" должно содержать значение в формате: dd.mm.yyyy");
        }
    }

    public static LocalDateTime getDateTimeValue(Row row, int cellIndex, int titleRow) throws ImportInsuranceException {
        try {
            //при создании отчета дата сохраняется в виде строки, после ручного редактирования ячейки может принять значение даты
            if (row.getCell(cellIndex).getCellTypeEnum() == CellType.NUMERIC) {
                Date date = row.getCell(cellIndex).getDateCellValue();
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            } else {
                String date = row.getCell(cellIndex).getStringCellValue();
                if (StringUtils.isNotEmpty(date)) {
                    return LocalDateTime.parse(date, DDMMYYYYHHMMSS);
                }
            }
            return null;
        } catch (Exception ex) {
            throw new ImportInsuranceException(
                    String.format("Поле \"%s\" должно содержать значение в формате: %s", getCellName(row, cellIndex, titleRow), "dd.MM.yyyy HH:mm:ss"));
        }
    }

    public static String getCellName(Row row, int cellIndex, int titleRow) {
        Cell cell = row.getSheet().getRow(titleRow).getCell(cellIndex);
        return Objects.nonNull(cell) ? cell.getStringCellValue() : String.format("(наименование не указано, индекс поля %s)", cellIndex);
    }

    public static void putCriticalError(List<CheckModel> checkModel, int rowNum, String errorText) {
        checkModel.add(new CheckModel(String.valueOf(rowNum), errorText, CheckModelErrorType.CRITICAL));
    }


    public static String getEncodedFileName(String fileName) throws UnsupportedEncodingException {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()).replaceAll("\\+", " ");
    }

    public static long convertLocalDateTimeToEpochMillis(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static void setColor(Row row, XSSFWorkbook myExcelBook, byte[] color, int statusUpdateCellNumber) {
        XSSFCellStyle style = myExcelBook.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(color));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        applyColorCellToRange(row, style, 0, statusUpdateCellNumber);
    }

    public static void applyColorCellToRange(Row row, XSSFCellStyle style, int colStart, int colEnd) {
        if (Objects.nonNull(row)) {
            for (int c = colStart; c <= colEnd; c++) {
                Cell cell = row.getCell(c);
                if (Objects.nonNull(cell)) {
                    cell.setCellStyle(style);
                }
            }
        }
    }

    public static String getString(Cell cell) {
        if (cell != null) {
            String value;
            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                value = String.valueOf((long) cell.getNumericCellValue());
            } else {
                value = String.valueOf(cell).trim();
            }
            return value.replace(".0", StringUtils.EMPTY);
        }
        return StringUtils.EMPTY;
    }

    private static void setErrorText(Row row, String errorText, int statusUpdateCellNumber) {
        Cell cellResponse = row.getCell(statusUpdateCellNumber);
        String prevCellValue = getString(cellResponse);
        if (Objects.isNull(cellResponse)) {
            cellResponse = row.createCell(statusUpdateCellNumber);
        }
        if (StringUtils.isNotBlank(errorText)) {
            String dateTimeString = ReportableContract.presentLocalDateTime(LocalDateTime.now(), "dd/MM/yyyyг. HH:mm:ss");
            errorText = String.format("[%s] %s", dateTimeString, errorText);
            cellResponse.setCellValue(
                    StringUtils.isBlank(prevCellValue) ?
                            errorText : prevCellValue.concat(";\n").concat(errorText));
        }
    }

    public static void setSyncErrors2XLSX(String path, List<AbstractMap.SimpleEntry<Long, String>> errors,
                                          int defaultSheetNumber, int headCountRow, int statusUpdateCellNumber) throws Exception {
        try (XSSFWorkbook myExcelBook = new XSSFWorkbook(new FileInputStream(path))) {
            XSSFSheet myExcelSheet = myExcelBook.getSheetAt(defaultSheetNumber);

            for (Row row : myExcelSheet) {
                if (row.getRowNum() > headCountRow) {
                    AtomicBoolean errorExists = new AtomicBoolean(false);
                    errors.forEach(err -> {
                        if (Long.valueOf(row.getRowNum()).equals(err.getKey())) {
                            errorExists.set(true);
                            setErrorText(row, err.getValue(), statusUpdateCellNumber);
                            byte[] yellowRGB = new byte[]{(byte) 255, (byte) 255, (byte) 204};
                            setColor(row, myExcelBook, yellowRGB, statusUpdateCellNumber);
                        }
                    });
                    if (Boolean.FALSE.equals(errorExists.get())) {
                        byte[] greenRGB = new byte[]{(byte) 153, (byte) 255, (byte) 153};
                        setColor(row, myExcelBook, greenRGB, statusUpdateCellNumber);
                        clearErrorText(row, statusUpdateCellNumber);
                    }
                }
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
                myExcelBook.write(fileOutputStream);
            }
        }
    }

    private static void clearErrorText(Row row, int statusUpdateCellNumber) {
        Cell cellResponse = row.getCell(statusUpdateCellNumber);
        if (Objects.nonNull(cellResponse)) {
            cellResponse.setCellValue(StringUtils.EMPTY);
        }
    }

    public static BigDecimal getBigDecimalFromString(String string) {
        if (StringUtils.isBlank(string)) {
            return BigDecimal.ZERO;
        }
        try {
            return BigDecimal.valueOf(Double.valueOf(string));
        } catch (Exception ex) {
            throw new ValidationException(Collections.singletonList(String.format(
                    "Произошла ошибка при попытке преобразовать значение '%s' в число", string)));
        }
    }

    public static String getNumber(Cell cell) {
        if (cell != null) {
            String value;
            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                value = String.valueOf(cell.getNumericCellValue());
            } else {
                value = String.valueOf(cell).trim();
            }
            return value;
        }
        return StringUtils.EMPTY;
    }

    public static String splitDigitBySpaces(BigDecimal digit) {
        if (Objects.isNull(digit)) {
            return "";
        }
        DecimalFormat formatter = getDecimalFormat();
        return formatter.format(digit);
    }

    /**
     * Сформировать ФИО клиента
     *
     * @param client данные клиента
     * @return ФИО клиента
     */
    public static String getFullName(ClientEntity client) {
        if (client == null) {
            return StringUtils.EMPTY;
        }

        return String.join(StringUtils.SPACE,
                StringUtils.defaultString(client.getSurName()),
                StringUtils.defaultString(client.getFirstName()),
                StringUtils.defaultString(client.getMiddleName()))
                .trim();
    }

    private static DecimalFormat getDecimalFormat() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter;
    }
}
