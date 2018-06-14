package com.example2017.android.recordit;

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
    String root;
    File audioRecorderDircetory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRecord=(Button)findViewById(R.id.button_record);
        buttonPlay=(Button)findViewById(R.id.button_play);
        buttonStop=(Button)findViewById(R.id.button_stop);
        buttonStopPlayingRecord=(Button)findViewById(R.id.button_stop_PlayingRecord);

        random=new Random();

        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission())
                {


                    AudioSavePathInDevice=SavingFiles()+"/"+
                           CreatRandomAudioName(numberOfrandomFile)+"AudioRecording.3gp";

                    /*
                    AudioSavePathInDevice=
                             Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+
                                    CreatRandomAudioName(numberOfrandomFile)+"AudioRecording.3gp";
*/

                    mediaRecorderReady();
                    audiofilesDirectory.add(AudioSavePathInDevice);
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

                    mediaPlayer.setDataSource(audiofilesDirectory.get(index));
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

              //  mediaPlayer.stop();
               // buttonRecord.setEnabled(true);
                //buttonStop.setEnabled(true);
        //    RetriveAudio();

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
    public String CreatRandomAudioName(int stringlength)
    {


        StringBuilder stringBuilder=new StringBuilder(stringlength);

        int i=0;
        while(i<stringlength)
        {
         stringBuilder.append(Alph.charAt(random.nextInt(Alph.length())));

            i++;
        }
        return stringBuilder.toString();
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


    public Object RetriveAudio() {
        Object object = null;
        try {

            FileInputStream fileInputStream = openFileInput("records");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            object = objectInputStream.readObject();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return object;
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();


    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
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



