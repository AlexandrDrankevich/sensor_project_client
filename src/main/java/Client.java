import dto.MeasurementResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client {
    static final RestTemplate restTemplate = new RestTemplate();
    private static final String sensorName = "Sensor5";

    public static void main(String[] args) {

        //registerSensor(sensorName);
        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            sendMeasurement(Math.ceil(random.nextDouble(-100, 100) * 100) / 100,
                    random.nextBoolean(), sensorName);
        }
        getMeasurements();
    }


    private static void registerSensor(String sensorName) {
        String url = "http://localhost:8080/sensors/registration";
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", sensorName);
        makePostRequest(url, jsonData);

    }

    private static void sendMeasurement(double value, boolean raining, String sensorName) {
        String url = "http://localhost:8080/measurement/add";
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("value", value);
        jsonData.put("raining", raining);
        jsonData.put("sensor", Map.of("name", sensorName));
        makePostRequest(url, jsonData);
    }

    private static void makePostRequest(String url, Map<String, Object> jsonData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);
        try {
            restTemplate.postForObject(url, request, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println("ОШИБКА!");
            System.out.println(e.getMessage());
        }
    }

    public static void getMeasurements() {
        String url = "http://localhost:8080/measurement";
        MeasurementResponse jsonResponse = restTemplate.getForObject(url, MeasurementResponse.class);
        System.out.println(jsonResponse.getMeasurements());
    }
}
