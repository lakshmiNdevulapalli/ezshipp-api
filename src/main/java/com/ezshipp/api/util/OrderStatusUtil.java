package com.ezshipp.api.util;

import com.ezshipp.api.enums.OrderStatusEnum;

/**
 * Created by srinivasseri on 2/23/18.
 */
public class OrderStatusUtil {

    public static boolean isOngoing(int status) {
        return (status == OrderStatusEnum.BIKER_ACCEPTED.getStatusId() ||
                status == OrderStatusEnum.ARRIVED_AT_PICKUP.getStatusId() ||
                status == OrderStatusEnum.DROP_AT_ZONE.getStatusId() ||
                status ==    OrderStatusEnum.ON_THE_WAY_TO_PICKUP.getStatusId() ||
                status ==    OrderStatusEnum.ONE_THE_WAY_TO_DELIVERY.getStatusId() ||
                status ==   OrderStatusEnum.PICKED_UP.getStatusId());
    }

    public static boolean isNew(int status) {
        return (status == OrderStatusEnum.NEW.getStatusId());
    }

    public static boolean isCompleted(int status) {
        return (status == OrderStatusEnum.COMPLETED.getStatusId());
    }

    public static boolean isCancelled(int status) {
        return (status == OrderStatusEnum.CANCELLED.getStatusId());
    }

    public static boolean isAccepted(int status) {
        return (status == OrderStatusEnum.BIKER_ACCEPTED.getStatusId());
    }

    public static boolean isZoned(int status) {
        return (status == OrderStatusEnum.DROP_AT_ZONE.getStatusId());
    }

    public static String getStatus(int status) {
        return OrderStatusEnum.getById(status).getStatus();
    }
}
