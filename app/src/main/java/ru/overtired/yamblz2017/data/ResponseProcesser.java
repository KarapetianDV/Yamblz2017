package ru.overtired.yamblz2017.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Created by overtired on 15.07.17.
 */

public class ResponseProcesser {
    public static Weather getWheatherFromJsonResponse(String jsonResponse){
        JsonParser parser = new JsonParser();

        JsonElement mainPart = parser.parse(jsonResponse)
                .getAsJsonObject()
                .get("current_observation");

        Weather weather = new Gson().fromJson(mainPart,Weather.class);

        weather.city = mainPart.getAsJsonObject()
                .get("display_location")
                .getAsJsonObject()
                .get("city")
                .getAsString();

        return weather;
    }
}
