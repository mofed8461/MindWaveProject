package com.oscarsalguero.eegstarter;

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

public class PECSBathActivity extends AppCompatActivity {

    public static PECSBathActivity currentPECSBathActivity = null;

    ImageButton[] buttons = new ImageButton[2];
    int[] originalBG = new int[2];
    int[] selectedBG = new int[2];
    boolean[] state = new boolean[2];

    void clearSelection()
    {
        for (int i = 0;i < buttons.length; ++i)
        {
            buttons[i].setBackgroundResource(originalBG[i]);
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pecs_bath);

        currentPECSBathActivity = this;

        buttons[0] = (ImageButton) findViewById(R.id.bathroom);
        buttons[1] = (ImageButton) findViewById(R.id.bath);

        originalBG[0] = R.drawable.bathroom;
        originalBG[1] = R.drawable.bath;


        selectedBG[0] = R.drawable.bathroom_selected;
        selectedBG[1] = R.drawable.bath_selected;


        for (int i = 0; i < 2; ++i)
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


        playSound(this, R.raw.want_to_go_bathroom);

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
