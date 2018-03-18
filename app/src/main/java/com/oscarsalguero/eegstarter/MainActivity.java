/***
 * Copyright (c) 2015 Oscar Salguero www.oscarsalguero.com
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oscarsalguero.eegstarter;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGRawMulti;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Main Activity
 * <p/>
 * Connects to a MindWave EEG device via bluetooth and starts listening for attention, meditation, heart rate and blink readings.
 * Shows raw data only when the rawEnabled flag is set to true.
 *
 * @author RacZo
 */
public class MainActivity extends AppCompatActivity {

    public static int minAttention = 30;
    public static int maxAttention = 80;
    public static int minMeditation = 30;
    public static int maxMeditation = 80;

    boolean isHighNotified = false;
    boolean isHighNotifiedM = false;
    boolean isLowNotified = false;
    boolean isLowNotifiedM = false;

    private static final int REQUEST_ENABLE_BT = 100;
    private BluetoothAdapter bluetoothAdapter;
    private TextView textViewAttention;
    private TextView textViewMeditation;
    private  TextView textViewConnectionStrength;

    private NotificationManager mNotificationManager;


    private TGDevice tgDevice;
    private final boolean rawEnabled = true; //Shows raw data in textViewRawData
    private static final String LOG_TAG = MainActivity.class.getName();


    int avgCount = 100;
    int currentAttentionIndex = 0;
    int[] attentionValues = new int[avgCount];
    float avgAttention = 0;
    int currentMeditationIndex = 0;
    int[] meditationValues = new int[avgCount];
    float avgMeditation = 0;


    float getAverage(int[] array)
    {
        float avg = 0.0f;
        for (int i = 0; i < array.length; ++i)
        {
            avg += array[i];
        }

        return avg / array.length;
    }

    void switchToSettings()
    {
        startActivity(new Intent(this, valuesActivity.class));
    }

    void switchToBubbles()
    {
        bubblesActivity.StopBubblesActivity();

        musicActivity.StopMusicActivity();

        PECSActivity.StopPECSActivity();

        startActivity(new Intent(this, bubblesActivity.class));
    }

    void switchToMusic()
    {
        bubblesActivity.StopBubblesActivity();

        musicActivity.StopMusicActivity();

        PECSActivity.StopPECSActivity();

        startActivity(new Intent(this, musicActivity.class));
    }

    void switchToSelection()
    {
        bubblesActivity.StopBubblesActivity();

        musicActivity.StopMusicActivity();

        PECSActivity.StopPECSActivity();

        startActivity(new Intent(this, PECSActivity.class));
    }


    long lastMsgTime = new Date().getTime();

    public void sendSms(String num, String msg){
        try
        {
            if (num.equals("") || num.equals("0000")) {

                Toast.makeText(this, getString(R.string.no_num), Toast.LENGTH_LONG).show();

                return;
            }

            long currentTime = new Date().getTime();

            if (currentTime - lastMsgTime < 20000)
                return;

            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> parts = sms.divideMessage(msg);
            sms.sendMultipartTextMessage(num, null, parts, null,null);

            lastMsgTime = currentTime;
        }
        catch (SecurityException ex)
        {
            Toast.makeText(this, getString(R.string.no_sms_permission), Toast.LENGTH_LONG).show();
        }
    }

    public static void call(Activity act, String num)
    {
        try {
            if (num.equals("") || num.equals("0000")) {

                Toast.makeText(act, act.getString(R.string.no_num), Toast.LENGTH_LONG).show();

                return;
            }

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
            act.startActivity(intent);
        }
        catch (SecurityException ex)
        {
            Toast.makeText(act, act.getString(R.string.no_call_permission), Toast.LENGTH_LONG).show();
        }
    }




    static String currentLang = "";
    public static void changeLanguage(Activity act, String lang)
    {
        if (currentLang != lang) {
            currentLang = lang;
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            act.getBaseContext().getResources().updateConfiguration(config, act.getBaseContext().getResources().getDisplayMetrics());

            Intent intent = act.getIntent();
            act.finish();
            act.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        {
            SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
            String lang = sharedPref.getString("lang", "en");

            changeLanguage(this, lang);
        }

        setContentView(R.layout.activity_main);



        avgAttention = (minAttention + maxAttention) / 2;
        avgMeditation = (minMeditation + maxMeditation) / 2;
        for (int i = 0; i < attentionValues.length; ++i)
        {
            meditationValues[i] = (int)avgMeditation;
            attentionValues[i] = (int)avgAttention;
        }


        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        final Button settingsButton = (Button)findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToSettings();
            }
        });

        final Button selectionButton = (Button)findViewById(R.id.SelectionGame);

        selectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToSelection();
            }
        });

        final Button musicButton = (Button)findViewById(R.id.musicButton);

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToMusic();
            }
        });

        final Button bubbleGameButton = (Button) findViewById(R.id.BubblesGame);

        bubbleGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToBubbles();
            }
        });




        textViewConnectionStrength = (TextView) findViewById(R.id.connectionStrength);
        textViewAttention = (TextView) findViewById(R.id.text_view_attention);
        textViewMeditation = (TextView) findViewById(R.id.text_view_meditation);
