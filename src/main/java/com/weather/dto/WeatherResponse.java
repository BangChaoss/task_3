package com.weather.dto;

import java.util.List;

public record WeatherResponse(
    double latitude,
    double longitude,
    String timezone,
    Current current,
    Hourly hourly,
    Daily daily
) {
    public record Current(
        String time,
        double temperature_2m,
        int relative_humidity_2m,
        double apparent_temperature,
        double precipitation,
        double wind_speed_10m,
        int weather_code
    ) {}

    public record Hourly(
        List<String> time,
        List<Double> temperature_2m,
        List<Double> wind_speed_10m,
        List<Integer> weather_code
    ) {}

    public record Daily(
        List<String> time,
        List<Integer> weather_code,
        List<Double> temperature_2m_max,
        List<Double> temperature_2m_min
    ) {}
}
