
package ru.overtired.yamblz2017.data.forecastApi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TxtForecast {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("forecastDayText")
    @Expose
    private List<ForecastDayText> forecastDayText = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ForecastDayText> getForecastDayText() {
        return forecastDayText;
    }

    public void setForecastDayText(List<ForecastDayText> forecastDayText) {
        this.forecastDayText = forecastDayText;
    }

}