//        textViewHeartRate = (TextView) findViewById(R.id.text_view_heart_rate);
//        textViewBlink = (TextView) findViewById(R.id.text_view_blink);
//        textViewRawData = (TextView) findViewById(R.id.text_view_raw_data);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, getString(R.string.bluetooth_not_available), Toast.LENGTH_LONG).show();
//            finish();
//            return;
        } else {
            /* Creates the TGDevice */
            tgDevice = new TGDevice(bluetoothAdapter, new Handler() {

                @Override
                public void handleMessage(Message message) {
                    switch (message.what) {
                        case TGDevice.MSG_STATE_CHANGE:

                            switch (message.arg1) {
                                case TGDevice.STATE_IDLE:
                                    break;
                                case TGDevice.STATE_CONNECTING:
                                    Log.d(LOG_TAG, "Connecting...");
                                    break;
                                case TGDevice.STATE_CONNECTED:
                                    Log.d(LOG_TAG, "Connected!");
                                    tgDevice.start();
                                    break;
                                case TGDevice.STATE_NOT_FOUND:
                                    Log.e(LOG_TAG, "Device not found");
                                    break;
                                case TGDevice.STATE_NOT_PAIRED:
                                    Log.w(LOG_TAG, "Device not paired");
                                    break;
                                case TGDevice.STATE_DISCONNECTED:
                                    Log.d(LOG_TAG, "Device disconnected.");
                            }

                            break;
                        case TGDevice.MSG_POOR_SIGNAL:
                            if (message.arg1 > 0) {

                                textViewConnectionStrength.setText(R.string.label_strength);
                                float val = message.arg1;
                                val -= 25;
                                val /= 175;
                                val = 1.0f - val;
                                val *= 100.0f;

                                textViewConnectionStrength.setText(textViewConnectionStrength.getText() + String.valueOf((int)val));

                                Log.w(LOG_TAG, "Poor signal: " + message.arg1);
                            }
                            break;
                        case TGDevice.MSG_RAW_DATA:
                            if (rawEnabled) {
                                updateRawData(message.arg1);
                            }
                            break;
//                        case TGDevice.MSG_HEART_RATE:
//                            updateHeartRate(message.arg1); // Never updates (may depend on the device model)
//                            break;
                        case TGDevice.MSG_ATTENTION:
                            updateAttention(message.arg1);
                            break;
                        case TGDevice.MSG_MEDITATION:
                            updateMeditation(message.arg1);
                            break;
                        case TGDevice.MSG_BLINK:
//                            updateBlink(message.arg1);
                            break;
                        case TGDevice.MSG_RAW_COUNT:
                            //Log.i(LOG_TAG, "Raw count: " + message.arg1);
                            break;
                        case TGDevice.MSG_LOW_BATTERY:
                            showLowBatteryToast();
                            break;
                        case TGDevice.MSG_RAW_MULTI:
                            TGRawMulti tgRawMulti = (TGRawMulti) message.obj;
                            Log.i(LOG_TAG, "Raw channel 1: " + tgRawMulti.ch1);
                            Log.i(LOG_TAG, "Raw channel 2: " + tgRawMulti.ch2);
                            Log.i(LOG_TAG, "Raw channel 3: " + tgRawMulti.ch3);
                            Log.i(LOG_TAG, "Raw channel 4: " + tgRawMulti.ch4);
                            Log.i(LOG_TAG, "Raw channel 5: " + tgRawMulti.ch5);
                            Log.i(LOG_TAG, "Raw channel 6: " + tgRawMulti.ch6);
                            Log.i(LOG_TAG, "Raw channel 7: " + tgRawMulti.ch7);
                            Log.i(LOG_TAG, "Raw channel 8: " + tgRawMulti.ch8);
                        default:
                            break;
                    }
                }
            });

            connect();
        }

        {
            SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
            minAttention = sharedPref.getInt("amin", 30);
            maxAttention = sharedPref.getInt("amax", 80);
            minMeditation = sharedPref.getInt("mmin", 30);
            maxMeditation = sharedPref.getInt("mmax", 80);
        }



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tgDevice != null)
            checkForBluetooth();
    }

    @Override
    public void onDestroy() {
        if (tgDevice != null)
            tgDevice.close();

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_connect:
                connect();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    boolean connect() {
        if (tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED) {
            tgDevice.connect(rawEnabled);
            return tgDevice.getState() != TGDevice.STATE_CONNECTED;
        }

        return false;
    }

    private void checkForBluetooth() {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }


    private void updateAttention(int attentionValue) {
//        textViewAttention.setText(String.valueOf(attentionValue));
        textViewAttention.setText(String.valueOf(attentionValue) + ", " + String.valueOf(minAttention) + " < " + String.valueOf(avgAttention) + " < " + String.valueOf(maxAttention));


        attentionValues[currentAttentionIndex] = attentionValue;
        currentAttentionIndex++;
        if (currentAttentionIndex >= attentionValues.length)
        {
            currentAttentionIndex = 0;
        }

        avgAttention = getAverage(attentionValues);

        if (!isHighNotified && avgAttention > maxAttention && avgMeditation > maxMeditation)
        {
            isHighNotified = true;
            isLowNotified = false;

            String msg = getString(R.string.high_concentration_msg);
            sendNotification(msg);

            {
                SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                int notifyBy = sharedPref.getInt("notifyBy", 0);
                String phone = sharedPref.getString("phone", "0000");

                if (!phone.equals("0000")) {
                    if (notifyBy == 0) // sms
                    {
                        sendSms(phone, msg);
                    } else {
                        call(this, phone);
                    }
                }
            }


            switchToSelection();
            return;
        }

        if (!isLowNotified && avgAttention < minAttention && avgMeditation < minMeditation)
        {
            isLowNotified = true;
            isHighNotified = false;

            String msg = getString(R.string.low_concentration_msg);

            sendNotification(msg);

            {
                SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                int notifyBy = sharedPref.getInt("notifyBy", 0);
                String phone = sharedPref.getString("phone", "0000");

                if (!phone.equals("0000"))
                {
                    if (notifyBy == 0) // sms
                    {
                        sendSms(phone, msg);
                    } else {
                        call(this, phone);
                    }
                }
            }


            SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
            int method = sharedPref.getInt("method", 0);

            if (method == 0)
            {
                switchToMusic();
            }
            else if (method == 1)
            {
                switchToBubbles();
            }
            else
            {
                if (new Random().nextFloat() > 0.5)
                {
                    switchToMusic();
                }
                else
                {
                    switchToBubbles();
                }
            }
            return;
        }

    }

    private void updateMeditation(int meditationValue) {
        textViewMeditation.setText(String.valueOf(meditationValue) + ", " + String.valueOf(minMeditation) + " < " + String.valueOf(avgMeditation) + " < " + String.valueOf(maxMeditation));
//        textViewMeditation.setText(String.valueOf(meditationValue));


        meditationValues[currentMeditationIndex] = meditationValue;
        currentMeditationIndex++;
        if (currentMeditationIndex >= meditationValues.length)
        {
            currentMeditationIndex = 0;
        }

        avgMeditation = getAverage(meditationValues);



    }

    private void updateHeartRate(int heartRateValue) {
//        textViewHeartRate.setText(String.valueOf(heartRateValue));
    }

    private void updateRawData(int rawData) {
//        textViewRawData.setText(String.valueOf(rawData));
    }

    private void showLowBatteryToast() {
        Toast.makeText(this, getString(R.string.device_low_battery), Toast.LENGTH_LONG).show();
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK),
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("");
        // Set Big View style to see full content of the message
        NotificationCompat.BigTextStyle inboxStyle =
                new NotificationCompat.BigTextStyle();

        inboxStyle.setBuilder(mBuilder);
        inboxStyle.bigText("");
        inboxStyle.setBigContentTitle("");
        inboxStyle.setSummaryText("");

        // Moves the big view style object into the notification object.
        mBuilder.setStyle(inboxStyle);
        mBuilder.setContentText(msg);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setContentIntent(contentIntent);
        final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};
        mBuilder.setVibrate(DEFAULT_VIBRATE_PATTERN);
        mBuilder.setLights(Color.WHITE, 2000, 3000);
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        mNotificationManager.notify(12945, mBuilder.build());
    }


}
