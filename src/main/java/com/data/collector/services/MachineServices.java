package com.data.collector.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        try {
            UUID uuid = UUID.randomUUID();
            Random rd = new Random();

            String error_description = null;
            int timeout = ThreadLocalRandom.current().nextInt(0, 2);
            int temperature = ThreadLocalRandom.current().nextInt(85, 95);
            float earn_day = ThreadLocalRandom.current().nextFloat(0, 20);
            float total_flops = ThreadLocalRandom.current().nextFloat(0, 20);

            if (!rd.nextBoolean()) {
                error_description = "Unknown Error";
            }

            machine.setMachine_id(uuid);
            machine.setTimeout(timeout);
            machine.setGpu_max_cur_temp(temperature);
            machine.setError_description(error_description);
            machine.setEarn_day(earn_day);
            machine.setTotal_flops(total_flops);

            return machineRepository.save(machine);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Machines> findAll(Integer pageNo, Integer pageSize) {
        try {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            return machineRepository.findAll(paging);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Machines> findAll() {
        try {
            return machineRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Machines> findById(UUID id) {
        try {
            return machineRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public Machines updateMachine(Machines machine, UUID machineId) {
        try {
            Machines existingMachine = machineRepository.getReferenceById(machineId);

            if (existingMachine == null) {
                throw new RuntimeException("Machine id not found - " + machineId);
            }

            if (machine.getHostname() != null && !machine.getHostname().isEmpty()) {
                existingMachine.setHostname(machine.getHostname());
            }
            if (machine.getNum_gpus() != 0) {
                existingMachine.setNum_gpus(machine.getNum_gpus());
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

            existingMachine.setMachine_id(existingMachine.getMachine_id());
            existingMachine.setTimeout(existingMachine.getTimeout());
            existingMachine.setGpu_max_cur_temp(existingMachine.getGpu_max_cur_temp());
            existingMachine.setEarn_day(existingMachine.getEarn_day());
            existingMachine.setTotal_flops(existingMachine.getTotal_flops());

            return machineRepository.save(existingMachine);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public void deleteMachine(UUID id) {
        try {
            Machines machine = machineRepository.getReferenceById(id);

            if (machine == null) {
                throw new RuntimeException("Machine id not found - " + id);
            }

            machineRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
