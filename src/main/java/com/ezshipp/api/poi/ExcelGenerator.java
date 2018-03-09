package com.ezshipp.api.poi;

import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.util.List;


abstract class ExcelGenerator<T> {

	protected void setUpWorkSheets(XSSFWorkbook workbook, XSSFSheet sheet, String[] field) {

		XSSFRow row = sheet.createRow(0);

		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(new XSSFColor(Color.WHITE));
		style.setFont(font);
		style.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 79, (byte) 129, (byte) 189}));
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);


		int len = field.length;

		for (int i = 0; i < len; i++) {
			XSSFCell genericCell = row.createCell(i);
			genericCell.setCellValue(field[i]);
		}

		for (int k = 0; k < row.getLastCellNum(); k++) {
			row.getCell(k).setCellStyle(style);
		}
	}

	protected void processWorkSheet(XSSFSheet sheet, List<T> entries) {
		int exCount = 0;
		int lastCellNum = 0;

		for (T entry : entries) {
			XSSFRow row = sheet.createRow(++exCount);
			List<String> elements = getColumnElements(entry);
			writeRecord(row, elements);
			lastCellNum = row.getLastCellNum();
		}

		for (int i = 0; i < lastCellNum; i++) {
			sheet.autoSizeColumn(i);
		}
	}

	protected void writeRecord(XSSFRow row, List<String> elements) {
		int len = elements.size();

		for (int i = 0; i < len; i++) {
			XSSFCell genericCell = row.createCell(i);
			genericCell.setCellValue(elements.get(i));
		}
	}

	abstract protected List<String> getColumnElements(T model);
}
