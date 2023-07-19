package com.data.collector.helper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

}
