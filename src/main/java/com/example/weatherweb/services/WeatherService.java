package com.example.weatherweb.services;

import com.example.weatherweb.controllers.WeatherController;
import com.example.weatherweb.entities.Weather;
import com.example.weatherweb.repositories.WeatherRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;


@Service
public class WeatherService {
    @Autowired
    WeatherRepository weatherRepository;

    public WeatherService() throws IOException {

    }

    public List<Weather> getWeather() {
        return weatherRepository.findAll(Sort.by(Sort.Direction.ASC, "date"));
    }
    public Weather getCityWeather(String city_name) throws IOException, JSONException {
        Weather add = new Weather();
        String main;
        String description;
        int city_id;
        double temp;
        Integer humidity;
        String name;

        String api = "https://v1.nocodeapi.com/weather8/ow/qufGsOEAXQIxGtyD/byCityName?q=";
        String apiwithcity = api + city_name;
        URL url = new URL(apiwithcity);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        int status = con.getResponseCode();
        if (status != 200) {
            throw new RuntimeException("HttpResponseCode:" + status);
        } else {
            String inline = "";
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                inline += scanner.nextLine();
                //if(scanner.nextLine().contains("main"))
                //{

                //}
            }
            scanner.close();

            JSONObject root = new JSONObject(inline);
            JSONArray jsonarray = root.getJSONArray("weather");
            JSONObject lastobject = jsonarray.getJSONObject(0);
            main = lastobject.getString("main");
            name = root.getString("name");
            description = lastobject.getString("description");
            add.setMain(main);
            add.setDescription(description);
            //add.setCity_id(5); // öğren otomatik generate
            //main=root.getJSONArray("weather").getJSONObject(0).getString("main");
            //main=new JSONObject(inline).getJSONArray("weather").getJSONObject(0).getString("main");

            add.setName(name);
            city_id = root.getInt("id");
            add.setCity_id(city_id);

            JSONObject mainobject = root.getJSONObject("main");
            Double feels_like = mainobject.getDouble("feels_like");
            add.setFeels_like(feels_like);

            temp = mainobject.getDouble("temp");
            add.setTemp(temp);

            humidity = mainobject.getInt("humidity");
            add.setHumidity(humidity);

            JSONObject windobject = root.getJSONObject("wind");
            Double wind_speed = windobject.getDouble("speed");
            add.setWind_speed(wind_speed);

            JSONObject sysobject = root.getJSONObject("sys");
            String country = sysobject.getString("country");
            add.setCountry(country);
            add.setDate(new Date());
        }
        return weatherRepository.save(add);
    }

    public Weather saveWeather(Weather weather) {
        return weatherRepository.save(weather);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void timerJob() throws IOException, JSONException {
        //String city_name="Ankara";
        //00.00 için cron=0 0 0 1/1 * ? *
        String api = "https://v1.nocodeapi.com/weather8/ow/qufGsOEAXQIxGtyD/byCityName?q=";
        String apiwithcity = api + "Ankara";
        URL url = new URL(apiwithcity);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.connect();
        int status = con.getResponseCode();
        if (status != 200) {
            throw new RuntimeException("HttpResponseCode:" + status);
        } else {
            String inline = "";
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                inline += scanner.nextLine();

            }
            scanner.close();
            WeatherController weatherController = new WeatherController();
            System.out.println(new Date());

        }

    }

}

