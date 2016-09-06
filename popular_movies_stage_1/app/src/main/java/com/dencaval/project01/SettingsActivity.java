package com.dencaval.project01;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

/**
 * Created by denis on 21/08/2016.
 */
public class SettingsActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        Preference pref = findPreference(getString(R.string.pref_sorting_key));
        pref.setOnPreferenceChangeListener(this);

//        this.onPreferenceChange(pref, PreferenceManager.getDefaultSharedPreferences(pref.getContext()).getString(pref.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_SHORT).show();
        return true;
    }
}
