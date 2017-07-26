
package ru.overtired.yamblz2017.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutoComplete {

    @SerializedName("RESULTS")
    @Expose
    private List<RESULT> rESULTS = null;

    public List<RESULT> getRESULTS() {
        return rESULTS;
    }

    public void setRESULTS(List<RESULT> rESULTS) {
        this.rESULTS = rESULTS;
    }

}
