package ru.overtired.yamblz2017;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.evernote.android.job.JobManager;
import com.facebook.stetho.Stetho;

import java.util.Locale;

import ru.overtired.yamblz2017.data.database.Dao;
import ru.overtired.yamblz2017.service.WeatherJobCreator;

/**
 * Created by overtired on 16.07.17.
 */

public class App extends Application {

    private static final String API_WEATHER = "7e79982c03e614a8";

    private Dao dao;
    private static String language;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new WeatherJobCreator());

        Dao dao = Dao.get(getApplicationContext());

        dao.putCityInDatabase("Moscow");

        Stetho.initializeWithDefaults(this);

        language = getLocale().getLanguage();
        Log.d("Lang", language);

        context = getApplicationContext();
    }

    public static String getLanguage(){
        return language;
    }

    public Dao getDao() {
        return dao;
    }
    public void setDao(Dao dao){
        this.dao = dao;
    }

    public static String getApiWeather() {
        return API_WEATHER;
    }

    public static Context getContext() {
        return context;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getLocale(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }
}
