package com.ezshipp.api.scheduler.order;

import com.ezshipp.api.model.OrderCount;
import com.ezshipp.api.poi.ExcelData;

import java.util.ArrayList;
import java.util.List;

public class OrderCountDataImpl implements ExcelData<OrderCount> {

    @Override
    public String[] getReportFields() {
        String[] fields = new String[4];
        fields[0] = "Operations";
        fields[1] = "Instant";
        fields[2] = "4Hours";
        fields[3] = "SameDay";

        return fields;
    }

    @Override
    public List<Object> getColumnElements(OrderCount model) {
        List<Object> elements = new ArrayList<>();
        elements.add(model.getOpsType());
        elements.add(model.getInstant());
        elements.add(model.getFourHours());
        elements.add(model.getSameDay());

        return elements;
    }

    @Override
    public String getSheetName() {
        return "orders-count";
    }
}
