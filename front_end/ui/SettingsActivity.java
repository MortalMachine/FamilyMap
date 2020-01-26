package ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import client.model.Model;
import jordanrj.familymap.R;
import client.model.Settings;

/**
 * Created by jordanrj on 12/8/18.
 */

public class SettingsActivity extends AppCompatActivity {
    final String[] lineColorOptions = {"Red", "Green", "Blue"};
    private Settings settings;
    private Spinner lifeStorySpinner;
    private Spinner treeSpinner;
    private Spinner spouseSpinner;
    private Switch lifeStorySwitch;
    private Switch treeSwitch;
    private Switch spouseSwitch;

    public SettingsActivity() {
        settings = Model.instance().getSettings();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        wireUpSpinners();
        wireUpSwitches();
    }

    private void wireUpSpinners() {
        lifeStorySpinner = findViewById(R.id.lifeStorySpinner);
        if (lifeStorySpinner != null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lineColorOptions);
            lifeStorySpinner.setAdapter(arrayAdapter);

            settings.setStoryLineColor(lifeStorySpinner.getSelectedItem().toString());

            lifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
/*
                    Toast.makeText(SettingsActivity.this, "Selected " + lineColorOptions[position],
                            Toast.LENGTH_LONG).show();
*/                  settings.setStoryLineColor(lineColorOptions[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        treeSpinner = findViewById(R.id.treeLinesSpinner);
        if (treeSpinner != null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lineColorOptions);
            treeSpinner.setAdapter(arrayAdapter);

            settings.setTreeLineColor(treeSpinner.getSelectedItem().toString());

            treeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
/*
                    Toast.makeText(SettingsActivity.this, "Selected " + lineColorOptions[position],
                            Toast.LENGTH_LONG).show();
*/                  settings.setTreeLineColor(lineColorOptions[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        spouseSpinner = findViewById(R.id.spouseLinesSpinner);
        if (spouseSpinner != null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lineColorOptions);
            spouseSpinner.setAdapter(arrayAdapter);

            settings.setSpouseLineColor(spouseSpinner.getSelectedItem().toString());

            spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
/*
                    Toast.makeText(SettingsActivity.this, "Selected " + lineColorOptions[position],
                            Toast.LENGTH_LONG).show();
*/                  settings.setSpouseLineColor(lineColorOptions[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void wireUpSwitches() {
        lifeStorySwitch = (Switch) findViewById(R.id.lifeStorySwitch);
        lifeStorySwitch.setChecked(settings.getStorySwitchChecked());
        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setStorySwitchChecked(isChecked);
            }
        });

        treeSwitch = (Switch) findViewById(R.id.treeLinesSwitch);
        treeSwitch.setChecked(settings.getTreeSwitchChecked());
        treeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setTreeSwitchChecked(isChecked);
            }
        });

        spouseSwitch = (Switch) findViewById(R.id.spouseLinesSwitch);
        spouseSwitch.setChecked(settings.getSpouseSwitchChecked());
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setSpouseSwitchChecked(isChecked);
            }
        });
    }

/*
    @Override
    public void onDestroy() {
        super.onDestroy();
        boolean spouseSwitchChecked = spouseSwitch.isChecked();
        boolean treeSwitchChecked = treeSwitch.isChecked();
        boolean storySwitchChecked = lifeStorySwitch.isChecked();
        spouseSwitch = null;
        treeSwitch = null;
        lifeStorySwitch = null;
        settings.setSpouseSwitchChecked(spouseSwitchChecked);
        settings.setTreeSwitchChecked(treeSwitchChecked);
        settings.setStorySwitchChecked(storySwitchChecked);
    }
*/
}
