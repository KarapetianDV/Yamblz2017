package ru.overtired.yamblz2017;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by overtired on 09.07.17.
 */

public class ContentFragment extends Fragment {
    public static ContentFragment newInstance() {
        //Bundle args = new Bundle();
        ContentFragment fragment = new ContentFragment();
        //fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity_content,container,false);
        return view;
    }
}
