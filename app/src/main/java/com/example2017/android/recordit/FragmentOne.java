package com.example2017.android.recordit;


import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.SequenceInputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.coremedia.iso.boxes.Container;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOne extends Fragment {

    Button buttonRecord, buttonSave, buttonPause;
    MediaRecorder mediaRecorder;
    String AudioSavePathInDevice = null;
    public static final int RequestPermissionCode = 1;
    TextView textView;
    private Handler handler;
    int second = 0, minute = 0, hour = 0;
    boolean recording = false;
    boolean StartRecording = false;
    boolean recordPause = false;
    ArrayList<String> path;
    ObjectAnimator scaleDown;
    NotificationManager manager   ;
    ImageView mic; ;

    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    public FragmentOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, null);


        buttonRecord = (Button) view.findViewById(R.id.button_record);
        buttonSave = (Button) view.findViewById(R.id.button_stop);
        buttonPause = (Button) view.findViewById(R.id.button_pause);
        textView = (TextView) view.findViewById(R.id.textView);
        mic=(ImageView)view.findViewById(R.id.imageView);

        handler = new Handler(getActivity().getMainLooper());

        path = new ArrayList<>();
        buttonPause.setVisibility(View.INVISIBLE);
        //to Ask for permisssion

        requestPermission();


        AdView mAdView = (AdView)view. findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

// Prepare the Interstitial Ad
        interstitial = new InterstitialAd(getActivity());
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


        buttonRecord.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        if (checkPermission()) {
        recording = true;
        StartRecording = true;
        buttonPause.setVisibility(View.VISIBLE);
        buttonRecord.setVisibility(View.INVISIBLE);
        SetAnimation();
        Notification();

        new Thread(new Runnable() {
        @Override
        public void run() {
        //if true
        while (recording) {

        try {
        Thread.sleep(1000);
        second++;
        } catch (InterruptedException e) {
        e.printStackTrace();
        }

        // we use handler because we treat with UI
        handler.post(new Runnable() {
        @Override
        public void run() {

            textView.setText(updateTimer());

        }
        });

        }
        }
        }).start();


        //to arrive to external path like whatsapp or any image app
        AudioSavePathInDevice = SavingFiles() + "/" +
        CreatDateAudioName() + "AudioRecording.3gp";

        mediaRecorderReady();


            Toast.makeText(getContext(), "record starting", Toast.LENGTH_SHORT).show();

        // start record
        new Thread(new Runnable() {
        @Override
        public void run() {
        try {
        mediaRecorder.prepare();
        mediaRecorder.start();
        StartRecording = false;

        } catch (IOException e) {
        e.printStackTrace();
        } catch (IllegalStateException e) {
        }
        }
        }).start();



        } else {
        requestPermission();
        }
        }
        });


                buttonPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                try {
                recordPause = true;
                recording = false;
                buttonPause.setVisibility(View.INVISIBLE);
                buttonRecord.setVisibility(View.VISIBLE);
                scaleDown.cancel();

                mediaRecorder.stop();
                mediaRecorder.reset();
                //to convert path to file
                path.add(AudioSavePathInDevice);

                Toast.makeText(getActivity(), "Recording pause",
                Toast.LENGTH_SHORT).show();




                } catch (Exception e) {


                }
                }
                });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (recording) {
                    //to convert path to file
                    path.add(AudioSavePathInDevice);

                    mediaRecorder.stop();

                    if (manager!=null){
                        manager.cancelAll();
                    }
                }

                    try {

                        buttonPause.setVisibility(View.INVISIBLE);
                        buttonRecord.setVisibility(View.VISIBLE);
                        scaleDown.cancel();
                        if (manager!=null){
                            manager.cancelAll();
                        }

                        recording = false;
                        second = minute = 0;
                        textView.setText("00:00");

                        File directory2 = new File(Environment.getExternalStorageDirectory() + File.separator + "AudioRecord");
                        if (!directory2.exists()) {
                            directory2.mkdirs();
                        }

                        //to arrive to external path like whatsapp or any image app
                        AudioSavePathInDevice = SavingFiles() + "/" +
                                CreatDateAudioName() + "AudioRecording.3gp";
                        mergeMediaFiles(true, path,  AudioSavePathInDevice);

                        for(int i=0;i<path.size();i++) {
                            File file = new File(path.get(i));
                            deleteRecursive(file);
                        }

                        //clear list
                        path.clear();
                    } catch (Exception e) {}

                Toast.makeText(getActivity(), "Recording Completed", Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }


    // function to start record
    public void mediaRecorderReady() {



        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }


    //to creat filename by his data
    public String CreatDateAudioName() {

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);
        final int sec = calendar.get(Calendar.SECOND);

        String CollectionDate = "" + day + "-" + (month + 1) + "-" + year + "-" + hour + "-" + min + "-" + sec;

        return CollectionDate;
    }


    // to save file
    public String SavingFiles() {
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
        }
    }


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


    public String OrganizeTime(int sec) {
        if (sec >= 59) {
            second = 0;
            minute = minute + 1;

        }
        if (minute >= 59) {
            minute = 0;
            hour = hour + 1;
        }

        String time = "" + hour + " : " + minute + " : " + second;
        return time;
    }





    private String updateTimer() {
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


    public static void mergeAudio(List<File> filesToMerge) {


    }



    public static boolean mergeMediaFiles(boolean isAudio, ArrayList<String> sourceFiles, String targetFile) {
        try {
            String mediaKey = isAudio ? "soun" : "vide";
            List<Movie> listMovies = new ArrayList<>();
            for (String filename : sourceFiles) {
                listMovies.add(MovieCreator.build(filename));
            }
            List<Track> listTracks = new LinkedList<>();
            for (Movie movie : listMovies) {
                for (Track track : movie.getTracks()) {
                    if (track.getHandler().equals(mediaKey)) {
                        listTracks.add(track);
                    }
                }
            }
            Movie outputMovie = new Movie();
            if (!listTracks.isEmpty()) {
                outputMovie.addTrack(new AppendTrack(listTracks.toArray(new Track[listTracks.size()])));
            }
            Container container = new DefaultMp4Builder().build(outputMovie);
            FileChannel fileChannel = new RandomAccessFile(String.format(targetFile), "rw").getChannel();
            container.writeContainer(fileChannel);
            fileChannel.close();
            return true;
        }
        catch (IOException e) {
            Log.e("LOG_TAG", "Error merging media files. exception: "+e.getMessage());
            return false;
        }
    }



    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }


    public void SetAnimation(){

        Animation pulse = AnimationUtils.loadAnimation(getContext(), R.anim.impulse);
        mic.startAnimation(pulse);
        scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                mic,
                PropertyValuesHolder.ofFloat("scaleX", 1.4f),
                PropertyValuesHolder.ofFloat("scaleY", 1.4f));
        scaleDown.setDuration(310);
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();

    }

    void Notification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext());
        mBuilder.setSmallIcon(R.drawable.pause);
        mBuilder.setContentTitle("Record it");
        mBuilder.setContentText("Recording");
        mBuilder.setOngoing(true);

        Intent notificationIntent = new Intent(getContext(),FragmentOne.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        // Add as notification
         manager = (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());

    }


    @Override
    public void onPause() {
        super.onPause();

    }




    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}