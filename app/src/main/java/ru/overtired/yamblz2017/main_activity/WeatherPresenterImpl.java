package ru.overtired.yamblz2017.main_activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.overtired.yamblz2017.App;
import ru.overtired.yamblz2017.ForecastAdapter;
import ru.overtired.yamblz2017.R;
import ru.overtired.yamblz2017.data.ForecastRequest;
import ru.overtired.yamblz2017.data.Weather;
import ru.overtired.yamblz2017.data.database.Dao;
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
    private Dao dao;
    private SharedPreferences sharedPreferences;
    private List<ForecastDay> forecastList;
    private MainView mainView;

    public WeatherPresenterImpl(WeatherView view, WeatherModel model, Dao dao, MainView mainView){
        this.view = view;
        this.model = model;
        this.dao = dao;
        this.mainView = mainView;
        model.setPresenter(this);

        compositeDisposable = new CompositeDisposable();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
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

        compositeDisposable.add
                (forecastRequest.getForecast(App.getApiWeather(),
                App.getLanguage(),
                sharedPreferences.getString(App.getContext().getString(R.string.pref_key_select_city), "Moscow"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onForecastLoaded, this::onForecastLoadingError));
    }

    @Override
    public void onForecastLoaded(ForecastApi forecastApi) {
        List<ForecastDay> forecastDays = forecastApi.getForecast().getSimpleforecast().getForecastday();
        this.forecastList = forecastDays;
        saveForecastCache(forecastDays);
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

    private boolean saveForecastCache(List<ForecastDay> forecastDays) {
        for (int i = 0; i < forecastDays.size(); i++) {
            Weather w = new Weather();
            w.city = sharedPreferences.getString(App.getContext().getString(R.string.pref_key_select_city),
                    "Moscow");
            w.tempCelsius = forecastDays.get(i).getHigh().getCelsius();
            w.tempFarengate = forecastDays.get(i).getHigh().getFahrenheit();
            w.feelsLikeCelsius = forecastDays.get(i).getHigh().getCelsius();
            w.feelsLikeFarengate = forecastDays.get(i).getHigh().getFahrenheit();
            w.humidity = forecastDays.get(i).getAvehumidity().toString();
            w.windSpeedKph = forecastDays.get(i).getAvewind().getKph().toString();
            w.windSpeedMph = forecastDays.get(i).getAvewind().getMph().toString();
            w.dewPointCelsius = "";
            w.dewPointFarengate = "";
            w.imageUrl = forecastDays.get(i).getIconUrl();
            w.weather = forecastDays.get(i).getConditions();
            w.date = ForecastAdapter.formatDate(forecastDays.get(i).getDate().getEpoch());
            w.lang = App.getLanguage();

            dao.addWeather(w);
        }

        return true;
    }

    @Override
    public void onClickInRecyclerView(int position) {
        view.RecyclerOnClickListener(forecastList.get(position));
    }
}
