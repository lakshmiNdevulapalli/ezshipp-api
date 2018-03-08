package com.ezshipp.api.enums;

/**
 * Created by srinivasseri on 2/3/18.
 */
public enum OrderStatusEnum {
    NA(0, "NO"),
    NEW(1, "New"),
    PICKED_UP(4, "Picked"),
    ONE_THE_WAY_TO_DELIVERY(11, "Enroute Delivery"),
    CANCELLED(5, "Cancelled"),
    DELIVERED(6, "Delivered"),
    BIKER_ACCEPTED(7, "Accepted"),
    REJECTED(8, "Rejected"),
    EXPIRED(9, "Expired"),
    ARRIVED_AT_PICKUP(10, "Arrived At Pickup"),
    ZONE_ORDER_ACCEPTED(11, "Zone Order Accepted"),
    DELIVERY_LOCATION(12, "At Delivery Point"),
    COMPLETED(14, "Completed"),
    DROP_AT_ZONE(15, "Zoned"),
    ON_THE_WAY_TO_PICKUP(16, "Enroute Pickup"),
    DRIVER_ACCEPTED_AT_ZONE(18, "Driver Accepted Zone Order");

    OrderStatusEnum(final int statusId, final String status)   {
        this.statusId = statusId;
        this.status = status;
    }

    private final int statusId;
    private final String status;

    public int getStatusId()    {
        return statusId;
    }

    public String getStatus()    {
        return status;
    }

    public static OrderStatusEnum getById(int statusId) {
        for(OrderStatusEnum e : values()) {
            if(e.statusId == statusId)
                return e;
        }
        return NA;
    }

    public static OrderStatusEnum getByName(String status) {
        for(OrderStatusEnum e : values()) {
            if(e.status.equals(status))
                return e;
        }
        return NA;
    }
}
