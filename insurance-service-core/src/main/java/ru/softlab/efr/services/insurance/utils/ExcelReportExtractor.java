package ru.softlab.efr.services.insurance.utils;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class ExcelReportExtractor implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(ExcelReportExtractor.class);

    private static final short FONT_HEIGHT_TITLE = 12;
    private static final short FONT_HEIGHT_STANDARD = 10;

    private static final int COLUMN_WIDTH_STANDARD = 25;

    private SXSSFWorkbook workbook;
    private Sheet sheet;
    private CellStyle borderedHeader;
    private CellStyle borderedCell;
    private CellStyle borderedDateCell;
    private CellStyle titleStyle;
    private CellStyle sumStyle;

    public ExcelReportExtractor() {
        workbook = new SXSSFWorkbook();
        sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(COLUMN_WIDTH_STANDARD);

        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints(FONT_HEIGHT_TITLE);
        titleFont.setBold(true);

        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints(FONT_HEIGHT_STANDARD);
        headerFont.setBold(true);

        Font standardFont = workbook.createFont();
        standardFont.setFontHeightInPoints(FONT_HEIGHT_STANDARD);
        standardFont.setBold(true);

        Font fontNotBold = workbook.createFont();
        fontNotBold.setFontHeightInPoints(FONT_HEIGHT_STANDARD);

        borderedHeader = createCellStyle(true, true, headerFont);
        borderedCell = createCellStyle(true, false, standardFont);

        borderedDateCell = createCellStyle(true, false, standardFont);
        borderedDateCell.setDataFormat(workbook.createDataFormat().getFormat("MMM.yy"));
        titleStyle = createCellStyle(false, true, titleFont);
        sumStyle = createCellStyle(true, false, standardFont);
        sumStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
    }

    /**
     * Получение сформированного файла отчёта в виде потока байт.
     * При вызове данного метода выполняется удаление всех временных файлов, которые были созданы
     * при формировании Excel-файла.
     * Данный метод может быть вызван только один раз на экземпляре объекта.
     *
     * @return Файл отчёта в виде потока байт.
     */
    public ByteArrayOutputStream getByteArrayOutputStreamFromExcel() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            workbook.write(stream);
        } catch (IOException e) {
            LOGGER.error("При формировании отчёта произошла ошибка", e);
        } finally {
            workbook.dispose();
        }
        return stream;
    }

    /**
     * Получение сформированного файла отчёта в виде массива байт.
     * При вызове данного метода выполняется удаление всех временных файлов, которые были созданы
     * при формировании Excel-файла.
     * Данный метод может быть вызван только один раз на экземпляре объекта.
     *
     * @return Файл отчёта в виде потока байт.
     */
    public byte[] getByteArrayFromExcel() {
        ByteArrayOutputStream stream = getByteArrayOutputStreamFromExcel();
        return stream.toByteArray();
    }

    /**
     * Создает стиль ячейки.
     */
    private CellStyle createCellStyle(boolean bordered, boolean header, Font font) {
        CellStyle cellStyle = workbook.createCellStyle();
        if (bordered) {
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
        } else {
            cellStyle.setBorderBottom(BorderStyle.NONE);
            cellStyle.setBorderLeft(BorderStyle.NONE);
            cellStyle.setBorderTop(BorderStyle.NONE);
            cellStyle.setBorderRight(BorderStyle.NONE);
        }
        if (header) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public int addTitle(String title, List<String> head) {
        int currentRow = 0;

        //Заголовок
        if (title != null) {
            addCell(currentRow, 0, title, titleStyle, title.contains("\n"));
            if (head != null) {
                addMergedRegion(currentRow, 0, currentRow, head.size() - 1);
            }
            currentRow++;
        }
        currentRow++;

        if (head != null) {
            //Шапка
            for (int i = 0; i < head.size(); i++) {
                addCell(currentRow, i, head.get(i), borderedHeader, false);
            }
            currentRow++;
        }
        return currentRow;
    }

    public int addRow(List<Object> row, int index) {
        if (row != null) {
            for (int col = 0; col < row.size(); col++) {
                addCell(index, col, row.get(col), borderedCell, false);
            }
        }
        return ++index;
    }


    /**
     * Вставить данные в документ Excel
     */
    public void addReport(String title, List<String> head, List<List<Object>> rowData) {
        int currentRow = addTitle(title, head);
        if (rowData != null) {
            //Таблица
            for (List<Object> aRowData : rowData) {
                currentRow = addRow(aRowData, currentRow);
            }
        }
    }

    private void addCell(int rowIndex, int cellIndex, Object value, CellStyle cellStyle, boolean isBigTitle) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        if (isBigTitle) {
            row.setHeight((short) 1800);
        }
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            cell = row.createCell(cellIndex);
        }

        if (value instanceof BigDecimal) {
            cell.setCellType(CellType.NUMERIC);
            cellStyle = sumStyle;
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof Number) {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Date) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue((Date) value);
            if (cellStyle.equals(borderedCell)) {
                cellStyle = borderedDateCell;
            }
        } else if (value == null) {
            cell.setCellType(CellType.STRING);
            cell.setCellValue("");
        } else {
            cell.setCellType(CellType.STRING);
            cell.setCellValue(value.toString());
        }

        cell.setCellStyle(cellStyle);
    }

    private void addMergedRegion(int x1, int y1, int x2, int y2) {
        sheet.addMergedRegion(new CellRangeAddress(x1, y1, x2, y2));
    }

    @Override
    public void close() {
        workbook.dispose();
    }
}
