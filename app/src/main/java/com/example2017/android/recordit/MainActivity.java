package com.example2017.android.recordit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button buttonRecord,buttonPlay,buttonStop,buttonStopPlayingRecord;
    MediaRecorder mediaRecorder;
    String AudioSavePathInDevice=null;
    Random random;
    String Alph="abcdefghijklmnopqestuvwxyz";
    int numberOfrandomFile=5;
    public static final int RequestPermissionCode=1;
    MediaPlayer mediaPlayer;
    ArrayList<String> audiofilesDirectory=new ArrayList<>();
    int index =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRecord=(Button)findViewById(R.id.button_record);
        buttonPlay=(Button)findViewById(R.id.button_play);
        buttonStop=(Button)findViewById(R.id.button_stop);
        buttonStopPlayingRecord=(Button)findViewById(R.id.button_stop_PlayingRecord);

        random=new Random();


        //to Ask for permisssion
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);




        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission())
                {


                    AudioSavePathInDevice=SavingFiles()+"/"+
                         CreatDateAudioName()+"AudioRecording.3gp";



                    mediaRecorderReady();

                    try {

                        mediaRecorder.prepare();
                        mediaRecorder.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (IllegalStateException e)
                    {
                    }

                    buttonRecord.setEnabled(false);
                    buttonPlay.setEnabled(false);
                    buttonStop.setEnabled(true);

                    Toast.makeText(MainActivity.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                }else{

                    requestPermission();
                }

                buttonRecord.setEnabled(false);
                buttonPlay.setEnabled(false);
                buttonStop.setEnabled(true);

            }
        });


buttonStop.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        mediaRecorder.stop();
        buttonPlay.setEnabled(true);
        buttonStop.setEnabled(false);
        buttonRecord.setEnabled(true);

        Toast.makeText(MainActivity.this, "Recording Completed",
                Toast.LENGTH_LONG).show();
    }
});


        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer=new MediaPlayer();


                buttonRecord.setEnabled(false);

                try {

                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                index++;
            }
        });

        buttonStopPlayingRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               mediaPlayer.stop();
                buttonRecord.setEnabled(true);
                buttonStop.setEnabled(true);

            }
        });

    }


    public void mediaRecorderReady()
    {

        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }


    //to creat filename by his data
    public String CreatDateAudioName()
    {

        Calendar calendar=Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        String CollectionDate=""+day+"-"+(month+1)+"-"+year;

        return CollectionDate;
    }



    public String SavingFiles()
    {
        //Creating File
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "AudioRecord");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //return file Path
        return directory.getAbsolutePath();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();


    }






    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    finish();
                    System.exit(0);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }







    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }





    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }



}



