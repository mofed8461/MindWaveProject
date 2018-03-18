package com.oscarsalguero.eegstarter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by mamounlaptop on 2/6/18.
 */

public class PECSActivity  extends AppCompatActivity {

    public static PECSActivity currentPECSActivity = null;

    public static void StopPECSActivity()
    {
        if (currentPECSActivity != null)
        {
            currentPECSActivity.finish();
            currentPECSActivity = null;
        }

        if (PECSFoodActivity.currentPECSFoodActivity != null)
        {
            PECSFoodActivity.currentPECSFoodActivity.finish();
            PECSFoodActivity.currentPECSFoodActivity = null;
        }

        if (PECSDrinkActivity.currentPECSDrinkActivity != null)
        {
            PECSDrinkActivity.currentPECSDrinkActivity.finish();
            PECSDrinkActivity.currentPECSDrinkActivity = null;
        }

        if (PECSBathActivity.currentPECSBathActivity != null)
        {
            PECSBathActivity.currentPECSBathActivity.finish();
            PECSBathActivity.currentPECSBathActivity = null;
        }

        if (PECSPlayActivity.currentPECSPlayActivity != null)
        {
            PECSPlayActivity.currentPECSPlayActivity.finish();
            PECSPlayActivity.currentPECSPlayActivity = null;
        }

        if (PECSExitActivity.currentPECSExitActivity != null)
        {
            PECSExitActivity.currentPECSExitActivity.finish();
            PECSExitActivity.currentPECSExitActivity = null;
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

        if (PECSCallActivity.currentPECSCallActivity != null)
        {
            PECSCallActivity.currentPECSCallActivity.finish();
            PECSCallActivity.currentPECSCallActivity = null;
        }

    }

    public static void playSound(Activity act, int rawFileID)
    {
        MediaPlayer mp = MediaPlayer.create(act, rawFileID);
        mp.start();
    }

    ImageButton[] buttons = new ImageButton[8];
    int[] originalBG = new int[8];
    int[] selectedBG = new int[8];
    boolean[] state = new boolean[8];

    void clearSelection()
    {
        for (int i = 0;i < buttons.length; ++i)
        {
            buttons[i].setBackgroundResource(originalBG[i]);
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pecs);

        currentPECSActivity = this;

        buttons[0] = (ImageButton) findViewById(R.id.play);
        buttons[1] = (ImageButton) findViewById(R.id.sleep);
        buttons[2] = (ImageButton) findViewById(R.id.drink);
        buttons[3] = (ImageButton) findViewById(R.id.eat);
        buttons[4] = (ImageButton) findViewById(R.id.bathroom);
        buttons[5] = (ImageButton) findViewById(R.id.exit);
        buttons[6] = (ImageButton) findViewById(R.id.emotions);
        buttons[7] = (ImageButton) findViewById(R.id.call);


        originalBG[0] = R.drawable.play;
        originalBG[1] = R.drawable.sleep;
        originalBG[2] = R.drawable.drink;
        originalBG[3] = R.drawable.eat;
        originalBG[4] = R.drawable.bathroom;
        originalBG[5] = R.drawable.door_exit;
        originalBG[6] = R.drawable.happy;
        originalBG[7] = R.drawable.call;


        selectedBG[0] = R.drawable.play_selected;
        selectedBG[1] = R.drawable.sleep_selected;
        selectedBG[2] = R.drawable.drink_selected;
        selectedBG[3] = R.drawable.eat_selected;
        selectedBG[4] = R.drawable.bathroom_selected;
        selectedBG[5] = R.drawable.door_exit_selected;
        selectedBG[6] = R.drawable.happy_selected;
        selectedBG[7] = R.drawable.call;


        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PECSActivity.this, PECSPlayActivity.class));
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(PECSActivity.this, R.raw.want_to_sleep);
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PECSActivity.this, PECSDrinkActivity.class));
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PECSActivity.this, PECSFoodActivity.class));
            }
        });

        buttons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PECSActivity.this, PECSBathActivity.class));
            }
        });

        buttons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PECSActivity.this, PECSExitActivity.class));
            }
        });

        buttons[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PECSActivity.this, PECSEmotionsActivity.class));
            }
        });

        buttons[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PECSActivity.this, PECSCallActivity.class));
            }
        });


//        for (int i = 0; i < 6; ++i)
//        {
//            state[i] = false;
//            final int icopy = i;
//            buttons[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (state[icopy] == true)
//                    {
//                        buttons[icopy].setBackgroundResource(originalBG[icopy]);
//                        state[icopy] = false;
//                    }
//                    else
//                    {
//                        clearSelection();
//                        buttons[icopy].setBackgroundResource(selectedBG[icopy]);
//                        state[icopy] = true;
//                    }
//                }
//            });
//        }



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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
