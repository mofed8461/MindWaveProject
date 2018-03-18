package com.oscarsalguero.eegstarter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import static com.oscarsalguero.eegstarter.PECSActivity.playSound;

/**
 * Created by mamounlaptop on 2/6/18.
 */

public class PECSPainActivity extends AppCompatActivity {

    public static PECSPainActivity currentPECSPainActivity = null;

    public static boolean backToBubbles = false;
    ImageButton[] buttons = new ImageButton[7];
    int[] originalBG = new int[7];
    int[] selectedBG = new int[7];
    boolean[] state = new boolean[7];

    void clearSelection()
    {
        for (int i = 0;i < buttons.length; ++i)
        {
            buttons[i].setBackgroundResource(originalBG[i]);
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pecs_pain);

        currentPECSPainActivity = this;

        buttons[0] = (ImageButton) findViewById(R.id.body1);
        buttons[1] = (ImageButton) findViewById(R.id.body2);
        buttons[2] = (ImageButton) findViewById(R.id.body3);
        buttons[3] = (ImageButton) findViewById(R.id.body4);
        buttons[4] = (ImageButton) findViewById(R.id.body5);
        buttons[5] = (ImageButton) findViewById(R.id.body6);
        buttons[6] = (ImageButton) findViewById(R.id.body7);

        originalBG[0] = R.drawable.body1;
        originalBG[1] = R.drawable.body2;
        originalBG[2] = R.drawable.body3;
        originalBG[3] = R.drawable.body4;
        originalBG[4] = R.drawable.body5;
        originalBG[5] = R.drawable.body6;
        originalBG[6] = R.drawable.body7;


        selectedBG[0] = R.drawable.body1_selected;
        selectedBG[1] = R.drawable.body2_selected;
        selectedBG[2] = R.drawable.body3_selected;
        selectedBG[3] = R.drawable.body4_selected;
        selectedBG[4] = R.drawable.body5_selected;
        selectedBG[5] = R.drawable.body6_selected;
        selectedBG[6] = R.drawable.body7_selected;


        for (int i = 0; i < 7; ++i)
        {
            state[i] = false;
            final int icopy = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (state[icopy] == true)
                    {
                        buttons[icopy].setBackgroundResource(originalBG[icopy]);
                        state[icopy] = false;
                    }
                    else
                    {
                        clearSelection();
                        buttons[icopy].setBackgroundResource(selectedBG[icopy]);
                        state[icopy] = true;
                    }
                }
            });
        }



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


        playSound(this, R.raw.feeling_pain);

    }





    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (backToBubbles) {
            startActivity(new Intent(PECSPainActivity.this, bubblesActivity.class));
            backToBubbles = false;
        }
    }
}
