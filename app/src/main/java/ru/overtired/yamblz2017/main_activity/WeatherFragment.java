package ru.overtired.yamblz2017.main_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.overtired.yamblz2017.ForecastAdapter;
import ru.overtired.yamblz2017.R;
import ru.overtired.yamblz2017.data.Weather;
import ru.overtired.yamblz2017.data.forecastApi.ForecastDay;
import ru.overtired.yamblz2017.service.WeatherService;

/**
 * Created by overtired on 09.07.17.
 */

public class WeatherFragment extends Fragment implements WeatherView{
    public static final String TAG = "WeatherFragment";

    WeatherPresenter presenter;

    private Unbinder unbinder;

    private SharedPreferences sharedPreferences;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.forecast_recyclerview)
    RecyclerView forecastRecyclerView;

    ForecastAdapter adapter = new ForecastAdapter();

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.loadForecast();
        }
    };

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HolderFragment<WeatherPresenter> holder = (HolderFragment<WeatherPresenter>)
                getFragmentManager()
                .findFragmentByTag(HolderFragment.TAG);
        if(holder ==null){
            holder = new HolderFragment<>();
            getFragmentManager().beginTransaction()
                    .add(holder,HolderFragment.TAG)
                    .commit();
        }

        presenter = holder.getPresenter();
        if(presenter==null){
            presenter = new WeatherPresenterImpl(this,
                    new WeatherModelImpl(getActivity().getApplicationContext()));
            holder.setPresenter(presenter);
        }else{
            presenter.setView(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();

        IntentFilter filter = new IntentFilter(WeatherService.ACTION_UPDATE_WEATHER);
        getActivity().registerReceiver(updateReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(updateReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity_weather, container, false);
        unbinder = ButterKnife.bind(this, view);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadForecast();
            }
        });

        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        presenter.loadForecast();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setWeather(Weather weather) {
        // Empty method
    }

    @Override
    public void hideProgress() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(MESSAGE message) {
        String text = "";
        switch (message){
            case INTERNET_ERROR: text = getString(R.string.no_internet_error);
        }
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRecyclerList(List<ForecastDay> forecastDays) {
        Log.d(TAG, "setRecyclerList: " + forecastDays);
        adapter = new ForecastAdapter(forecastDays);
        forecastRecyclerView.setAdapter(adapter);
    }
}
