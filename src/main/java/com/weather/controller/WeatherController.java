package com.weather.controller;

import com.weather.dto.WeatherResponse;
import com.weather.enitity.DailyDisplay;
import com.weather.enitity.HourlyDisplay;
import com.weather.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String getWeather(Model model) {
        // Location for Hanoi, Vietnam
        double lat = 21.0285;
        double lon = 105.8542;

        WeatherResponse data = weatherService.getWeatherData(lat, lon);
        
        if (data != null) {
            model.addAttribute("weather", data);
            model.addAttribute("location", "Hà Nội, Vietnam");
            model.addAttribute("currentTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, h:mm a")));
            
            // Format hourly data for different tabs
            List<HourlyDisplay> hourlyTemps = new ArrayList<>();
            List<HourlyDisplay> hourlyWinds = new ArrayList<>();
            List<HourlyDisplay> hourlyPrecip = new ArrayList<>();
            List<HourlyDisplay> hourlyAir = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                String time = data.hourly().time().get(i);
                LocalDateTime dateTime = LocalDateTime.parse(time);
                if (dateTime.isAfter(LocalDateTime.now().minusHours(1))) {
                    String hourLabel = dateTime.format(DateTimeFormatter.ofPattern("ha")).toLowerCase();
                    
                    hourlyTemps.add(new HourlyDisplay(hourLabel, data.hourly().temperature_2m().get(i), "°"));
                    
                    // Convert wind speed from km/h to mph (1 km/h = 0.621371 mph)
                    double windMph = data.hourly().wind_speed_10m().get(i) * 0.621371;
                    hourlyWinds.add(new HourlyDisplay(hourLabel, windMph, " mph"));
                    
                    // Precipitation placeholder - Open-Meteo current precip is mm, we'll use a mock probability or just show mm
                    hourlyPrecip.add(new HourlyDisplay(hourLabel, data.current().precipitation(), "%"));
                    
                    if (hourlyTemps.size() >= 8) break;
                }
            }
            model.addAttribute("hourlyTemps", hourlyTemps);
            model.addAttribute("hourlyWinds", hourlyWinds);
            model.addAttribute("hourlyPrecip", hourlyPrecip);
            
            // Format daily data
            List<DailyDisplay> dailyDisplays = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                String date = data.daily().time().get(i);
                LocalDateTime dateTime = LocalDateTime.parse(date + "T00:00:00");
                String dayName = (i == 0) ? "Today" : dateTime.format(DateTimeFormatter.ofPattern("EEE"));
                dailyDisplays.add(new DailyDisplay(
                    dayName,
                    (int)Math.round(data.daily().temperature_2m_max().get(i)),
                    (int)Math.round(data.daily().temperature_2m_min().get(i)),
                    data.daily().weather_code().get(i)
                ));
            }
            model.addAttribute("dailyList", dailyDisplays);
        }

        return "weather";
    }
}
