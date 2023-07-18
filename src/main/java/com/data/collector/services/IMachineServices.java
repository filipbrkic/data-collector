package com.data.collector.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.data.collector.models.Machines;

public interface IMachineServices {
    Machines addMachine(Machines machine);

    Page<Machines> findAll(Integer pageNo, Integer pageSize);

    Optional<Machines> findById(UUID id);

    Machines updateMachine(Machines machine, UUID machineId);

    void deleteMachine(UUID id);
}
