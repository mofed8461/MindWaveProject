package com.oscarsalguero.eegstarter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class valuesActivity extends AppCompatActivity {

    SeekBar minAttention;
    SeekBar maxAttention;
    SeekBar minMeditation;
    SeekBar maxMeditation;

    TextView minAttentionText;
    TextView maxAttentionText;
    TextView minMeditationText;
    TextView maxMeditationText;

    InputMethodManager inputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_values);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        RadioButton[] rb = new RadioButton[]
        {
            (RadioButton)findViewById(R.id.prefer_music_radio),
            (RadioButton)findViewById(R.id.prefer_bubbles_colors_radio),
            (RadioButton)findViewById(R.id.prefer_random_radio)
        };

        {
            SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
            rb[pref.getInt("method", 0)].setChecked(true);


            String lang = pref.getString("lang", "en");
            RadioButton enRadio, arRadio;
            enRadio = (RadioButton) findViewById(R.id.english_radio);
            arRadio = (RadioButton) findViewById(R.id.arabic_radio);

            if (lang == "en") {
                enRadio.setChecked(true);
            } else {
                arRadio.setChecked(true);
            }


            enRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("lang", "en");

                        editor.commit();

                        MainActivity.changeLanguage(valuesActivity.this, "en");
                    }
                }
            });

            arRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("lang", "ar");

                        editor.commit();

                        MainActivity.changeLanguage(valuesActivity.this, "ar");
                    }
                }
            });
        }

        Button contactSettingsButton = (Button) findViewById(R.id.contact_settings);
        contactSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(valuesActivity.this, contactSettingsActivity.class));
            }
        });

        for (int i = 0; i < rb.length; ++i) {
            final int icopy = i;
            rb[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("method", icopy);

                        editor.commit();
                    }
                }
            });
        }



        minAttention = (SeekBar) findViewById(R.id.minAttention);
        maxAttention = (SeekBar) findViewById(R.id.maxAttention);
        minMeditation = (SeekBar) findViewById(R.id.minMeditation);
        maxMeditation = (SeekBar) findViewById(R.id.maxMeditation);

        minAttentionText = (TextView) findViewById(R.id.text1);
        maxAttentionText = (TextView) findViewById(R.id.text2);
        minMeditationText = (TextView) findViewById(R.id.text3);
        maxMeditationText = (TextView) findViewById(R.id.text4);

        minAttention.setProgress(MainActivity.minAttention);
        maxAttention.setProgress(MainActivity.maxAttention);
        minMeditation.setProgress(MainActivity.minMeditation);
        maxMeditation.setProgress(MainActivity.maxMeditation);

        {
            minAttentionText.setText(R.string.MinAttention);
            minAttentionText.setText(minAttentionText.getText() + new Integer(minAttention.getProgress()).toString());
            maxAttentionText.setText(R.string.MaxAttention);
            maxAttentionText.setText(maxAttentionText.getText() + new Integer(maxAttention.getProgress()).toString());
            minMeditationText.setText(R.string.MinMeditation);
            minMeditationText.setText(minMeditationText.getText() + new Integer(minMeditation.getProgress()).toString());
            maxMeditationText.setText(R.string.MaxMeditation);
            maxMeditationText.setText(maxMeditationText.getText() + new Integer(maxMeditation.getProgress()).toString());
        }

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                minAttentionText.setText(R.string.MinAttention);
                minAttentionText.setText(minAttentionText.getText() + new Integer(minAttention.getProgress()).toString());
                maxAttentionText.setText(R.string.MaxAttention);
                maxAttentionText.setText(maxAttentionText.getText() + new Integer(maxAttention.getProgress()).toString());
                minMeditationText.setText(R.string.MinMeditation);
                minMeditationText.setText(minMeditationText.getText() + new Integer(minMeditation.getProgress()).toString());
                maxMeditationText.setText(R.string.MaxMeditation);
                maxMeditationText.setText(maxMeditationText.getText() + new Integer(maxMeditation.getProgress()).toString());
                MainActivity.minAttention = minAttention.getProgress();
                MainActivity.maxAttention = maxAttention.getProgress();
                MainActivity.minMeditation = minMeditation.getProgress();
                MainActivity.maxMeditation = maxMeditation.getProgress();

                SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("amin", MainActivity.minAttention);
                editor.putInt("amax", MainActivity.maxAttention);
                editor.putInt("mmin", MainActivity.minMeditation);
                editor.putInt("mmax", MainActivity.maxMeditation);

                editor.commit();

            }
        };

        minAttention.setOnSeekBarChangeListener(listener);
        maxAttention.setOnSeekBarChangeListener(listener);
        minMeditation.setOnSeekBarChangeListener(listener);
        maxMeditation.setOnSeekBarChangeListener(listener);

        Button doneButton = (Button) findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
