package com.example.teste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherResultActivity extends AppCompatActivity {
    private TextView weatherInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_result);

        weatherInfoTextView = findViewById(R.id.weatherInfoTextView);

        Intent intent = getIntent();
        String weatherInfo = intent.getStringExtra("weatherInfo");
        try {
            JSONObject json = new JSONObject(weatherInfo);
            String cityName = json.getString("name");
            JSONObject main = json.getJSONObject("main");
            double temperature = main.getDouble("temp");
            double feelsLike = main.getDouble("feels_like");
            int humidity = main.getInt("humidity");
            JSONObject wind = json.getJSONObject("wind");
            double windSpeed = wind.getDouble("speed");
            JSONArray weatherArray = json.getJSONArray("weather");
            JSONObject weather = weatherArray.getJSONObject(0);
            String description = weather.getString("description");

            String result = "Cidade: " + cityName +
                    "\nTemperatura: " + temperature + "°C" +
                    "\nSensação Térmica: " + feelsLike + "°C" +
                    "\nUmidade: " + humidity + "%" +
                    "\nVelocidade do Vento: " + windSpeed + " m/s" +
                    "\nDescrição: " + description;
            weatherInfoTextView.setText(result);
        } catch (JSONException e) {
            e.printStackTrace();
            weatherInfoTextView.setText("Erro ao interpretar as informações do tempo.");
        }
    }
}
