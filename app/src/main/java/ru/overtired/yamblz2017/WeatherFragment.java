package ru.overtired.yamblz2017;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.overtired.yamblz2017.data.ResponseProcesser;
import ru.overtired.yamblz2017.data.Weather;
import ru.overtired.yamblz2017.data.WeatherFetcher;

/**
 * Created by overtired on 09.07.17.
 */

public class WeatherFragment extends Fragment {
    private FloatingActionButton fab;


    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity_weather,container,false);
        fab = (FloatingActionButton) view.findViewById(R.id.wheather_fragment_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncFetcher fetcher = new AsyncFetcher();
                fetcher.execute();
            }
        });

        return view;
    }

    class AsyncFetcher extends AsyncTask<String,Void,Weather>{

        @Override
        protected Weather doInBackground(String... params) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.wunderground.com/api/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            WeatherFetcher fetcher = retrofit.create(WeatherFetcher.class);

            try {
                String response = fetcher.getWheather("7e79982c03e614a8","RU","Moscow")
                        .execute()
                        .body();

                Weather weather =  ResponseProcesser.getWheatherFromJsonResponse(response);

                Log.d("TEMP_IN_C:", weather.city);
                Log.d("WHEATHER:", weather.wheather);

                Log.d("WHEATHER_RESPONSE",response);
            }catch (Exception e){
                Log.d("WHEATHER_RESPONSE","Exception:(");
                e.printStackTrace();
            }
            return null;
        }
    }
}
