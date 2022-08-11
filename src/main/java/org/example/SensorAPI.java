package org.example;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.StyledEditorKit;
import java.util.*;

public class SensorAPI {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        //registration(restTemplate);
        //sendMeasurement(restTemplate, new Sensor("Sensor16"));
        //getMeasurements(restTemplate);
        //getSensors(restTemplate);
    }

    public static Sensor registration(RestTemplate restTemplate) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Регистрация сенсора!");
        System.out.println("Введите название сенсора - ");
        String name = scanner.nextLine();
        Sensor sensor = new Sensor(name);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Sensor> request = new HttpEntity<>(sensor, headers);
        String response = restTemplate.postForObject("http://localhost:8080/sensors/registration", request, String.class);
        if ("\"OK\"".equals(response)) {
            System.out.println("Отправка успешно завершена");
        }
        return sensor;
    }

    public static Random random = new Random();

    public static Double getValue() {
        return -100.0 + (100.0 - (-100.0)) * random.nextDouble();
    }

    public static Boolean getRaining() {
        return random.nextBoolean();
    }

    public static void sendMeasurement(RestTemplate restTemplate, Sensor sensor) {
        System.out.println("Отправка измерения от сенсора '" + sensor.getName() + "'");
        Double value = getValue();
        Boolean raining = getRaining();
        Measurement measurement = new Measurement(value,raining,sensor);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Measurement> request = new HttpEntity<>(measurement, headers);
        String response = restTemplate.postForObject("http://localhost:8080/measurements/add", request, String.class);
        if ("\"OK\"".equals(response)) {
            System.out.println("Отправка успешно завершена");
        }
    }

    public static void getMeasurements(RestTemplate restTemplate) {
        ResponseEntity<List<Measurement>> rateResponse =
                restTemplate.exchange("http://localhost:8080/measurements",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Measurement>>() {
                        });
        for (Measurement m : rateResponse.getBody()
        ) {
            System.out.println(m);
        }
    }

    public static void getSensors(RestTemplate restTemplate) {
        ResponseEntity<List<Sensor>> rateResponse =
                restTemplate.exchange("http://localhost:8080/sensors",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Sensor>>() {
                        });
        for (Sensor m : rateResponse.getBody()
        ) {
            System.out.println(m);
        }
    }
}
