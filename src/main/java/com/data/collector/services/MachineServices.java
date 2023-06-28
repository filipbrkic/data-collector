package com.data.collector.services;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.data.collector.models.Machines;
import com.data.collector.repositories.IMachineRepository;

import jakarta.transaction.Transactional;

@Service
public class MachineServices implements IMachineServices {

    private IMachineRepository machineRepository;

    public MachineServices(IMachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Transactional
    @Override
    public Machines addMachine(Machines machine) {
        UUID uuid = UUID.randomUUID();
        Random rd = new Random();

        int timeout = ThreadLocalRandom.current().nextInt(0, 2);
        float temperature = ThreadLocalRandom.current().nextFloat(85, 95);
        String error_description = null;
        if (!rd.nextBoolean()) {
            error_description = "Unknown Error";
        }

        machine.setMachine_id(uuid);
        machine.setTimeout(timeout);
        machine.setGpu_max_cur_temp(temperature);
        machine.setError_description(error_description);

        return machineRepository.addMachine(machine);
    }

    @Override
    public List<Machines> findAll() {
        return machineRepository.findAll();
    }

    @Override
    public Machines findById(UUID id) {
        return machineRepository.findById(id);
    }

    @Transactional
    @Override
    public Machines updateMachine(Machines machine, UUID machineId) {
        Machines existingMachine = machineRepository.findById(machineId);

        if (existingMachine == null) {
            throw new RuntimeException("Machine id not found - " + machineId);
        }

        
        if (machine.getHostname() != null && !machine.getHostname().isEmpty()) {
            existingMachine.setHostname(machine.getHostname());
        }
        if (machine.getNum_gpus() != 0) {
            existingMachine.setNum_gpus(machine.getNum_gpus());
        }
        if (machine.getTotal_flops() != 0.0f) {
            existingMachine.setTotal_flops(machine.getTotal_flops());
        }
        if (machine.getGpu_name() != null && !machine.getGpu_name().isEmpty()) {
            existingMachine.setGpu_name(machine.getGpu_name());
        }
        if (machine.getGpu_ram() != 0) {
            existingMachine.setGpu_ram(machine.getGpu_ram());
        }
        if (machine.getCpu_name() != null && !machine.getCpu_name().isEmpty()) {
            existingMachine.setCpu_name(machine.getCpu_name());
        }
        if (machine.getEarn_day() != 0.0f) {
            existingMachine.setEarn_day(machine.getEarn_day());
        }
        
        existingMachine.setMachine_id(existingMachine.getMachine_id());
        existingMachine.setTimeout(existingMachine.getTimeout());
        existingMachine.setGpu_max_cur_temp(existingMachine.getGpu_max_cur_temp());
        existingMachine.setError_description(existingMachine.getError_description());

        return machineRepository.updateMachine(existingMachine);
    }

    @Transactional
    @Override
    public void deleteMachine(UUID id) {
        Machines machine = machineRepository.findById(id);

        if (machine == null) {
            throw new RuntimeException("Machine id not found - " + id);
        }

        machineRepository.deleteMachine(id);
    }
    
}
