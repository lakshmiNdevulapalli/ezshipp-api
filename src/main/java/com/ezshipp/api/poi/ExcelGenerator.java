package com.ezshipp.api.poi;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class ExcelGenerator<T> {

	//private static final Logger logger = LogManager.getLogger(ExcelGeneratorChild.class.getName());

	public Workbook createXLS(List<T> modelData, ExcelData excelData){
		XSSFWorkbook workbook = new XSSFWorkbook();

		try {

			XSSFSheet xSSFSheet = workbook.createSheet(excelData.getSheetName());
			//XSSFSheet xSSFSheet2 = workbook.createSheet("Sheet 2");

			String[] reportFields = excelData.getReportFields();

			setUpWorkSheets(workbook,xSSFSheet, reportFields);
			//setUpWorkSheets(workbook,xSSFSheet2, reportFields);

			List<T> sheetData = new ArrayList<>();
			for(T data : modelData){
				sheetData.add(data);
			}

			processWorkSheet(xSSFSheet, sheetData, excelData);
			//processWorkSheet(xSSFSheet2, sheetData);

		}
		catch (Exception e) {
			e.printStackTrace();
			//logger.error("Error generating excel report:\n", e);
		}

		return workbook;
	}

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

	private void processWorkSheet(XSSFSheet sheet, List<T> entries, ExcelData excelData) {
		int exCount = 0;
		int lastCellNum = 0;

		for (T entry : entries) {
			XSSFRow row = sheet.createRow(++exCount);
			List<Object> elements = excelData.getColumnElements(entry);
			writeRecord(row, elements);
			lastCellNum = row.getLastCellNum();
		}

		for (int i = 0; i < lastCellNum; i++) {
			sheet.autoSizeColumn(i);
		}
	}


	protected void writeRecord(XSSFRow row, List<Object> elements) {
		int len = elements.size();

		for (int i = 0; i < len; i++) {
			XSSFCell genericCell = row.createCell(i);
			if (elements.get(i) instanceof String)
				genericCell.setCellValue((String)elements.get(i));
			else if(elements.get(i) instanceof Long)
				genericCell.setCellValue((Long)elements.get(i));
		}
	}

//	@Override
//	protected List<String> getColumnElements(Order expiryEvent) {
//		List<String> elements = new ArrayList<>();
//
//		elements.add(expiryEvent.getOrderseqId());
//		elements.add(expiryEvent.getItemName());
//		elements.add(expiryEvent.getReceiverName());
//		elements.add(expiryEvent.getReceiverPhone());
//		elements.add(expiryEvent.getDropAddress());
//		elements.add(String.valueOf(expiryEvent.getSubtotal_amount()));
//		elements.add(expiryEvent.getOrderStatus());
//
//		return elements;
//	}

//	public static String[] getReportFields(){
//
//		String[] fields = new String[7];
//		fields[0] = "OrderID";
//		fields[1] = "CentralID";
//		fields[2] = "Customer Name";
//		fields[3] = "Customer Phone";
//		fields[4] = "Drop Address";
//		fields[5] = "COD";
//		fields[6] = "Status";
//
//		return fields;
//	}
//

}
