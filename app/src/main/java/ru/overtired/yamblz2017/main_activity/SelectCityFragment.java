package ru.overtired.yamblz2017.main_activity;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.overtired.yamblz2017.R;

public class SelectCityFragment extends DialogFragment {

    public SelectCityFragment() {
        // Required empty public constructor
    }


    public static SelectCityFragment newInstance() {
        return new SelectCityFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_city, container, false);
    }

}
