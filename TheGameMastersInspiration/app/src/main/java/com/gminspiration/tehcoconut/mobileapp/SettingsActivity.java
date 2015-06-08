package com.gminspiration.tehcoconut.mobileapp;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by tehcoconut on 6/8/15.
 */

// In the name of support for older devices, we have to load up this activity,
// use addPreferencesFromResource() if the device API is 10 or lower
// use Preference Fragment otherwise

public class SettingsActivity extends PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(Build.VERSION.SDK_INT >= 11){
            addPreferencesFromResource(R.xml.preferences);
        }else{
            SettingsFragment frag = new SettingsFragment();
            getFragmentManager().beginTransaction().replace(android.R.id.content, frag).commit();
        }

    }
}
