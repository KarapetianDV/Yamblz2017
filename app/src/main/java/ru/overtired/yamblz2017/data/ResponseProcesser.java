package ru.overtired.yamblz2017.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.overtired.yamblz2017.App;

/**
 * Created by overtired on 15.07.17.
 */

public class ResponseProcesser {


    @Nullable
    public static Weather requestWeather(@NonNull String lang, @NonNull String city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.wunderground.com/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        WeatherFetcher fetcher = retrofit.create(WeatherFetcher.class);

        String jsonResponse;
        try {
             jsonResponse = fetcher.getWheather(App.getApiWeather(), lang, city)
                    .execute()
                    .body();
        }catch (IOException e){
            return null;
        }

        JsonParser parser = new JsonParser();

        JsonElement mainPart = parser.parse(jsonResponse)
                .getAsJsonObject()
                .get("current_observation");

        Weather weather = new Gson().fromJson(mainPart, Weather.class);

        weather.city = mainPart.getAsJsonObject()
                .get("display_location")
                .getAsJsonObject()
                .get("city")
                .getAsString();

        weather.lang = lang;

        String dateText = mainPart.getAsJsonObject()
                .get("local_time_rfc822")
                .getAsString();

        weather.date = new SimpleDateFormat(Weather.DATE_FORMAT, Locale.ENGLISH)
                    .format(dateText);

        return weather;
    }

    @Nullable
    public static AutoCompleteFetcher requestAutoComplete() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://autocomplete.wunderground.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(AutoCompleteFetcher.class);
    }
}
