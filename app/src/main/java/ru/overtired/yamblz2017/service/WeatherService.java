package ru.overtired.yamblz2017.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.overtired.yamblz2017.data.ResponseProcesser;
import ru.overtired.yamblz2017.data.Weather;
import ru.overtired.yamblz2017.data.WeatherFetcher;
import ru.overtired.yamblz2017.data.database.Dao;

/**
 * Created by overtired on 16.07.17.
 */

public class WeatherService extends IntentService {
    public static final String SERVICE_NAME = "ru.overtired.yamblz2017.weather_service";

    public static final String ACTION_UPDATE_WEATHER = "ru.overtired.yamblz2017.weather.update_weather";

    public WeatherService(){
        super(SERVICE_NAME);
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, WeatherService.class);
        return intent;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.wunderground.com/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        WeatherFetcher fetcher = retrofit.create(WeatherFetcher.class);


        try {
            String response = fetcher.getWheather("7e79982c03e614a8", "RU", "Moscow")
                    .execute()
                    .body();

            Weather weather = ResponseProcesser.getWheatherFromJsonResponse(response);

            Log.d("TEMP_IN_C:", weather.city);
            Log.d("WHEATHER:", weather.weather);

            Log.d("WHEATHER_RESPONSE", response);

            Dao.get(getApplicationContext()).addWeather(weather);
            sendBroadcast(new Intent(ACTION_UPDATE_WEATHER));
        } catch (Exception e) {
            Log.d("WHEATHER_RESPONSE", "Exception:(");
            e.printStackTrace();
        }
    }
}
