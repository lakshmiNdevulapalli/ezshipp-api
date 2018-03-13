package com.ezshipp.api.util;

import com.ezshipp.api.document.Order;
import net.gpedro.integrations.slack.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srinivasseri on 3/9/18.
 */
public class SlackUtil {

    public static final String EZSHIPP_SLACK_URL = "https://hooks.slack.com/services/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";
    public static final String PENDING_INSTANT_CHANNEL = "#pending-instant";
    public static final String PENDING_FOUR_HOUR_CHANNEL = "#pending-fourhours";

    public static void sendSlackMessage(String message, String channel, Order order) {
        SlackApi api = new SlackApi(EZSHIPP_SLACK_URL);
        SlackMessage slackMessage = new SlackMessage(channel, "ops-admin", message);
        slackMessage.setIcon(":boom:");
        SlackAttachment slackAttachment = new SlackAttachment();
        slackAttachment.addAction(new SlackAction("ops-admin", message, SlackActionType.SELECT, ""));
        slackAttachment.setColor("#b20720");
        slackAttachment.setFallback("Required plain-text summary of the attachment.");
        slackAttachment.setCallbackId("srinivas");
        SlackField slackField = new SlackField();
        slackField.setTitle(order.getOrderseqId());
        if (order != null && order.getBikerName() != null) {
            slackField.setValue(order.getBikerName() + "[" + order.getBikerPhone() + "]");
        } else  {
            slackField.setValue("*No Biker assigned yet.*");
        }
        slackAttachment.addFields(slackField);
        List<SlackAttachment> slackAttachments = new ArrayList<>();
        slackAttachments.add(slackAttachment);
        slackMessage.setAttachments(slackAttachments);
        api.call(slackMessage);

    }
}
