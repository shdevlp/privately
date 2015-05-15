package ru.panoptico.privately;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by shved on 11/23/14.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
