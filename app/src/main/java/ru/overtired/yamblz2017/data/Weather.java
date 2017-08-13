package ru.overtired.yamblz2017.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by overtired on 14.07.17.
 */

public class Weather {
    public static final String DATE_FORMAT = "EEE, d MMM yyyy hh:mm:ss Z";

    public String city;
    public String lang;
    public String date;

    @SerializedName("weather")
    public String weather;

    @SerializedName("temp_c")
    public String tempCelsius;
    @SerializedName("temp_f")
    public String tempFarengate;

    @SerializedName("feelslike_c")
    public String feelsLikeCelsius;
    @SerializedName("feelslike_f")
    public String feelsLikeFarengate;

    @SerializedName("relative_humidity")
    public String humidity;

    @SerializedName("wind_kph")
    public String windSpeedKph;
    @SerializedName("wind_mph")
    public String windSpeedMph;

    @SerializedName("dewpoint_c")
    public String dewPointCelsius;
    @SerializedName("dewpoint_f")
    public String dewPointFarengate;

    @SerializedName("icon_url")
    public String imageUrl;
}