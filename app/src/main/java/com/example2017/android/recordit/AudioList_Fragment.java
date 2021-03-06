package com.example2017.android.recordit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by M7moud on 17-Jun-18.
 */
public class AudioList_Fragment extends Fragment {

    SharedPreferences sh;
    File[] AudioListFiles;
    MediaPlayer mediaPlayer2;
    ListView listView;
     Customlistview customlistview;

    public AudioList_Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_audio_list, null);



        listView = (ListView) view.findViewById(R.id.listView);

        customlistview = new Customlistview(ConvertFilesToArray());
        listView.setAdapter(customlistview);
        return view;

    }



    public ArrayList ConvertFilesToArray() {
        ArrayList<ListItem> arrayList = new ArrayList<>();
        try {


            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "AudioRecord");
            AudioListFiles = directory.listFiles();
            if (directory.isDirectory()) {

                for (int i = 0; i < (directory.length() - 1); i++) {
                    mediaPlayer2=new android.media.MediaPlayer();
                    mediaPlayer2.setDataSource(AudioListFiles[i].getAbsolutePath());
                    mediaPlayer2.prepare();
                    String duration = organizeTime(mediaPlayer2.getDuration());
                    String name = AudioListFiles[i].getName();
                    Double size = (AudioListFiles[i].length() / 1024.0);


                    arrayList.add(new ListItem(name,
                            String.valueOf((approximate(size) + "Kb")),
                            duration

                    ));
                }

            }

            return arrayList;

        } catch (Exception e) {

        }

        return arrayList;

    }

//to calculate the size of file in approximate to 2 digit number
// 12,16311231  = 12,16
    public double approximate(double number) {
        int n = (int) (number * 100);
        double num = n / 100.0;
        return num;
    }


    class Customlistview extends BaseAdapter {

        ArrayList<ListItem> arrayList = new ArrayList<>();

        public Customlistview(ArrayList<ListItem> arrayList) {
            this.arrayList = arrayList;
        }


        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i).name;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            final LayoutInflater inflater = getLayoutInflater(null);
            View view1 = inflater.inflate(R.layout.playing_design, null);
            TextView txt_name = (TextView) view1.findViewById(R.id.textView_name);
            TextView txt_size = (TextView) view1.findViewById(R.id.textView_size);
            TextView txt_duration = (TextView) view1.findViewById(R.id.textView_date);
            Button but_play = (Button) view1.findViewById(R.id.but_play);
            Button but_delete = (Button) view1.findViewById(R.id.but_delete);

            txt_name.setText(arrayList.get(i).name);
            txt_size.setText(arrayList.get(i).duration);
            txt_duration.setText(arrayList.get(i).date);


            but_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent=new Intent(getActivity(),MediaPlayerView.class);
                    intent.putExtra("postion",i);
                    startActivity(intent);
                    /*
                    String directory = (Environment.getExternalStorageDirectory() + File.separator + "AudioRecord" + File.separator + arrayList.get(i).name).trim();
                    sh=getActivity().getSharedPreferences("PLZ", Context.MODE_PRIVATE);
                    SharedPreferences.Editor  mydata=sh.edit();
                    mydata.putString( "data",directory);
                    mydata.commit();
*/
                    /*
                    try {

                        //to play what w select


                        mediaPlayer2 = new MediaPlayerView();


                        //adding name to standard path
                        String directory = (Environment.getExternalStorageDirectory() + File.separator + "AudioRecord" + File.separator + arrayList.get(i).name).trim();
                        mediaPlayer2.setDataSource(directory);
                        mediaPlayer2.prepare();
                        mediaPlayer2.start();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
*/

                }
            });


            //delete it from listview
            but_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final String directory = (Environment.getExternalStorageDirectory() + File.separator + "AudioRecord" + File.separator + arrayList.get(i).name).trim();

                    final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setMessage("Do you want to delete this file");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {




                            File DirectExistFile = new File(directory);

                            deleteRecursive(DirectExistFile);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Toast.makeText(getContext(), "file deleted", Toast.LENGTH_SHORT).show();
                            }

                            // to refresh listview when item deleted
                            final Customlistview customlistview = new Customlistview(ConvertFilesToArray());

                            listView.setAdapter(customlistview);


                        }
                    });

                    builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();

                }
                    });



            return view1;
        }






        void deleteRecursive(File fileOrDirectory) {
            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    deleteRecursive(child);

            fileOrDirectory.delete();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here



            customlistview = new Customlistview(ConvertFilesToArray());
            listView.setAdapter(customlistview);
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

