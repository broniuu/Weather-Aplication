package com.example.weatheraplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText cityNameEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean connection = isNetworkConnected();

        disableWeatherButton(connection);

        cityNameEditText = findViewById(R.id.cityEditText);
        loadCityName();
    }

    private void disableWeatherButton(boolean connection) {
        if (connection == false){
            findViewById(R.id.buttonWeather).setEnabled(false);
            findViewById(R.id.textViewNoConnection).setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
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