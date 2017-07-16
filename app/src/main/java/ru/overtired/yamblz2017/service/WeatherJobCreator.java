package ru.overtired.yamblz2017.service;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import ru.overtired.yamblz2017.service.WeatherRequestJob;

/**
 * Created by overtired on 16.07.17.
 */

public class WeatherJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        switch (tag){
            case WeatherRequestJob.TAG:
                return new WeatherRequestJob();
            default:
                return null;
        }
    }
}
