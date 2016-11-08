package com.example.lanayusuf.remimemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Derrick on 11/3/2016.
 */
public class PreferenceScreen extends PreferenceActivity {

    public static PreferenceScreen this_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this_pref = this;
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment  {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Preference back_button = (Preference) findPreference(getString(R.string.pref_back_button));
            back_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(this_pref, MainActivity.class));
                    return true;
                }
            });


        }
    }
}
