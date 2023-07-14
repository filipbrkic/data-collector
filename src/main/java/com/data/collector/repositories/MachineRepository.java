package com.data.collector.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.data.collector.models.Machines;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class MachineRepository implements IMachineRepository {

    private EntityManager entityManager;

    public MachineRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Machines addMachine(Machines machine) {
        try {
            Machines dbMachine = entityManager.merge(machine);

            return dbMachine;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Machines> findAll() {
        try {
            TypedQuery<Machines> query = entityManager.createQuery("from Machines", Machines.class);

            List<Machines> machines = query.getResultList();

            return machines;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Machines findById(UUID id) {
        try {
            Machines machine = entityManager.find(Machines.class, id);

            return machine;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Machines updateMachine(Machines machine) {
        try {
            Machines dbMachine = entityManager.merge(machine);

            return dbMachine;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMachine(UUID id) {
        try {
            Machines machine = entityManager.find(Machines.class, id);

            entityManager.remove(machine);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
