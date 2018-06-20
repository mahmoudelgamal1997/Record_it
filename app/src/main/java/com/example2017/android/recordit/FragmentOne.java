package com.example2017.android.recordit;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.content.Intent;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOne extends Fragment {

    Button buttonRecord,buttonPlay,buttonStop,buttonStopPlayingRecord;
    MediaRecorder mediaRecorder;
    String AudioSavePathInDevice=null;
    public static final int RequestPermissionCode=1;
    MediaPlayer mediaPlayer;
    ArrayList<String> audiofilesDirectory=new ArrayList<>();


    public FragmentOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_one,null);

        buttonRecord=(Button)view.findViewById(R.id.button_record);
        buttonPlay=(Button)view.findViewById(R.id.button_play);
        buttonStop=(Button)view.findViewById(R.id.button_stop);
        buttonStopPlayingRecord=(Button)view.findViewById(R.id.button_stop_PlayingRecord);



        //to Ask for permisssion
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},
                1);





        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (checkPermission()) {


                        //to arrive to external path like whatsapp or any image app
                        AudioSavePathInDevice = SavingFiles() + "/" +
                                CreatDateAudioName() + "AudioRecording.3gp";


                        // start record
                        mediaRecorderReady();

                        try {

                            mediaRecorder.prepare();
                            mediaRecorder.start();

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                        }

                        buttonRecord.setEnabled(false);
                        buttonPlay.setEnabled(false);
                        buttonStop.setEnabled(true);

                        Toast.makeText(getActivity(), "Recording started",
                                Toast.LENGTH_LONG).show();


                        buttonRecord.setEnabled(false);
                        buttonPlay.setEnabled(false);
                        buttonStop.setEnabled(true);

                    }else{
                        requestPermission();
                    }

            }
        });


        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                      try {
                          mediaRecorder.stop();
                          buttonPlay.setEnabled(true);
                          buttonStop.setEnabled(false);
                          buttonRecord.setEnabled(true);
                      }catch (Exception e){
                       Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG).show();

                      }
                Toast.makeText(getActivity(), "Recording Completed",Toast.LENGTH_LONG).show();




            }
        });


        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (checkPermission()){


                // to play audio
                mediaPlayer=new MediaPlayer();


                buttonRecord.setEnabled(false);

                try {
                    // give me  path of your audio
                    mediaPlayer.setDataSource(AudioSavePathInDevice);

                    mediaPlayer.prepare();
                    // Toast.makeText(getApplicationContext(),String.valueOf(mediaPlayer.getDuration()),Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }else {
                requestPermission();

                }
                }
        });

        //to stop audio which play now
        buttonStopPlayingRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                buttonRecord.setEnabled(true);
                buttonStop.setEnabled(true);



            }
        });





        return view;
    }

// function to start record
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
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);
        final int sec = calendar.get(Calendar.SECOND);

        String CollectionDate=""+day+"-"+(month+1)+"-"+year+"-"+hour+"-"+min+"-"+sec;

        return CollectionDate;
    }


    // to save file
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


    // Request permission
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
                    Toast.makeText(getContext(), "Permission applied to read your External storage", Toast.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }}



    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }





    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onStart() {
        super.onStart();


        requestPermission();


    }
}
