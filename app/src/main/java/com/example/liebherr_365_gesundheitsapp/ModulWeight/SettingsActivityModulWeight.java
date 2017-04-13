package com.example.liebherr_365_gesundheitsapp.ModulWeight;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.renderscript.Float2;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.liebherr_365_gesundheitsapp.AppCompatPreferenceActivity;
import com.example.liebherr_365_gesundheitsapp.ModulWeight.BmiCalculator;
import com.example.liebherr_365_gesundheitsapp.ModulWeight.ModulWeight;
import com.example.liebherr_365_gesundheitsapp.R;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivityModulWeight extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        EditTextPreference editTextPref = (EditTextPreference) findPreference("height");

        //Set summary text for the user settings
        editTextPref.setSummary(sp.getString("height", null) + " cm");
        editTextPref = (EditTextPreference) findPreference("age");
        editTextPref.setSummary(sp.getString("age", null) + " Jahre");
        editTextPref = (EditTextPreference) findPreference("weightgoal");
        editTextPref.setSummary(sp.getString("weightgoal", null) + " kg");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)`enter code here`
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void OnCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String value = "";

        Preference pref = findPreference(key);
        if (pref instanceof EditTextPreference) {
            EditTextPreference etp = (EditTextPreference) pref;
            switch (etp.getKey()) {
                case "height":
                    value = " cm";
                    break;
                case "age":
                    value = " Jahre";
                    break;
                case "weightgoal":

                    // call function setweighgoaltext
                    ModulWeight.setWeightGoal(Float.parseFloat(etp.getText()));

                    value = " kg";
                    break;
                default:
                    value = "";

            }
            pref.setSummary(etp.getText() + value);
        }
        BmiCalculator.calculateBmi(this);
    }
}