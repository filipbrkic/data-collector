package com.data.collector.cron;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.data.collector.helper.ISchedulerHelper;
import com.data.collector.services.ISlackAlertsServices;

@Component
public class Scheduler {

    private ISlackAlertsServices slackAlertsServices;
    private ISchedulerHelper schedulerHelper;

    public Scheduler(ISlackAlertsServices slackAlertsServices, ISchedulerHelper schedulerHelper) {
        this.slackAlertsServices = slackAlertsServices;
        this.schedulerHelper = schedulerHelper;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void cronJobSch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("Cron job started:: " + strDate);

        List<Map<String, Object>> data = schedulerHelper.machineData();

        slackAlertsServices.notifications(data);

        schedulerHelper.updateMachineData(data);

        System.out.println("Cron job finished.");
    }
}
