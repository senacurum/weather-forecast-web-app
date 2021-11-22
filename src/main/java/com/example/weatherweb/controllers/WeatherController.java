package com.example.weatherweb.controllers;

import com.example.weatherweb.entities.Weather;
import com.example.weatherweb.services.WeatherService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


@Controller
public class WeatherController {
    @Autowired
    WeatherService weatherService;

    @GetMapping("getweather")
    public String getWeather(Model model) {
        List<Weather> weathers=weatherService.getWeather().subList(weatherService.getWeather().size()-15,weatherService.getWeather().size());
        model.addAttribute("weathers",weathers);
        return "weathers";
    }

    @GetMapping("getcityweather/{city_name}")
    public Weather getCityWeather(@PathVariable String city_name) throws JSONException, IOException {
       return weatherService.getCityWeather(city_name);
    }

    @PostMapping("addweather")
    public Weather SaveWeather(@RequestBody String city_name) {
        //Weather weather=new Weather();
        //weather.setCity_id(5);
        //weather.setMain(weather.getMain());
        //weather.setDescription(weather.getDescription());
        Weather weather = null;
        try {
            weather = getCityWeather(city_name);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weatherService.saveWeather(weather);

    }


}