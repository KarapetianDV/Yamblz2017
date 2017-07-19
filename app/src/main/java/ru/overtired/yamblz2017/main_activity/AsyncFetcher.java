package ru.overtired.yamblz2017.main_activity;

import android.os.AsyncTask;
import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.overtired.yamblz2017.data.ResponseProcesser;
import ru.overtired.yamblz2017.data.Weather;
import ru.overtired.yamblz2017.data.WeatherFetcher;
import ru.overtired.yamblz2017.data.database.Dao;

/**
 * Created by overtired on 18.07.17.
 */

public class AsyncFetcher extends AsyncTask<String, Void, Weather> {

    private Runnable runnable;

    public AsyncFetcher(Runnable runnable){
        this.runnable = runnable;
    }

    @Override
    protected Weather doInBackground(String... params) {

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

            Log.d("WHEATHER_RESPONSE", response);

            //Dao.get(getActivity().getApplicationContext()).addWeather(weather);

            return weather;
        } catch (Exception e) {
            Log.d("WHEATHER_RESPONSE", "Exception:(");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Weather weather) {
        runnable.run();
    }
}
