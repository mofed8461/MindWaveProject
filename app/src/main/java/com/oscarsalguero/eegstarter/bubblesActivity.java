package com.oscarsalguero.eegstarter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import java.util.ArrayList;
import java.util.Random;


public class bubblesActivity extends AppCompatActivity {

    public static bubblesActivity currentBubblesActivity = null;

    public static void StopBubblesActivity()
    {
        if (currentBubblesActivity != null)
        {
            currentBubblesActivity.finish();
            currentBubblesActivity = null;
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

    BubblePicker bubbles;

    MediaPlayer[] bubbleSound;

    RelativeLayout layout;

    ArrayList<PickerItem> items = new ArrayList<PickerItem>();

    MenuItem muteMenuItem;

    boolean muted = false;

    int color = 0;

    void restart()
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubbles);

        layout = (RelativeLayout) findViewById(R.id.relativeLayout);

        currentBubblesActivity = this;



        bubbleSound = new MediaPlayer[10];

        for (int i = 0;i < bubbleSound.length; ++i)
        {
            bubbleSound[i] = MediaPlayer.create(this, R.raw.bubble);
        }
        bubbles = (BubblePicker) findViewById(R.id.bubblesPicker);


        for (int i = 0; i < 10; ++i) {
            PickerItem pi = new PickerItem(" ", Color.BLUE, new BubbleGradient(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF")), 0.2f, Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL), Color.BLACK, getDrawable(R.drawable.bubble), true);

            items.add(pi);

        }
        bubbles.setItems(items);

        bubbles.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(PickerItem pickerItem) {
                items.remove(pickerItem);

                bubbles.setItems(items);

                if (!muted) {
                    int r = (int) (new Random().nextDouble() * 10);

                    bubbleSound[r].start();
                }
            }

            @Override
            public void onBubbleDeselected(PickerItem pickerItem) {
                items.remove(pickerItem);

                if (!muted) {
                    int r = (int) (new Random().nextDouble() * 10);

                    bubbleSound[r].start();
                }

                bubbles.setItems(items);
                if (items.size() == 0)
                {
                    restart();
                }
            }
        });



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bubbles, menu);

        muteMenuItem = menu.findItem(R.id.MuteUnmute);

        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        muted = sharedPref.getBoolean("muted", false);
        color = sharedPref.getInt("color", 0);
        if (muted)
        {
            muteMenuItem.setTitle(R.string.Unmute);
            muteMenuItem.setIcon(R.drawable.unmute);
        }
        else
        {
            muteMenuItem.setTitle(R.string.Mute);
            muteMenuItem.setIcon(R.drawable.mute);
        }

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.feeling_pain:
                finish();
                PECSPainActivity.backToBubbles = true;
                startActivity(new Intent(this, PECSPainActivity.class));
                break;
            case R.id.share_emotions:
                finish();
                PECSEmotionsActivity.backToBubbles = true;
                startActivity(new Intent(this, PECSEmotionsActivity.class));
                break;
            case R.id.MuteUnmute:

                muted = !muted;

                if (muted)
                {
                    item.setTitle(R.string.Unmute);
                    item.setIcon(R.drawable.unmute);
                }
                else
                {
                    item.setTitle(R.string.Mute);
                    item.setIcon(R.drawable.mute);
                }
                {
                    SharedPreferences sharedPref = getSharedPreferences("soundPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("muted", muted);

                    editor.commit();
                }
                break;

            case R.id.ChangeColor:
                {
                    SharedPreferences sharedPref = getSharedPreferences("soundPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    color++;

                    int[] colors = {
                            Color.argb(0x90, 255,0,0),
                            Color.argb(0x90, 0,255,0),
                            Color.argb(0x90, 0,0,255),
                            Color.argb(0x90, 255,255,255),

                    };

                    if (color >= colors.length)
                        color = 0;

                    editor.putInt("color", color);

                    bubbles.setBackgroundColor(colors[color]);
//                    layout.setBackgroundColor(Color.argb(255, 255, 0, 0));


                    editor.commit();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (items.size() != 0)
            bubbles.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (items.size() != 0)
            bubbles.onResume();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        super.onTouchEvent(event);

        return bubbles.onTouchEvent(event);
    }
}
