package com.data.collector.services;

import java.util.List;
import java.util.Map;

public interface ISlackAlertsServices {
    void notifications(List<Map<String, Object>> data);
}
