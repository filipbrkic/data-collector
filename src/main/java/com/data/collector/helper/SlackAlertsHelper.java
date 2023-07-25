package com.data.collector.helper;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.data.collector.models.Webhook;

import redis.clients.jedis.Jedis;

@Component
public class SlackAlertsHelper implements ISlackAlertsHelper {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    private Webhook getWebhook;

    public SlackAlertsHelper(Webhook getWebhook) {
        this.getWebhook = getWebhook;
    }

    public ArrayList<String> generateTimeoutMessage(Map<String, Object> i, ArrayList<String> message) {
        try {
            if ((int) i.get("timeout") > 0) {
                message.add("The machine " + i.get("id") + " " + i.get("hostname")
                        + " requires your attention. The machine is unavailable and last seen " + i.get("timeout")
                        + " seconds ago.");
            }

            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> generateErrorDescriptionMessage(Map<String, Object> i, ArrayList<String> message) {
        try {
            if (i.get("error_description") != null) {
                message.add("The machine " + i.get("id") + " " + i.get("hostname")
                        + " requires your attention. The machine has an error: " + i.get("error_description") + ".");
            }

            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> generateTemperatureMessage(Map<String, Object> i, ArrayList<String> message) {
        try {
            if (i.get("gpu_max_cur_temp") instanceof Integer) {
                int temperature = (Integer) i.get("gpu_max_cur_temp");
                if (temperature > 88) {
                    message.add("The machine " + i.get("id") + " " + i.get("hostname")
                            + " requires your attention. The GPU temperature is too high, currently " + temperature
                            + "\u00B0 and the machine needs to be checked.");
                }
            }

            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isMessageSent(String id) {
        Jedis jedis = null;
        try {
            jedis = new Jedis(redisHost, redisPort);

            boolean exists = jedis.exists(id);

            if (exists != false) {
                jedis.close();
                return true;
            }

            jedis.set(id, id);
            jedis.expire(id, 3600);

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
}
