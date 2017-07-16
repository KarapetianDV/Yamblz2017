package ru.overtired.yamblz2017;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

/**
 * Created by overtired on 16.07.17.
 */

public class WeatherRequestJob extends Job {
    public static final String TAG = "ru.overtired.yamblz2017.weather_request_job";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        getContext().startService(WeatherService.newIntent(getContext()));
        return Result.SUCCESS;
    }

    public static void scheduleJob(boolean isOn){
        new JobRequest.Builder(TAG)
                .setPeriodic(900000)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }
}
