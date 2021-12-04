package com.example.weatheraplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText cityNameEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityNameEditText = findViewById(R.id.cityEditText);
        loadCityName();
    }

    public void sendText(View view) {

        String cityNameText = cityNameEditText.getText().toString();

        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("KEY_CITY_NAME", cityNameText);
        startActivity(intent);
    }
    public void loadCityName(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String cityName = sharedPreferences.getString("CITY_KEY", "Default value");
        cityNameEditText.setText(cityName);
    }
}