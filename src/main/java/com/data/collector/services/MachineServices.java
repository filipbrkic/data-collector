package com.data.collector.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.data.collector.helper.IMachineHelper;
import com.data.collector.models.Machines;
import com.data.collector.repositories.IMachineRepository;

import jakarta.transaction.Transactional;

@Service
public class MachineServices implements IMachineServices {

    private IMachineRepository machineRepository;
    private IMachineHelper machineHelper;

    public MachineServices(IMachineRepository machineRepository, IMachineHelper machineHelper) {
        this.machineRepository = machineRepository;
        this.machineHelper = machineHelper;
    }

    @Transactional
    @Override
    public Machines addMachine(Machines machine) {
        try {
            Machines newMachine = machineHelper.dataGenerator(machine);

            return machineRepository.save(newMachine);
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
    public Machines updateMachine(Machines machine, UUID id) {
        try {
            Machines existingMachine = machineRepository.getReferenceById(id);

            if (existingMachine == null) {
                throw new RuntimeException("Machine id not found - " + id);
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

    @Transactional
    @Override
    public Machines updateMachineForCron(Machines machine, UUID id) {
        Machines existingMachine = machineRepository.getReferenceById(id);

        if (existingMachine == null) {
            throw new RuntimeException("Machine id not found - " + id);
        }
        existingMachine.setHostname(existingMachine.getHostname());
        existingMachine.setNum_gpus(existingMachine.getNum_gpus());
        existingMachine.setGpu_name(existingMachine.getGpu_name());
        existingMachine.setGpu_ram(existingMachine.getGpu_ram());
        existingMachine.setCpu_name(existingMachine.getCpu_name());
        existingMachine.setTimeout(existingMachine.getTimeout());
        existingMachine.setGpu_max_cur_temp(existingMachine.getGpu_max_cur_temp());
        existingMachine.setEarn_day(existingMachine.getEarn_day());
        existingMachine.setTotal_flops(existingMachine.getTotal_flops());

        Machines newMachine = machineHelper.dataGenerator(existingMachine);

        return machineRepository.save(newMachine);
    }
}
