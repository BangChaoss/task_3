package com.weather.service;

import com.weather.dto.WeatherResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service

public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();


    public WeatherResponse getWeatherData(double lat, double lon) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", lat)
                .queryParam("longitude", lon)
                .queryParam("current", "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m")
                .queryParam("hourly", "temperature_2m,weather_code,wind_speed_10m")
                .queryParam("daily", "weather_code,temperature_2m_max,temperature_2m_min")
                .queryParam("timezone", "Asia/Bangkok")
                .queryParam("temperature_unit", "celsius")
                .queryParam("forecast_days", 7)
                .toUriString();

        return restTemplate.getForObject(url, WeatherResponse.class);
    }
}
