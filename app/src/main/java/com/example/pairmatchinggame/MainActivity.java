package com.example.pairmatchinggame;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int[] boxTapped = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int[] image_id = {  R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,
                        R.drawable.a5,R.drawable.a6,R.drawable.a7,R.drawable.a8,
                        R.drawable.a9,R.drawable.a10,R.drawable.a11,R.drawable.a12,
                        R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,
                        R.drawable.a5,R.drawable.a6,R.drawable.a7,R.drawable.a8,
                        R.drawable.a9,R.drawable.a10,R.drawable.a11,R.drawable.a12};
    List<Integer> img_position = new ArrayList<>();
    boolean gameActive = true;
    int total_tapCounter=0;
    int tap_1;
    int total_attempt = 0;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playMusic(R.raw.background_music);
        additemstoList();
        Collections.shuffle(img_position);
    }
    public static MediaPlayer music;
    public void playMusic(int id)
    {
        music = MediaPlayer.create(MainActivity.this, id);
        music.setLooping(true);
        music.start();
    }
    public void additemstoList()
    {
        for(int i=0;i<24;i++)
            img_position.add(image_id[i]);
    }
    public void tapIn(View v)
    {
        GridLayout grid = findViewById(R.id.grid);
        image = (ImageView) findViewById(v.getId());
        if(gameActive && boxTapped[Integer.parseInt(image.getTag().toString())]==0)
        {
            playTapSound();
            int tappedCounter = Integer.parseInt(image.getTag().toString());
            boxTapped[tappedCounter]=1;
            image.setImageResource(img_position.get(tappedCounter));
            total_tapCounter++;
            if(total_tapCounter % 2 == 0)
            {
                if(img_position.get(tap_1).intValue() != img_position.get(tappedCounter).intValue())
                {
                    gameActive = false;
                    total_tapCounter-=2;
                    boxTapped[tappedCounter]=0;
                    boxTapped[tap_1]=0;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gameActive = true;
                            image = (ImageView) grid.getChildAt(tappedCounter);
                            image.setImageDrawable(null);
                            image = (ImageView) grid.getChildAt(tap_1);
                            image.setImageDrawable(null);
                        }
                    }, 400);
                }
            }
            else
            {
                tap_1 = tappedCounter;
                total_attempt++;
                TextView tv = findViewById(R.id.display);
                tv.setText("Total taps : "+Integer.toString(total_attempt));
            }
            if(total_tapCounter == 24)
            {
                gameActive = false;
                TextView tv = findViewById(R.id.display);
                tv.setText("Great! You have successfully completed this challenge. You can try again to finish it faster.");
            }
        }
    }
    public  void restart(View v)
    {
        playTapSound();
        Collections.shuffle(img_position);
        TextView tv = findViewById(R.id.display);
        tv.setText("");
        int i;
        GridLayout grid = findViewById(R.id.grid);
        for(i=0;i<24;i++)
        {
            boxTapped[i]=0;
            image = (ImageView) grid.getChildAt(i);
            image.setImageDrawable(null);
        }
        total_tapCounter = 0;
        total_attempt = 0;
        gameActive = true;
    }
    MediaPlayer media;
    public void playTapSound()
    {
        media= MediaPlayer.create(MainActivity.this, R.raw.tap);
        media.start();
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
            }
        });
    }
    @Override
    protected void onPause(){
        super.onPause();
        music.pause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        music.start();
    }
}