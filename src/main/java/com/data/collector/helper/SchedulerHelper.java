package com.data.collector.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.data.collector.models.Machines;
import com.data.collector.services.IMachineServices;

@Component
public class SchedulerHelper implements ISchedulerHelper {

    private IMachineServices machineServices;

    public SchedulerHelper(IMachineServices machineServices) {
        this.machineServices = machineServices;
    }

    @Override
    public void updateMachineData(List<Map<String, Object>> data) {
        for (Map<String, Object> i : data) {
            Object idObject = i.get("id");
            if (idObject instanceof UUID) {
                UUID id = (UUID) idObject;
                Optional<Machines> machineOptional = machineServices.findById(id);

                if (machineOptional.isPresent()) {
                    Machines machine = machineOptional.get();
                    machineServices.updateMachineForCron(machine, id);
                } else {
                    System.out.println("Machine not found for id: " + id);
                }
            } else {
                System.out.println("Invalid UUID: " + idObject);
            }
        }
    }

    @Override
    public List<Map<String, Object>> machineData() {
        List<Machines> machines = machineServices.findAll();

        List<Map<String, Object>> data = machines.stream().map(machine -> {
            Map<String, Object> machineData = new HashMap<>();
            machineData.put("id", machine.getId());
            machineData.put("error_description", machine.getError_description());
            machineData.put("timeout", machine.getTimeout());
            machineData.put("gpu_max_cur_temp", machine.getGpu_max_cur_temp());
            machineData.put("hostname", machine.getHostname());
            return machineData;
        }).collect(Collectors.toList());

        return data;
    }

}
