package com.data.collector.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.collector.models.Machines;

public interface IMachineRepository extends JpaRepository<Machines, UUID> {

}
