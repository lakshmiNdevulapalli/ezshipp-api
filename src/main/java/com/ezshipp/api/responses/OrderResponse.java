package com.ezshipp.api.responses;

import com.ezshipp.api.document.Order;
import com.ezshipp.api.enums.OrderStatusEnum;
import com.ezshipp.api.enums.OrderTypeEnum;
import com.ezshipp.api.enums.PaymentTypeEnum;
import com.ezshipp.api.model.ClientOrder;
import com.ezshipp.api.model.Zone;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.ezshipp.api.util.OrderStatusUtil.*;

/**
 * Created by srinivasseri on 2/13/18.
 */
@Data
public class OrderResponse {

    private long totalCount;
    private long instantCount;
    private long fourHoursCount;
    private long sameDayCount;

    private long deletedCount;
    private long cancelledCount;
    private long completedCount;
    private long newCount;
    private long ongoingCount;

    private long monthlyCount;
    private long cashCount;
    private long onlineCount;

    private double codTotal;
    private double uncollectedCODTotal;
    private double collectDeliveryCharges;
    private double monthlyDeliveryCharges;
    private double onlineCharges;

    private Map<String, Long> clientOrdersCount = new HashMap<>();
    private Map<String, List<Order>> clientOrders = new HashMap<>();
    private List<ClientOrder> clientOrderList = new ArrayList<>();
    private Map<String, Long> zonalCount = new HashMap<>();
    private List<Zone> zonalList = new ArrayList<>();

    private List<Order> completedOrderList = new ArrayList<>();
    private List<Order> ongoingOrderList = new ArrayList<>();
    private List<Order> newOrderList = new ArrayList<>();

    private List<Order> documentList;
    private List<Order> zonalDocumentList;

    public List<Order> getDocumentsByType(long type) {
        return documentList.stream().filter(o -> o.getBookingType().equals(Long.toString(type))).collect(Collectors.toList());
    }

    public void setCounts()    {
        if (CollectionUtils.isEmpty(documentList)) {
            return;
        }

        documentList = documentList.stream().filter(o -> o.getStatus() != OrderStatusEnum.CANCELLED.getStatusId()).collect(Collectors.toList());
        totalCount = documentList.size();
        sameDayCount = getDocumentsByType(OrderTypeEnum.SAMEDAY.ordinal()).size();
        fourHoursCount = getDocumentsByType(OrderTypeEnum.FOURHOURS.ordinal()).size();
        instantCount = getDocumentsByType(OrderTypeEnum.INSTANT.ordinal()).size();
        deletedCount = documentList.stream().filter(o -> o.isWhether_Deleted() == true).count();

        newOrderList = documentList.stream().filter(o -> isNew(o.getStatus())).collect(Collectors.toList());
        newCount = newOrderList.size();

        completedOrderList = documentList.stream().filter(o -> isCompleted(o.getStatus())).collect(Collectors.toList());
        completedCount = completedOrderList.size();

        ongoingOrderList = documentList.stream().filter(o -> isOngoing(o.getStatus())).collect(Collectors.toList());
        ongoingCount = ongoingOrderList.size();

        //completedCount = documentList.stream().filter(o -> o.getStatus() == OrderStatusEnum.COMPLETED.getStatusId()).count();
        cancelledCount = documentList.stream().filter(o -> o.getStatus() == OrderStatusEnum.CANCELLED.getStatusId()).count();

        monthlyCount = documentList.stream().filter(o -> o.getPaymentType() == PaymentTypeEnum.MONTHLY.ordinal()).count();

        clientOrdersCount =
                documentList.stream().filter(o -> o.getPaymentType() == PaymentTypeEnum.MONTHLY.ordinal())
                        .filter(o -> (o.getStatus() != OrderStatusEnum.CANCELLED.ordinal()))
                        .collect(Collectors.groupingBy(Order::getCustomerName, Collectors.counting()));

        clientOrders =
                documentList.stream()
                        .filter(o -> o.getPaymentType() == PaymentTypeEnum.MONTHLY.ordinal())
                       .collect(Collectors.groupingBy(Order::getCustomerName));


        cashCount = documentList.stream().filter(o -> o.getPaymentType() == PaymentTypeEnum.CASH.ordinal()).count();
        onlineCount = documentList.stream().filter(o -> o.getPaymentType() == PaymentTypeEnum.ONLINE.ordinal()).count();

        //subtotal_amount
        codTotal = completedOrderList.stream().mapToDouble(o -> o.getSubtotal_amount()).sum();

        uncollectedCODTotal = documentList.stream()
                .filter(o -> o.getStatus() != OrderStatusEnum.COMPLETED.getStatusId())
                .mapToDouble(o -> o.getSubtotal_amount()).sum();

        collectDeliveryCharges = documentList.stream()
                .filter(o -> o.getPaymentType() == PaymentTypeEnum.CASH.ordinal())
                .filter(o -> o.getStatus() == OrderStatusEnum.COMPLETED.getStatusId())
                .mapToDouble(o -> o.getDeliverycharge()).sum()  ;
        monthlyDeliveryCharges = documentList.stream()
                .filter(o -> o.getPaymentType() == PaymentTypeEnum.MONTHLY.ordinal())
                .filter(o -> o.getStatus() == OrderStatusEnum.COMPLETED.getStatusId())

                .mapToDouble(o -> o.getDeliverycharge()).sum();
        onlineCharges = documentList.stream()
                .filter(o -> o.getPaymentType() == PaymentTypeEnum.ONLINE.ordinal())
                .filter(o -> o.getStatus() == OrderStatusEnum.COMPLETED.getStatusId())
                .mapToDouble(o -> o.getDeliverycharge()).sum();

        totalCount = totalCount - cancelledCount;

        applyZoneCount();

        fetchClientOrders();
    }

