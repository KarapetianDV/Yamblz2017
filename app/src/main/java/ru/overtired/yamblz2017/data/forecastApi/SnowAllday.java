
package ru.overtired.yamblz2017.data.forecastApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SnowAllday {

    @SerializedName("in")
    @Expose
    private Double in;
    @SerializedName("cm")
    @Expose
    private Double cm;

    public Double getIn() {
        return in;
    }

    public void setIn(Double in) {
        this.in = in;
    }

    public Double getCm() {
        return cm;
    }

    public void setCm(Double cm) {
        this.cm = cm;
    }

}
