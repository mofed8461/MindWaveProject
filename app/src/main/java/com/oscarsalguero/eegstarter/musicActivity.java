package com.oscarsalguero.eegstarter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class musicActivity extends AppCompatActivity {


    public  static musicActivity currentMusicActivity = null;

    public static void StopMusicActivity()
    {
        if (currentMusicActivity != null)
        {
            currentMusicActivity.finish();
            currentMusicActivity = null;
        }

        if (PECSEmotionsActivity.currentPECSEmotionsActivity != null)
        {
            PECSEmotionsActivity.currentPECSEmotionsActivity.finish();
            PECSEmotionsActivity.currentPECSEmotionsActivity = null;
        }

        if (PECSPainActivity.currentPECSPainActivity != null)
        {
            PECSPainActivity.currentPECSPainActivity.finish();
            PECSPainActivity.currentPECSPainActivity = null;
        }
    }

    MediaPlayer[] tunes;
    int usedMusic;

    RadioButton[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        currentMusicActivity = this;

        tunes = new MediaPlayer[2];

        tunes[0] = MediaPlayer.create(this, R.raw.meditation1);
        tunes[1] = MediaPlayer.create(this, R.raw.meditation2);

        for (int i = 0; i < tunes.length; ++i)
        {
            tunes[i].setLooping(true);
        }

        {
            SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
            usedMusic = sharedPref.getInt("music", 0);
        }

        buttons = new RadioButton[] { (RadioButton)findViewById(R.id.tune1_radio), (RadioButton)findViewById(R.id.tune2_radio) };

        buttons[usedMusic].setChecked(true);

        for (int i = 0; i < buttons.length; ++i)
        {
            final int icopy = i;
            buttons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

                        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("music", icopy);

                        editor.commit();

                        for (int j = 0; j < tunes.length;++j)
                        {
                            if (tunes[j].isPlaying())
                            {
                                tunes[j].pause();
                            }
                        }
                        tunes[icopy].start();
                    }
                }
            });
        }

        tunes[usedMusic].start();


        Button pausePlay = (Button)findViewById(R.id.stop_play_button);
        pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tunes[usedMusic].isPlaying())
                {
                    tunes[usedMusic].pause();
                }
                else
                {
                    tunes[usedMusic].start();
                }
            }
        });

        ImageButton emotions = (ImageButton) findViewById(R.id.emotions);
        ImageButton pain = (ImageButton) findViewById(R.id.pain);

        emotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(musicActivity.this, PECSEmotionsActivity.class));

            }
        });

        pain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(musicActivity.this, PECSPainActivity.class));
            }
        });


        ((Button)findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        for (int i = 0; i < tunes.length; ++i)
        {
            if (tunes[i].isPlaying())
                tunes[i].pause();
        }
    }
}
