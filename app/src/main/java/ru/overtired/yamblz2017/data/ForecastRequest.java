package ru.overtired.yamblz2017.data;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.overtired.yamblz2017.data.forecastApi.ForecastApi;

public interface ForecastRequest {
    @GET("api/{apiKey}/forecast10day/lang:{lang}/q/RU/{city}.json")
    Observable<ForecastApi> getForecast(@Path("apiKey") String apiKey, @Path("lang") String lang, @Path("city") String city);
}
