package ru.overtired.yamblz2017;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.overtired.yamblz2017.data.forecastApi.ForecastDay;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    Context parentContext;

    private List<ForecastDay> forecastDayList;

    public ForecastAdapter() {
    }

    public ForecastAdapter(List<ForecastDay> forecastDayList) {
        this.forecastDayList = forecastDayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_card, parent, false);
        parentContext = parent.getContext();

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        loadWeatherIcon(parentContext, forecastDayList.get(position).getIconUrl(), holder.cardImage);
        holder.cardTemp.setText(forecastDayList.get(position).getHigh().getCelsius());
        holder.cardWeather.setText(forecastDayList.get(position).getConditions());
        holder.cardFeelsLike.setText(forecastDayList.get(position).getHigh().getCelsius());
        holder.cardHumidity.setText(forecastDayList.get(position).getAvehumidity().toString());
        holder.cardWindspeed.setText(forecastDayList.get(position).getAvewind().getKph().toString());
    }

    @Override
    public int getItemCount() {
        return forecastDayList.size();
    }

    public void loadWeatherIcon(Context context, String iconUrl, ImageView cardImage) {
        Picasso.with(context)
                .load(iconUrl)
                .into(cardImage);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_image)
        ImageView cardImage;

        @BindView(R.id.card_temp)
        TextView cardTemp;

        @BindView(R.id.card_weather)
        TextView cardWeather;

        @BindView(R.id.card_feelslike_temp)
        TextView cardFeelsLike;

        @BindView(R.id.card_humidity_text)
        TextView cardHumidity;

        @BindView(R.id.card_windspeed)
        TextView cardWindspeed;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
