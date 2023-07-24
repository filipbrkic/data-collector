package com.data.collector.helper;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.data.collector.models.Machines;

@Component
public class MachineHelper implements IMachineHelper {

    @Override
    public Machines dataGenerator(Machines machine) {
        try {
            Random rd = new Random();

            String error_description = null;
            int timeout = ThreadLocalRandom.current().nextInt(0, 2);
            int temperature = ThreadLocalRandom.current().nextInt(85, 95);
            float earn_day = ThreadLocalRandom.current().nextFloat(0, 20);
            float total_flops = ThreadLocalRandom.current().nextFloat(0, 20);

            if (!rd.nextBoolean()) {
                error_description = "Unknown Error";
            }

            machine.setTimeout(timeout);
            machine.setGpu_max_cur_temp(temperature);
            machine.setError_description(error_description);
            machine.setEarn_day(earn_day);
            machine.setTotal_flops(total_flops);

            return machine;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
