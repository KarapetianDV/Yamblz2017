package ru.overtired.yamblz2017.main_activity;

import android.util.Log;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.overtired.yamblz2017.App;
import ru.overtired.yamblz2017.data.ForecastRequest;
import ru.overtired.yamblz2017.data.Weather;
import ru.overtired.yamblz2017.data.forecastApi.ForecastApi;
import ru.overtired.yamblz2017.data.forecastApi.ForecastDay;

/**
 * Created by overtired on 18.07.17.
 */

public class WeatherPresenterImpl implements WeatherPresenter {
    private static final String TAG = WeatherPresenterImpl.class.getSimpleName();
    private WeatherView view;
    private WeatherModel model;
    private CompositeDisposable compositeDisposable;

    public WeatherPresenterImpl(WeatherView view, WeatherModel model){
        this.view = view;
        this.model = model;
        model.setPresenter(this);

        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onRefreshWeather() {
        loadForecast();
        model.loadNewWeather();
    }

    @Override
    public void onUpdateWeather() {
        Weather weather = model.getLastWeather();
        if(weather == null){
            onRefreshWeather();
        }else {
            view.setWeather(weather);
        }
    }

    @Override
    public void onResume() {
        onUpdateWeather();
    }

    @Override
    public void setView(WeatherView view){
        this.view = view;
    }

    @Override
    public void onWeatherLoaded() {

        view.setWeather(model.getLastWeather());
        view.hideProgress();
    }

    @Override
    public void onWeatherLoadingError() {
        view.hideProgress();
    }

    @Override
    public void loadForecast() {
        ForecastRequest forecastRequest = new Retrofit.Builder()
                .baseUrl("http://api.wunderground.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ForecastRequest.class);

        compositeDisposable.add(forecastRequest.getForecast(App.getApiWeather(), "ru", "Moscow")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onForecastLoaded, this::onForecastLoadingError));
    }

    @Override
    public void onForecastLoaded(ForecastApi forecastApi) {
        List<ForecastDay> forecastDays = forecastApi.getForecast().getSimpleforecast().getForecastday();
        view.setRecyclerList(forecastDays);
        view.hideProgress();
    }

    @Override
    public void onForecastLoadingError(Throwable t) {
        view.showMessage(WeatherView.MESSAGE.INTERNET_ERROR);
        view.hideProgress();
        Log.e(TAG, "onForecastLoadingError: ", t);
    }

    @Override
    public void onDestroy() {
        this.view = null;
        compositeDisposable.clear();
    }
}
