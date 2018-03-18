package com.oscarsalguero.eegstarter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class contactSettingsActivity extends AppCompatActivity {


    InputMethodManager inputManager;

    EditText phoneNum;
    EditText motherPhone;
    EditText fatherPhone;
    EditText trainerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_settings);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);



        {
            RadioButton smsRadio, phoneRadio;
            smsRadio = (RadioButton) findViewById(R.id.use_sms);
            phoneRadio = (RadioButton) findViewById(R.id.use_phone_call);
            phoneNum = (EditText) findViewById(R.id.phone_num);
            motherPhone = (EditText) findViewById(R.id.mother_phone);
            fatherPhone = (EditText) findViewById(R.id.father_phone);
            trainerPhone = (EditText) findViewById(R.id.trainer_phone);

            SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
            if (pref.getInt("notifyBy", 0) == 0)
            {
                smsRadio.setChecked(true);
            }
            else
            {
                phoneRadio.setChecked(true);
            }

            phoneNum.setText(pref.getString("phone", ""));
            motherPhone.setText(pref.getString("mother_phone", ""));
            fatherPhone.setText(pref.getString("father_phone", ""));
            trainerPhone.setText(pref.getString("trainer_phone", ""));


            smsRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("notifyBy", 0);

                        editor.commit();
                    }
                }
            });

            phoneRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("notifyBy", 1);

                        editor.commit();
                    }
                }
            });

            phoneNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (event == null || !event.isShiftPressed()) {


                            inputManager.hideSoftInputFromWindow(contactSettingsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            return true; // consume.
                        }
                    }
                    return false; // pass on to other listeners.
                }
            });

            motherPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (event == null || !event.isShiftPressed()) {


                            inputManager.hideSoftInputFromWindow(contactSettingsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            return true; // consume.
                        }
                    }
                    return false; // pass on to other listeners.
                }
            });

            fatherPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (event == null || !event.isShiftPressed()) {


                            inputManager.hideSoftInputFromWindow(contactSettingsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            return true; // consume.
                        }
                    }
                    return false; // pass on to other listeners.
                }
            });

            trainerPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (event == null || !event.isShiftPressed()) {



                            inputManager.hideSoftInputFromWindow(contactSettingsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            return true; // consume.
                        }
                    }
                    return false; // pass on to other listeners.
                }
            });
        }

        Button doneButton = (Button) findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                finish();
            }
        });
    }


    @Override
    protected void onStop()
    {
        super.onStop();

        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("phone", phoneNum.getText().toString());

        editor.putString("mother_phone", motherPhone.getText().toString());

        editor.putString("father_phone", fatherPhone.getText().toString());
        editor.putString("trainer_phone", trainerPhone.getText().toString());

        editor.commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
