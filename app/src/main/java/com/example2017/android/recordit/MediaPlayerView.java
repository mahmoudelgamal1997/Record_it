package com.example2017.android.recordit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

public class MediaPlayerView extends AppCompatActivity {

    private Button butStart,butPause,butStop;
    private SeekBar seekBar;
    private SharedPreferences sh;
    android.media.MediaPlayer mediaPlayer;
    int fprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

    butStart =(Button)findViewById(R.id.button_start_media);
    butPause=(Button)findViewById(R.id.button_pause_media);
    seekBar=(SeekBar)findViewById(R.id.seekBar);


    sh=getSharedPreferences("PLZ", Context.MODE_PRIVATE );

   String source = (sh.getString( "data","emputy" ) );

    mediaPlayer=new android.media.MediaPlayer();
        try {
            mediaPlayer.setDataSource(source);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


      butStart.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              mediaPlayer.start();

              Toast.makeText(getApplicationContext(), String.valueOf(mediaPlayer.getDuration()), Toast.LENGTH_SHORT).show();
          }
      });


        butPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });



        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                fprogress=i;
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



}
