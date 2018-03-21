package com.ezshipp.api.scheduler.order;

import com.ezshipp.api.document.Order;
import com.ezshipp.api.enums.OrderTypeEnum;
import com.ezshipp.api.service.EmailService;
import com.ezshipp.api.service.OrderService;
import com.ezshipp.api.util.SlackUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static com.ezshipp.api.util.OrderStatusUtil.*;
import static com.ezshipp.api.util.SlackUtil.PENDING_FOUR_HOUR_CHANNEL;
import static com.ezshipp.api.util.SlackUtil.PENDING_INSTANT_CHANNEL;

/**
 * Created by srinivasseri on 3/8/18.
 */
@Component
public class OrderScheduleTasks {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private OrderService orderService;

    @Inject
    private EmailService emailService;

    //@Scheduled(fixedRate = 600000) //10mins
    public void slackInstantOrders() throws Exception {
        //log.info("The time is now {}", dateFormat.format(new Date()));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Order> orders = orderService.findAllOrders(OrderTypeEnum.INSTANT);
        Date now = new Date();
        for (Order order : orders) {
            Date orderCreatedTime = format.parse(order.getOrder_datetime());
            long diff = now.getTime() - orderCreatedTime.getTime();
            long diffMinutes = diff / (60 * 1000);
            String message="";
            if (diffMinutes > 15 && isNew(order.getStatus()))   {
                message = String.format("%s[%s] is past due: *%d* mins and not Accepted yet",
                        order.getOrderseqId(), order.getOrderStatus(), diffMinutes);
            } else if (diffMinutes > 30 && isAccepted(order.getStatus())) {
                message = String.format("%s[%s] is past due: *%d* mins and not Picked yet",
                        order.getOrderseqId(), order.getOrderStatus(), diffMinutes);
            } else if (diffMinutes > 45 && isOngoing(order.getStatus())) {
                message = String.format("%s[%s] is past due: *%d* mins and not Delivered yet",
                        order.getOrderseqId(), order.getOrderStatus(), diffMinutes);
            }
            System.out.println(message);
            if (!message.isEmpty()) {
               SlackUtil.sendSlackMessage(message, PENDING_INSTANT_CHANNEL, order);
            }
        }
    }

    //@Scheduled(fixedRate = 1200000) //20mins
    public void slackFourHourOrders() throws Exception {
        //log.info("The time is now {}", dateFormat.format(new Date()));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Order> orders = orderService.findAllOrders(OrderTypeEnum.FOURHOURS);
        Date now = new Date();
        for (Order order : orders) {
            Date orderCreatedTime = format.parse(order.getOrder_datetime());
            long diff = now.getTime() - orderCreatedTime.getTime();
            long diffMinutes = diff / (60 * 1000);
            long diffHours = diff / (60 * 60 * 1000);
            String hourMins = LocalTime.MIN.plus(Duration.ofMinutes( diff )).toString();
            String message="";
            if (diffMinutes > 45 && isNew(order.getStatus()))   {
                message = String.format("%s[%s] is past due: *%d* mins and not Accepted yet",
                        order.getOrderseqId(), order.getOrderStatus(), diffMinutes);
            } else if (diffHours > 1 && isAccepted(order.getStatus())) {
                message = String.format("%s[%s] is past due: *%s* hours and not Picked yet",
                        order.getOrderseqId(), order.getOrderStatus(), hourMins);
            } else if (diffHours > 2 && isOngoing(order.getStatus())) {
                message = String.format("%s[%s] is past due: *%s* hours and not Delivered yet",
                        order.getOrderseqId(), order.getOrderStatus(), hourMins);
            }
            System.out.println(message);
            if (diffMinutes > 45 || diffHours > 1) {
                SlackUtil.sendSlackMessage(message, PENDING_FOUR_HOUR_CHANNEL, order);
            }
        }
    }

    //@Scheduled(fixedRate = 1500000)
    public void sendVijayaOrders() throws Exception {
        //log.info("The time is now {}", dateFormat.format(new Date()));
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        List<Order> orders = orderService.findByCustomerNameOrNumber("Vij-diag- Lal Bunglow", "9000552691");
//        orders = orders.stream().filter(o -> !isCompleted(o.getStatus())).collect(Collectors.toList());
//        Workbook workbook = new ExcelGenerator().createXLS("vijaya-orders", orders);
//        Date now = new Date();
//        FileOutputStream out = new FileOutputStream("vijaya-pending-orders.xlsx");
//        workbook.write(out);
//        out.close();

    }

    //@Scheduled(cron = "0 57 23 * * *") // 1:30PM



    private String constructMessage(long diff, Order order)   {
        long diffMinutes = diff / (60 * 1000);
        if (diffMinutes > 60)   {
            return order.getOrderseqId() + "[" + order.getOrderStatus() + "] is past due: " + (diffMinutes/ 60)   + " hours.";
        } else  {
            return order.getOrderseqId() + "[" + order.getOrderStatus() + "] is past due: " + (diffMinutes)   + " mins.";
        }
    }



}
