package ru.overtired.yamblz2017;

import android.app.Application;

import ru.overtired.yamblz2017.data.database.Dao;

/**
 * Created by overtired on 16.07.17.
 */

public class App extends Application {
    private Dao dao;

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao){
        this.dao = dao;
    }
}
