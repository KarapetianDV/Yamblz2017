package ru.overtired.yamblz2017.main_activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.overtired.yamblz2017.R;
import ru.overtired.yamblz2017.service.WeatherRequestJob;
import ru.overtired.yamblz2017.data.ResponseProcesser;
import ru.overtired.yamblz2017.data.Weather;
import ru.overtired.yamblz2017.data.WeatherFetcher;
import ru.overtired.yamblz2017.data.database.Dao;
import ru.overtired.yamblz2017.service.WeatherService;

/**
 * Created by overtired on 09.07.17.
 */

public class WeatherFragment extends Fragment {
    private FloatingActionButton fab;
    private ImageView cardImage;
    private TextView cardTemp;
    private TextView cardFeelsTemp;

    private SwipeRefreshLayout refreshLayout;

    private AsyncFetcher fetcher;


    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity_weather, container, false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNewWeather();
            }
        });

        cardImage = (ImageView) view.findViewById(R.id.card_image);
        cardTemp = (TextView) view.findViewById(R.id.card_temp);
        cardFeelsTemp = (TextView) view.findViewById(R.id.card_feelslike_temp);

        updateWeather(false);

        return view;
    }

    private void updateWeather(boolean useNetwork) {
        Weather weather = Dao.get(getActivity().getApplicationContext()).getLastWeather();
        if (!useNetwork) {
            if (weather != null) {
                setWeather(weather);
            } else {
                loadNewWeather();
            }
        }else {
            loadNewWeather();
        }
    }

    private class AsyncFetcher extends AsyncTask<String, Void, Weather> {

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

                Log.d("TEMP_IN_C:", weather.city);
                Log.d("WHEATHER:", weather.weather);

                Log.d("WHEATHER_RESPONSE", response);

                Dao.get(getActivity().getApplicationContext()).addWeather(weather);

                return weather;
            } catch (Exception e) {
                Log.d("WHEATHER_RESPONSE", "Exception:(");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Weather weather) {
            setWeather(weather);
            refreshLayout.setRefreshing(false);
        }
    }

    private void setWeather(Weather weather) {
        Picasso.with(getActivity()).load(weather.imageUrl).into(cardImage);
        cardTemp.setText(Double.toString(weather.tempCelsius));
        cardFeelsTemp.setText(Double.toString(weather.feelsLikeCelsius));
    }

    private void loadNewWeather(){
        if (fetcher != null) {
            fetcher.cancel(true);
        }
        fetcher = new AsyncFetcher();
        fetcher.execute();
    }
}
