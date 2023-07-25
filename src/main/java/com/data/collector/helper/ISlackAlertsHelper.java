package com.data.collector.helper;

import java.util.ArrayList;
import java.util.Map;

public interface ISlackAlertsHelper {
    ArrayList<String> generateTimeoutMessage(Map<String, Object> i, ArrayList<String> message);

    ArrayList<String> generateErrorDescriptionMessage(Map<String, Object> i, ArrayList<String> message);

    ArrayList<String> generateTemperatureMessage(Map<String, Object> i, ArrayList<String> message);

    Boolean isMessageSent(String id);

    void sendMessage(ArrayList<String> message);
}
