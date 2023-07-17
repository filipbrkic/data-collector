package com.data.collector.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.data.collector.models.Webhook;

import org.springframework.http.*;
import redis.clients.jedis.Jedis;

import org.springframework.stereotype.Service;

@Service
public class SlackAlerts {

    private Webhook getWebhook;

    public SlackAlerts(Webhook getWebhook) {
        this.getWebhook = getWebhook;
    }

    public void notifications(List<Map<String, Object>> data) {
        try {
            ArrayList<String> message = new ArrayList<>();
            for (Map<String, Object> i : data) {
                message = generateTimeoutMessage(i, message);

                message = generateErrorDescriptionMessage(i, message);

                message = generateTemperatureMessage(i, message);

                if (message.size() > 0) {
                    String machineId = i.get("machine_id").toString();
                    Boolean redis = this.isMessageSent(machineId);
                    if (redis == false) {
                        this.sendMessage(message);
                    }
                }

                message.clear();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isMessageSent(String machine_id) {
        Jedis jedis = null;
        try {
            jedis = new Jedis("localhost", 6379);

            boolean exists = jedis.exists(machine_id);

            if (exists != false) {
                jedis.close();
                return true;
            }

            jedis.set(machine_id, machine_id);
            jedis.expire(machine_id, 3600);

            jedis.close();

            return false;
        } catch (Exception e) {
            if (jedis != null) {
                jedis.close();
            }
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(ArrayList<String> message) {
        try {
            String username = "slack-alert-bot";
            String icon_emoji = ":robot_face:";

            String messageText = "```\n" + String.join("\n", message) + "\n```";
            String requestBody = "{\"text\":\"" + messageText + "\", \"username\":\"" + username
                    + "\", \"icon_emoji\":\"" + icon_emoji + "\"}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();

            String webhook = getWebhook.getWebhook();

            ResponseEntity<String> responseEntity = restTemplate.exchange(webhook, HttpMethod.POST, requestEntity,
                    String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                System.out.println("Slack notification sent successfully!");
            } else {
                System.out.println("Failed to send Slack notification.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> generateTimeoutMessage(Map<String, Object> i, ArrayList<String> message) {
        if ((int) i.get("timeout") > 0) {
            message.add("The machine " + i.get("machine_id") + " " + i.get("hostname")
                    + " requires your attention. The machine is unavailable and last seen " + i.get("timeout")
                    + " seconds ago.");
        }

        return message;
    }

    public ArrayList<String> generateErrorDescriptionMessage(Map<String, Object> i, ArrayList<String> message) {
        if (i.get("error_description") != null) {
            message.add("The machine " + i.get("machine_id") + " " + i.get("hostname")
                    + " requires your attention. The machine has an error: " + i.get("error_description") + ".");
        }

        return message;
    }

    public ArrayList<String> generateTemperatureMessage(Map<String, Object> i, ArrayList<String> message) {
        if (i.get("gpu_max_cur_temp") instanceof Integer) {
            int temperature = (Integer) i.get("gpu_max_cur_temp");
            if (temperature > 88) {
                message.add("The machine " + i.get("machine_id") + " " + i.get("hostname")
                        + " requires your attention. The GPU temperature is too high, currently " + temperature
                        + "\u00B0 and the machine needs to be checked.");
            }
        }

        return message;
    }
}
