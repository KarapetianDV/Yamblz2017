
package ru.overtired.yamblz2017.data.forecastApi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Simpleforecast {

    @SerializedName("forecastday")
    @Expose
    private List<ForecastDay> forecastday = null;

    public List<ForecastDay> getForecastday() {
        return forecastday;
    }

    public void setForecastday(List<ForecastDay> forecastday) {
        this.forecastday = forecastday;
    }

}
