package ru.overtired.yamblz2017.main_activity;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import ru.overtired.yamblz2017.R;

/**
 * Created by overtired on 09.07.17.
 */

public class PreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