    private void applyZoneCount() {
        for (Order o : documentList) {
            if (!zonalCount.containsKey(o.getPickupdeponame()) || !zonalCount.containsKey(o.getDeliverydeponame()))  {
                zonalCount.put(zonalCount.containsKey(o.getPickupdeponame())?o.getDeliverydeponame() : o.getPickupdeponame(), 0L);
            }

            if (zonalCount.containsKey(o.getPickupdeponame()))    {
                long count = zonalCount.get(o.getPickupdeponame());
                zonalCount.put(o.getPickupdeponame(), ++count);
            }
            if (zonalCount.containsKey(o.getDeliverydeponame()))    {
                long count = zonalCount.get(o.getDeliverydeponame());
                zonalCount.put(o.getDeliverydeponame(), ++count);
            }
        }

        zonalCount =
                documentList.stream()
                        .filter(o -> !(isCancelled(o.getStatus())))
                        .collect(Collectors.groupingBy(Order::getPickupdeponame, Collectors.counting()));
        System.out.println(zonalCount);
        zonalCount.clear();
        zonalCount =
                documentList.stream()
                        .filter(o -> !(isCancelled(o.getStatus())))
                        .collect(Collectors.groupingBy(Order::getDeliverydeponame, Collectors.counting()));
        System.out.println(zonalCount);
    }


    private void fetchClientOrders() {
        for (String c : clientOrders.keySet()) {
            List<Order> eachClientOrders = clientOrders.get(c);
            ClientOrder clientOrder = new ClientOrder();
            String customerPhone = eachClientOrders.get(0).getCustomerPhone();

            int quantity = eachClientOrders.size();
            long newCount = eachClientOrders.stream().filter(o ->isNew(o.getStatus())).count();
            long completed = eachClientOrders.stream().filter(o -> isCompleted(o.getStatus())).count();
            long pending = eachClientOrders.stream().filter(o -> isOngoing(o.getStatus())).count();
            long cancelled = eachClientOrders.stream().filter(o -> isCancelled(o.getStatus())).count();
            clientOrder.setCustomerName(c);
            clientOrder.setCustomerPhone(customerPhone);
            clientOrder.setQuantity(quantity);
            clientOrder.setCompleted(completed);
            clientOrder.setPending(pending);
            clientOrder.setNewCount(newCount);
            clientOrder.setCancelled(cancelled);
            clientOrder.setZone(eachClientOrders.get(0).getPickupdeponame());
            clientOrderList.add(clientOrder);
        }
        clientOrderList = clientOrderList.stream().sorted(Comparator.comparing(ClientOrder::getQuantity)).collect(Collectors.toList());
    }
}