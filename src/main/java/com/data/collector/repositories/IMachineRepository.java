package com.data.collector.repositories;

import java.util.List;
import java.util.UUID;

import com.data.collector.models.Machines;

public interface IMachineRepository {
    Machines addMachine(Machines machine);

    List<Machines> findAll();

    Machines findById(UUID id);

    Machines updateMachine(Machines machine);

    void deleteMachine(UUID id);
}
