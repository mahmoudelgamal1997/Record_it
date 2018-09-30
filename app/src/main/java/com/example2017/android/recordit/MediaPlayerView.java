package com.example2017.android.recordit;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.io.IOException;
import java.util.Locale;

public class MediaPlayerView extends AppCompatActivity {

    /*
    *Activity to play audio


    */

    private ImageButton butStart,butPause;
    private TextView currenttime,totaltime;
    private SeekBar seekBar;
    private SharedPreferences sh;
    android.media.MediaPlayer mediaPlayer;
    int fprogress;
    PulseView pulseView;
    int seconds =0;
    private Handler handler;
    private boolean Running=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

    butStart =(ImageButton) findViewById(R.id.button_start_media);
    butPause=(ImageButton) findViewById(R.id.button_pause_media);
    pulseView=(PulseView)findViewById(R.id.pv);
    seekBar=(SeekBar)findViewById(R.id.seekBar);
    currenttime=(TextView)findViewById(R.id.textView2);
    totaltime=(TextView)findViewById(R.id.textView3);

        handler=new Handler(getApplicationContext().getMainLooper());

        butPause.setVisibility(View.INVISIBLE);
    // to recieve audio data which you click on
    sh=getSharedPreferences("PLZ", Context.MODE_PRIVATE );

    String source = (sh.getString( "data","emputy" ) );

    mediaPlayer=new android.media.MediaPlayer();
        try {
            mediaPlayer.setDataSource(source);
            mediaPlayer.prepare();

            totaltime.setText(organizeTime(mediaPlayer.getDuration()));
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

              /*

              //to start thread
              Running=true;
              new Thread(new Runnable() {
                  @Override
                  public void run() {

                      while(Running){
                          try {
                              Thread.sleep(1000);
                              seconds++;
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }

                      handler.post(new Runnable() {
                          @Override
                          public void run() {
                         currenttime.setText(updateTimer(seconds));

                          }
                      });

                      }
                  }
              }).start();


*/









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

                currenttime.setText(organizeTime(i));
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







    public  String organizeTime(double num){
        int sec,min=0;

        sec=(int)num/(1000);
        min=sec/60;
        sec=sec-min*60;
        return updateTimer(sec);
    }

    private String updateTimer(int second) {
        int minute=0;
        int hour=0;
        if (second > 59) {
            minute++;
            second = 0;
        }

        if (minute > 59) {
            minute = second = 0;
            hour++;
            return String.format(Locale.US, "%02d", hour) + ":" + String.format(Locale.US, "%02d", minute) +
                    ":" + String.format(Locale.US, "%02d", second);
        }

        return String.format(Locale.US, "%02d", minute) + ":" + String.format(Locale.US, "%02d", second);


    }
}
