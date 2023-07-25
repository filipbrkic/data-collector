package com.data.collector.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.data.collector.helper.ISlackAlertsHelper;

import org.springframework.stereotype.Service;

@Service
public class SlackAlertsServices implements ISlackAlertsServices {

    private ISlackAlertsHelper slackAlertsHelper;

    public SlackAlertsServices(ISlackAlertsHelper slackAlertsHelper) {
        this.slackAlertsHelper = slackAlertsHelper;
    }

    public void notifications(List<Map<String, Object>> data) {
        try {
            ArrayList<String> message = new ArrayList<>();
            for (Map<String, Object> i : data) {
                message = slackAlertsHelper.generateTimeoutMessage(i, message);

                message = slackAlertsHelper.generateErrorDescriptionMessage(i, message);

                message = slackAlertsHelper.generateTemperatureMessage(i, message);

                if (message.size() > 0) {
                    String id = i.get("id").toString();
                    Boolean redis = slackAlertsHelper.isMessageSent(id);
                    if (redis == false) {
                        slackAlertsHelper.sendMessage(message);
                    }
                }

                message.clear();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
