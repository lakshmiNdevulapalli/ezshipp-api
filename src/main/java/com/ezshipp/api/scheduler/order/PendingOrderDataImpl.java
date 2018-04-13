package com.ezshipp.api.scheduler.order;

import com.ezshipp.api.document.Order;
import com.ezshipp.api.poi.ExcelData;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderDataImpl implements ExcelData<Order> {

    @Override
    public String[] getReportFields() {
        String[] fields = new String[13];
        fields[0] = "OrderID";
        fields[1] = "Customer Name";
        fields[2] = "Customer Phone";
        fields[3] = "Receiver Name";
        fields[4] = "Receiver Phone";
        fields[5] = "Order Date";
        fields[6] = "Order Type";
        fields[7] = "Pickup Zone";
        fields[8] = "Delivery Zone";
        fields[9] = "Current Biker";
        fields[10] = "Lapsed Time";
        fields[11] = "Status";
        fields[12] = "Comments";

        return fields;
    }

    @Override
    public List<Object> getColumnElements(Order model) {
        List<Object> elements = new ArrayList<>();
        elements.add(model.getOrderseqId());
        elements.add(model.getCustomerName());
        elements.add(formatPhoneNumber(model.getCustomerPhone()));
        elements.add(model.getReceiverName());
        elements.add(formatPhoneNumber(model.getReceiverPhone()));
        elements.add(model.getOrder_datetime());
        elements.add(model.getOrderTypeStr());
        elements.add(model.getPickupdeponame());
        elements.add(model.getDeliverydeponame());
        elements.add(model.getBikerName());
        elements.add(model.getOrderlapseTime());
        elements.add(model.getOrderStatus());
        elements.add(model.getAdmin_comment());

        return elements;
    }

    @Override
    public String getSheetName() {
        return "pending-orders";
    }

    private Long formatPhoneNumber(String number)  {
        String formatted="";
        if (number.trim().startsWith("+91"))   {
            formatted = number.substring(3, number.length());
            return Long.valueOf(formatted);
        }
        return Long.valueOf(number.trim());

    }
}
