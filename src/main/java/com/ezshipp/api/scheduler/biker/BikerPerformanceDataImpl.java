package com.ezshipp.api.scheduler.biker;

import com.ezshipp.api.model.BikerOrder;
import com.ezshipp.api.poi.ExcelData;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BikerPerformanceDataImpl implements ExcelData<BikerOrder> {

    @Override
    public String[] getReportFields() {
        String[] fields = new String[5];
        fields[0] = "BikerName";
        fields[1] = "Order Count";
        fields[2] = "Zone";
        fields[3] = "Customer";
        fields[4] = "Orders";

        return fields;
    }

    @Override
    public List<Object> getColumnElements(BikerOrder model) {
        List<Object> elements = new ArrayList<>();
        elements.add(model.getName());
        elements.add(model.getOrderCount());
        elements.add(model.getZone());
        elements.add(model.getClientName());
        elements.add(StringUtils.join(model.getOrderList(), ','));

        return elements;
    }

    @Override
    public String getSheetName() {
        return "bikers-performance";
    }
}
