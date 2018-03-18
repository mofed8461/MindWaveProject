package com.oscarsalguero.eegstarter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import static com.oscarsalguero.eegstarter.MainActivity.call;
import static com.oscarsalguero.eegstarter.PECSActivity.playSound;

/**
 * Created by mamounlaptop on 2/6/18.
 */

public class PECSCallActivity extends AppCompatActivity {

    public static PECSCallActivity currentPECSCallActivity = null;

    ImageButton[] buttons = new ImageButton[3];




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pecs_call);

        currentPECSCallActivity = this;

        buttons[0] = (ImageButton) findViewById(R.id.call_father);
        buttons[1] = (ImageButton) findViewById(R.id.call_mother);
        buttons[2] = (ImageButton) findViewById(R.id.call_trainer);

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                String num = sharedPref.getString("mother_phone", "0000");

                call(PECSCallActivity.this, num);

            }
        });


        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                String num = sharedPref.getString("father_phone", "0000");

                call(PECSCallActivity.this, num);
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                String num = sharedPref.getString("trainer_phone", "0000");
                call(PECSCallActivity.this, num);

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


        playSound(this, R.raw.want_to_call);

    }





    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
