package ru.overtired.yamblz2017.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by overtired on 14.07.17.
 */

public class Weather {
    @SerializedName("city")
    public String city;

    @SerializedName("weather")
    public String wheather;

    @SerializedName("temp_c")
    public double tempCelsius;
    @SerializedName("temp_f")
    public double tempFarengate;

    @SerializedName("feelslike_c")
    public double feelsLikeCelsius;
    @SerializedName("feelslike_f")
    public double feelsLikeFarengate;

    @SerializedName("relative_humidity")
    public String humidity;

    @SerializedName("wind_kph")
    public double windSpeedKph;
    @SerializedName("wind_mph")
    public double windSpeedMph;

    @SerializedName("dewpoint_c")
    public int dewPointCelsius;
    @SerializedName("dewpoint_f")
    public int dewPointFarengate;
}