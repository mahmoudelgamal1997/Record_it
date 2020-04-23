package com.example2017.android.recordit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.library.PulseView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MediaPlayerView extends AppCompatActivity {

    /*
    *Activity to play audio


    */

    private ImageButton butStart,butPause,next,previous;
    private TextView currenttime,totaltime;
    private SeekBar seekBar;
    private SharedPreferences sh;
    android.media.MediaPlayer mediaPlayer;
    int fprogress;
    int postion=0;
    PulseView pulseView;
    File[] arrayList;

    int sec,min=0;
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;
    private Handler handler;
    private boolean Running=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

    butStart =(ImageButton) findViewById(R.id.button_start_media);
    butPause=(ImageButton) findViewById(R.id.button_pause_media);
    next=(ImageButton) findViewById(R.id.button_next_media);
    previous=(ImageButton) findViewById(R.id.button_previous_media);


    pulseView=(PulseView)findViewById(R.id.pv);
    seekBar=(SeekBar)findViewById(R.id.seekBar);
    currenttime=(TextView)findViewById(R.id.textView2);
    totaltime=(TextView)findViewById(R.id.textView3);

        handler=new Handler(getApplicationContext().getMainLooper());

        butPause.setVisibility(View.INVISIBLE);



        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

// Prepare the Interstitial Ad
        interstitial = new InterstitialAd(MediaPlayerView.this);
// Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);
// Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
            }
        });











        arrayList = new File[0];
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "AudioRecord");
        if (directory.isDirectory()){

            arrayList=directory.listFiles();

        }
            postion=getIntent().getIntExtra("postion",0);


        mediaPlayer=new android.media.MediaPlayer();
        try {
            mediaPlayer.setDataSource(arrayList[postion].getAbsolutePath());
            mediaPlayer.prepare();

            totaltime.setText(updateTimer(mediaPlayer.getDuration()));
        } catch (IOException e) {
            e.printStackTrace();
        }


      butStart.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              butPause.setVisibility(View.VISIBLE);
              butStart.setVisibility(View.INVISIBLE);
              mediaPlayer.start();
              pulseView.startPulse();



          }
      });




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                try {
                    mediaPlayer=new android.media.MediaPlayer();
                    if (postion<arrayList.length-1){
                        postion=postion+1;

                    }
                    mediaPlayer.setDataSource(arrayList[postion].getAbsolutePath());
                    mediaPlayer.prepare();
                    seekBar.setProgress(0);
                    seekBar.setMax(mediaPlayer.getDuration());
                    butStart.setVisibility(View.VISIBLE);
                    butPause.setVisibility(View.INVISIBLE);

                    totaltime.setText(updateTimer(mediaPlayer.getDuration()));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                try {
                    mediaPlayer=new android.media.MediaPlayer();
                    if (postion>0){
                        postion=postion-1;

                    }
                    mediaPlayer.setDataSource(arrayList[postion].getAbsolutePath());
                    mediaPlayer.prepare();
                    seekBar.setMax(mediaPlayer.getDuration());
                    seekBar.setProgress(0);
                    butStart.setVisibility(View.VISIBLE);
                    butPause.setVisibility(View.INVISIBLE);
                    totaltime.setText(updateTimer(mediaPlayer.getDuration()));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });




        butPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                butPause.setVisibility(View.INVISIBLE);
                butStart.setVisibility(View.VISIBLE);

                mediaPlayer.pause();
                pulseView.finishPulse();

                Running=false;
            }
        });


        // max value of seekbar
        seekBar.setMax(mediaPlayer.getDuration());


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                fprogress=i;

                currenttime.setText(updateTimer(i));
                if (i==mediaPlayer.getDuration()){
                    pulseView.finishPulse();
                    Running=false;
                }
            }



            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //when touch seekbar end
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(fprogress);
            }
        });


        // to make seekbar move with audio and stop when audio finish
        Mythread mythread=new Mythread();
        mythread.start();
    }





class Mythread extends Thread{
public void run(){
    // if audio isn't finished yet
    while (mediaPlayer!=null){

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {e.printStackTrace();}

        seekBar.post(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        });


    }


}




}








    private String updateTimer(int second) {
        int min=0;
        second=(int)(second/1000);
        min=(int) second/60;
        second=second-min*60;
        System.out.println(second+":"+min);


        return String.format(Locale.US, "%02d", min) + ":" + String.format(Locale.US, "%02d", second);


    }



    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

}
