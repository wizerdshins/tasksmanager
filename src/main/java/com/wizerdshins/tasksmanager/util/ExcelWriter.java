package com.wizerdshins.tasksmanager.util;

import com.wizerdshins.tasksmanager.entity.Task;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

public class ExcelWriter {

    private static final Logger log = Logger.getLogger(ExcelWriter.class);

    private int columnIndex = 0;
    private int rowIndex = 0;

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;

    public ExcelWriter() {

        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet("Report");

        for (int i = 0; i <= 5; i++) {
            sheet.setColumnWidth(i, 5000);
        }

        createHeader();
        createBody();
    }

    public void createHeader() {

        Font fontTitle = sheet.getWorkbook().createFont();
        fontTitle.setBold(true);
        fontTitle.setFontHeight((short)300);

        HSSFCellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setFont(fontTitle);

        HSSFRow rowTitle = sheet.createRow(rowIndex);
        rowTitle.setHeight((short)500);
        HSSFCell cellTitle = rowTitle.createCell(columnIndex);
        cellTitle.setCellValue("Report");
        cellTitle.setCellStyle(cellStyle);

        HSSFRow dateTitle = sheet.createRow(rowIndex + 1);
        HSSFCell cellDate = dateTitle.createCell(columnIndex);

        HSSFCellStyle dateCellStyle = sheet.getWorkbook().createCellStyle();
        dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dateCellStyle.setWrapText(true);

        Date reportDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, E, HH:mm z");
        cellDate.setCellValue(dateFormat.format(reportDate));
        cellDate.setCellStyle(dateCellStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
    }

    public void createBody() {

        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);

        HSSFCellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
        headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.FINE_DOTS);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setWrapText(true);
        headerCellStyle.setFont(font);
        headerCellStyle.setBorderBottom(BorderStyle.DASH_DOT);

        HSSFRow rowHeader = sheet.createRow(rowIndex + 2);
        rowHeader.setHeight((short)500);

        HSSFCell cellOne = rowHeader.createCell(columnIndex);
        cellOne.setCellValue("ID");
        cellOne.setCellStyle(headerCellStyle);

        HSSFCell cellTwo = rowHeader.createCell(columnIndex + 1);
        cellTwo.setCellValue("Message");
        cellTwo.setCellStyle(headerCellStyle);

        HSSFCell cellThree = rowHeader.createCell(columnIndex + 2);
        cellThree.setCellValue("Company");
        cellThree.setCellStyle(headerCellStyle);

        HSSFCell cellFour = rowHeader.createCell(columnIndex + 3);
        cellFour.setCellValue("Date Create");
        cellFour.setCellStyle(headerCellStyle);

        HSSFCell cellFive = rowHeader.createCell(columnIndex + 4);
        cellFive.setCellValue("Date Complete");
        cellFive.setCellStyle(headerCellStyle);

        HSSFCell cellSix = rowHeader.createCell(columnIndex + 5);
        cellSix.setCellValue("Status");
        cellSix.setCellStyle(headerCellStyle);
    }

    public void write(String path, List<Task> tasks) {

        rowIndex += 2;

        HSSFCellStyle bodyCellStyle = sheet.getWorkbook().createCellStyle();
        bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyCellStyle.setWrapText(true);

        for (int i = rowIndex; i + rowIndex - 2 < tasks.size() + 2; i++) {

            HSSFRow row = sheet.createRow(i + 1);

            HSSFCell idCell = row.createCell(columnIndex);
            idCell.setCellValue(tasks.get(i - 2).getId());
            idCell.setCellStyle(bodyCellStyle);

            HSSFCell messageCell = row.createCell(columnIndex + 1);
            messageCell.setCellValue(tasks.get(i - 2).getMessage());
            messageCell.setCellStyle(bodyCellStyle);

            HSSFCell companyCell = row.createCell(columnIndex + 2);
            companyCell.setCellValue(tasks.get(i - 2).getCompany().toString());
            companyCell.setCellStyle(bodyCellStyle);

            HSSFCell dateCreateCell = row.createCell(columnIndex + 3);
            dateCreateCell.setCellValue(tasks.get(i - 2).getDateCreate().toString());
            dateCreateCell.setCellStyle(bodyCellStyle);

            HSSFCell dateCompleteCell = row.createCell(columnIndex + 4);
            if (tasks.get(i - 2).getDateComplete() != null) {
                dateCompleteCell.setCellValue(tasks.get(i - 2).getDateCreate().toString());
            } else {
                dateCompleteCell.setCellValue("");
            }
            dateCompleteCell.setCellStyle(bodyCellStyle);

            HSSFCell statusCell = row.createCell(columnIndex + 5);
            statusCell.setCellValue(tasks.get(i - 2).getStatus());
            statusCell.setCellStyle(bodyCellStyle);

            File file = new File(path + ".xls");
            try {
                workbook.write(new FileOutputStream(file));
            } catch (IOException e) {
                log.warn("Excel write exception: ", e);
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    log.warn(e);
                }
            }

        }
    }

}
