package com.ezshipp.api.poi;

import com.ezshipp.api.document.Order;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class ExcelGeneratorChild extends ExcelGenerator<Order>{

	//private static final Logger logger = LogManager.getLogger(ExcelGeneratorChild.class.getName());

	public Workbook createXLS(String reportName, List<Order> modelData){
		File file = null;
		XSSFWorkbook workbook = new XSSFWorkbook();

		try {

			XSSFSheet xSSFSheet = workbook.createSheet("Sheet 1");
			XSSFSheet xSSFSheet2 = workbook.createSheet("Sheet 2");

			String[]  reportFields = getReportFields();

			setUpWorkSheets(workbook,xSSFSheet, reportFields);
			setUpWorkSheets(workbook,xSSFSheet2, reportFields);

			List<Order> sheetData = new ArrayList<>();
			for(Order expiryEvent : modelData){
				sheetData.add(expiryEvent);
			}

			processWorkSheet(xSSFSheet, sheetData);
			processWorkSheet(xSSFSheet2, sheetData);

//			FileOutputStream out = new FileOutputStream(reportName+".xlsx");
//			workbook.write(out);
//			out.close();

			file = new  File(reportName+".xlsx");

		}
		catch (Exception e) {
			e.printStackTrace();
			//logger.error("Error generating excel report:\n", e);
		}

		return workbook;
	}

	@Override
	protected List<String> getColumnElements(Order expiryEvent) {
		List<String> elements = new ArrayList<>();

		elements.add(expiryEvent.getOrderseqId());
		elements.add(expiryEvent.getItemName());
		elements.add(expiryEvent.getReceiverName());
		elements.add(expiryEvent.getReceiverPhone());
		elements.add(expiryEvent.getDropAddress());
		elements.add(String.valueOf(expiryEvent.getSubtotal_amount()));
		elements.add(expiryEvent.getOrderStatus());

		return elements;
	}

	public static String[] getReportFields(){

		String[] fields = new String[7];
		fields[0] = "OrderID";
		fields[1] = "CentralID";
		fields[2] = "Customer Name";
		fields[3] = "Customer Phone";
		fields[4] = "Drop Address";
		fields[5] = "COD";
		fields[6] = "Status";

		return fields;
	}
}
