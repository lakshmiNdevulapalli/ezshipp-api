package com.ezshipp.api.poi;

import java.util.List;

public interface ExcelData<T> {

    String[] getReportFields();

    List<Object> getColumnElements(T model);

    String getSheetName();

}
