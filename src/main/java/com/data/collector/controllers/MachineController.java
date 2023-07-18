package com.data.collector.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.data.collector.models.Machines;
import com.data.collector.services.IMachineServices;

@RestController
@RequestMapping("/api")
public class MachineController {

    private IMachineServices machineServices;

    public MachineController(IMachineServices machineServices) {
        this.machineServices = machineServices;
    }

    @PostMapping("/machines")
    public ResponseEntity<?> addMachine(@RequestBody Machines machine) {
        try {
            Machines dbMachine = machineServices.addMachine(machine);

            return new ResponseEntity<Machines>(dbMachine, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("An error occurred while adding the machine: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/machines")
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            return new ResponseEntity<Page<Machines>>(machineServices.findAll(pageNo, pageSize), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("An error occurred while catching machines: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/machines/{machineId}")
    public ResponseEntity<?> findById(@PathVariable UUID machineId) {
        try {
            Optional<Machines> machine = machineServices.findById(machineId);

            if (machine == null) {
                throw new RuntimeException("Machine id not found - " + machineId);
            }

            return new ResponseEntity<Optional<Machines>>(machine, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(
                    "An error occurred while catching the machine - " + machineId + ": " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/machines/{machineId}")
    public ResponseEntity<?> updateMachines(@RequestBody Machines machines, @PathVariable UUID machineId) {
        try {
            Machines dbMachine = machineServices.updateMachine(machines, machineId);

            return new ResponseEntity<Machines>(dbMachine, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(
                    "An error occurred while updating the machine - " + machineId + ": " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/machines/{machineId}")
    public ResponseEntity<String> deleteMachine(@PathVariable UUID machineId) {
        try {
            machineServices.deleteMachine(machineId);

            return new ResponseEntity<String>("Deleted machine id - " + machineId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(
                    "An error occurred while deleting the machine - " + machineId + ": " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}