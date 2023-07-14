package com.data.collector.services;

import java.util.List;
import java.util.UUID;

import com.data.collector.models.Machines;

public interface IMachineServices {
    Machines addMachine(Machines machine);

    List<Machines> findAll();

    Machines findById(UUID id);

    Machines updateMachine(Machines machine, UUID machineId);

    void deleteMachine(UUID id);
}
