package ru.panoptico.privately;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by shved on 11/23/14.
 */
public class SettingsActivity extends Activity {

    public static final String PREF_LOG = "pref_key_log";
    public static final String PREF_PSW = "pref_key_psw";
    public static final String PREF_SRV = "pref_key_srv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.getInstance().setHeaderActionBar(this, "Settings", true);

        // Display the fragment as the main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
