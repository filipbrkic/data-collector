package com.data.collector.cron;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.data.collector.models.Machines;
import com.data.collector.repositories.IMachineRepository;
import com.data.collector.utils.SlackAlerts;

@Component
public class Scheduler {

    private IMachineRepository machineRepository;
    private SlackAlerts slackAlerts;

    public Scheduler(IMachineRepository machineRepository, SlackAlerts slackAlerts) {
        this.machineRepository = machineRepository;
        this.slackAlerts = slackAlerts;
    }

    @Scheduled(cron = "0 0/25 * * * ?")
    public void cronJobSch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("Cron job started:: " + strDate);

        List<Machines> machines = machineRepository.findAll();

        List<Map<String, Object>> data = machines.stream().map(machine -> {
            Map<String, Object> machineData = new HashMap<>();
            machineData.put("machine_id", machine.getMachine_id());
            machineData.put("error_description", machine.getError_description());
            machineData.put("timeout", machine.getTimeout());
            machineData.put("gpu_max_cur_temp", machine.getGpu_max_cur_temp());
            machineData.put("hostname", machine.getHostname());
            return machineData;
        }).collect(Collectors.toList());

        slackAlerts.notifications(data);
        System.out.println("Cron job finished.");
    }
}
