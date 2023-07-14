package com.data.collector;

import com.data.collector.models.Machines;
import com.data.collector.repositories.IMachineRepository;
import com.data.collector.services.IMachineServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CollectorApplicationTests {
    @Mock
    private IMachineServices machineServices;

    @Mock
    private IMachineRepository machineRepository;

    @Autowired
    public CollectorApplicationTests(IMachineServices machineServices, IMachineRepository machineRepository) {
        this.machineServices = machineServices;
        this.machineRepository = machineRepository;
    }

    @Test
    void contextLoads() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Machines> fakeData = objectMapper.readValue(new File("src/test/java/com/data/collector/Machines.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Machines.class));

        when(machineServices.findAll()).thenReturn(fakeData);
        List<Machines> machines = machineServices.findAll();

        Class<?> machinesClass = machines.get(0).getClass();
        Class<?> fakeDataClass = fakeData.get(0).getClass();

        Field[] machineFields = machinesClass.getDeclaredFields();
        Field[] fakeDataFields = fakeDataClass.getDeclaredFields();

        assertEquals(machineFields.length, fakeDataFields.length);

        for (int i = 0; i < machineFields.length; i++) {
            Field machineField = machineFields[i];
            Field fakeDataField = fakeDataFields[i];

            assertEquals(machineField.getName(), fakeDataField.getName());
            assertEquals(machineField.getType(), fakeDataField.getType());
        }
    }

}
