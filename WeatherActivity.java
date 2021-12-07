package com.example.weatheraplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent = getIntent();
        String cityNameToQuerry = intent.getStringExtra("KEY_CITY_NAME") + ",pl";

        reRun(cityNameToQuerry);

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        initializeRefreshListener(cityNameToQuerry);
    }

    private void reRun(String cityNameToQuerry) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        getData(cityNameToQuerry);
                        Thread.sleep(5000);
                    }
                } catch(InterruptedException e){
                    Log.e("tagErr", e.getMessage());
                }
            }
        }).start();
    }

    private void getData(String cityNameToQuerry) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceholderAPI jsonPlaceholderAPI = retrofit.create(JsonPlaceholderAPI.class);

        Call<Post> call = jsonPlaceholderAPI.getWeather(cityNameToQuerry, "749561a315b14523a8f5f1ef95e45864", "metric");

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
                    startActivity(intent);
                    return;
                }
                Post post = response.body();
                String cityName = post.getName();
                TextView cityViev = findViewById(R.id.cityView);
                cityViev.setText(cityName);

                Double tempValue = post.getMain().getTemp();
                String tempValueReading = tempValue.toString();
                TextView tempView = findViewById(R.id.tempView);
                tempView.setText(tempValueReading+" °C");

                Integer pressureValue = post.getMain().getPressure();
                String pressureValueReading = pressureValue.toString();
                TextView pressureView = findViewById(R.id.pressureView);
                pressureView.setText(pressureValueReading+" hPa");

                Integer humidityValue = post.getMain().getHumidity();
                String humidityValueReading = humidityValue.toString();
                TextView humidityView = findViewById(R.id.humidityView);
                humidityView.setText(humidityValueReading+" %");

                Double tempMinValue = post.getMain().getTemp_min();
                String tempMinValueReading = tempMinValue.toString();
                TextView tempMinView = findViewById(R.id.tempMinView);
                tempMinView.setText(tempMinValueReading+" °C");

                Double tempMaxValue = post.getMain().getTemp_max();
                String tempMaxValueReading = tempMaxValue.toString();
                TextView tempMaxView = findViewById(R.id.tempMaxView);
                tempMaxView.setText(tempMaxValueReading+" °C");

                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                String currentTimeText = dateFormat.format(currentTime);
                TextView timeView = findViewById(R.id.timeView);
                timeView.setText(currentTimeText);

                saveCityName(cityName);


            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    private void initializeRefreshListener(String cityName) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(cityName);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void saveCityName(String cityName){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CITY_KEY", cityName);
        editor.apply();
    }
}