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

    @GetMapping("/machines/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        try {
            Optional<Machines> machine = machineServices.findById(id);

            if (machine.isEmpty()) {
                throw new RuntimeException("Machine id not found - " + id);
            }

            return new ResponseEntity<Optional<Machines>>(machine, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(
                    "An error occurred while catching the machine - " + id + ": " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/machines/{id}")
    public ResponseEntity<?> updateMachines(@RequestBody Machines machines, @PathVariable UUID id) {
        try {
            Machines dbMachine = machineServices.updateMachine(machines, id);

            return new ResponseEntity<Machines>(dbMachine, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(
                    "An error occurred while updating the machine - " + id + ": " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/machines/{id}")
    public ResponseEntity<String> deleteMachine(@PathVariable UUID id) {
        try {
            machineServices.deleteMachine(id);

            return new ResponseEntity<String>("Deleted machine id - " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(
                    "An error occurred while deleting the machine - " + id + ": " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}