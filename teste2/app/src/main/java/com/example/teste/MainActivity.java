package com.example.teste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText cityNameEditText;
    private Button searchButton;
    private static final String API_KEY = "6becba70eaf3c4cebc4f41dc5493863c";

    /*https://home.openweathermap.org/api_keys Para pegar o API kEY*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityNameEditText = findViewById(R.id.cityNameEditText);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = cityNameEditText.getText().toString();
                new FetchWeatherTask().execute(cityName);
            }
        });
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String cityName = params[0];
            try {
                String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + API_KEY + "&units=metric&lang=pt";
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String weatherInfo) {

            if (weatherInfo == null) {
                // Mostra uma mensagem de erro se não conseguir obter as informações do tempo
                Toast.makeText(MainActivity.this, "Erro ao obter a previsão do tempo. Verifique o nome da cidade e tente novamente.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject json = new JSONObject(weatherInfo);
                    if (json.getInt("cod") != 200) {
                        // Mostra uma mensagem de erro se a cidade não for encontrada
                        Toast.makeText(MainActivity.this, "Cidade não encontrada. Por favor, verifique o nome e tente novamente.", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, WeatherResultActivity.class);
                        intent.putExtra("weatherInfo", weatherInfo);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Erro ao interpretar as informações do tempo.", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
