package com.data.collector;

import com.data.collector.helper.ISlackAlertsHelper;
import com.data.collector.models.Machines;
import com.data.collector.repositories.IMachineRepository;
import com.data.collector.services.IMachineServices;
import com.data.collector.services.ISlackAlertsServices;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CollectorApplicationTests {
    @Mock
    private IMachineServices machineServices;

    @Mock
    private IMachineRepository machineRepository;

    @Mock
    private ISlackAlertsHelper slackAlertsHelper;

    @Mock
    private ISlackAlertsServices slackAlertsServices;

    List<Machines> fakeData;

    @BeforeEach
    void setup() throws IOException {
        MockitoAnnotations.openMocks(this);

        ObjectMapper objectMapper = new ObjectMapper();
        fakeData = objectMapper.readValue(new File("src/test/java/com/data/collector/Machines.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Machines.class));
    }

    @Test
    void findAllTest() throws IOException {
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

    @Test
    void slackAlertsTimeoutMessage() throws IOException {

        Machines[] machines = createMachines(100, "timeout");
        ArrayList<String> expectedMessage = new ArrayList<>();
        ArrayList<String> message = new ArrayList<>();

        for (Machines machine : machines) {
            if (machine.getTimeout() > 0) {
                expectedMessage.add("Timeout message");
                Map<String, Object> machineData = new HashMap<>();
                machineData.put("hostname", machine.getHostname());
                machineData.put("error_description", machine.getError_description());
                machineData.put("id", machine.getId());
                machineData.put("timeout", machine.getTimeout());
                machineData.put("gpu_max_cur_temp", machine.getGpu_max_cur_temp());

                when(slackAlertsHelper.generateTimeoutMessage(machineData, message)).thenReturn(expectedMessage);

                message = slackAlertsHelper.generateTimeoutMessage(machineData, message);
            }
        }

        assertEquals(expectedMessage, message);
    }

    Machines[] createMachines(Integer number, String string) {
        int count = 0;

        Machines[] machineInstances = new Machines[number];

        while (number > count) {
            machineInstances[count] = this.instance(string);
            count++;
        }

        return machineInstances;
    }

    Machines instance(String string) {
        Machines machine = null;
        if (string == "timeout") {
            Random rd = new Random();
            String error_description = null;
            if (!rd.nextBoolean()) {
                error_description = "Unknown Error";
            }

            machine = new Machines(null, string, ThreadLocalRandom.current().nextInt(0, 2), 0, 0, string, 0,
                    ThreadLocalRandom.current().nextInt(85, 95), string, 0, error_description);

        }
        return machine;
    }
}
