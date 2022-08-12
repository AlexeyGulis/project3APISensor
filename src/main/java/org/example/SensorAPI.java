package org.example;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.*;

public class SensorAPI {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        //registration(restTemplate);
        //sendMeasurement(restTemplate, new Sensor("Sensor5"));
        //getMeasurements(restTemplate);
        //getSensors(restTemplate);
        taskTwoProject3APIClientSensor(restTemplate);
        scanner.close();
    }

    public static void taskTwoProject3APIClientSensor(RestTemplate restTemplate) {
        Sensor sensor = registration(restTemplate);
        for (int i = 0; i < 1000; i++) {
            sendMeasurement(restTemplate, sensor);
        }
        getMeasurements(restTemplate);
    }

    public static Sensor registration(RestTemplate restTemplate) {
        System.out.println("Регистрация сенсора!");
        System.out.print("Введите название сенсора: ");
        String name = scanner.nextLine();
        Sensor sensor = new Sensor(name);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Sensor> request = new HttpEntity<>(sensor, headers);
        String response = restTemplate.postForObject("http://localhost:8080/sensors/registration", request, String.class);
        if ("\"OK\"".equals(response)) {
            System.out.println("Регистрация " + name + " успешно завершена");
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
        Measurement measurement = new Measurement();
        Double value = Double.parseDouble(String.format(Locale.US, "%.2f", getValue()));
        Boolean raining = getRaining();
        measurement.setValue(value);
        measurement.setRaining(raining);
        measurement.setSensor(sensor);
        System.out.println("value = " + value + ", raining = " + raining + ", sensor = " + sensor.getName());
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
        List<Measurement> measurements = rateResponse.getBody();
        List<Double> temperature = new ArrayList<>();
        List<Integer> numberMeasurement = new ArrayList<>();
        int i = 0;
        for (Measurement m : measurements
        ) {
            temperature.add(m.getValue());
            numberMeasurement.add(++i);
            System.out.println(m);
        }
        // Create Chart
        XYChart chart = QuickChart.getChart("Graph of Temperatures",
                "Number of measurement","Temperature",  "Temperature/Number", numberMeasurement, temperature);
        // Show it
        new SwingWrapper(chart).displayChart();
    }

    public static void getSensors(RestTemplate restTemplate) {
        ResponseEntity<List<Sensor>> rateResponse =
                restTemplate.exchange("http://localhost:8080/sensors",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Sensor>>() {
                        });
        for (Sensor s : rateResponse.getBody()
        ) {
            System.out.println(s);
        }
    }
}
