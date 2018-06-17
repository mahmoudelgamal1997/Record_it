package com.example2017.android.recordit;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AudioList extends AppCompatActivity {
    File[] AudioListFiles;
    MediaPlayer mediaPlayer2;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);


        mediaPlayer2=new MediaPlayer();
         listView = (ListView) findViewById(R.id.listView);

      //  ArrayList<ListItem> arrayList=new ArrayList<>();

        final Customlistview customlistview=new Customlistview(ConvertFilesToArray());

        //        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ConvertFilesToArray());
        listView.setAdapter(customlistview);

    }


    public ArrayList ConvertFilesToArray() {
        ArrayList<ListItem> arrayList = new ArrayList<>();
try {


    File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "AudioRecord");
    AudioListFiles = directory.listFiles();
    if (directory.isDirectory()) {

        for (int i = 0; i < (directory.length()-1); i++) {
            mediaPlayer2.setDataSource(AudioListFiles[i].getAbsolutePath());
            int duration=mediaPlayer2.getDuration();
            String name =AudioListFiles[i].getName();
            Double size= (AudioListFiles[i].length() / 1024.0);


            arrayList.add(new ListItem(name,
                   String.valueOf((approximate(size)+"Kb")),
                    String.valueOf(duration)

                    ));
        }

    }

    return arrayList;
    }catch (Exception e) {

    }

    return arrayList;

    }


    public double  approximate(double number)
    {
        int n = (int) (number *100);
        double num = n/100.0;
        return num;
    }


class Customlistview extends BaseAdapter{

    ArrayList<ListItem> arrayList=new ArrayList<>();

    public Customlistview(ArrayList<ListItem> arrayList) {this.arrayList = arrayList;}



    @Override
    public int getCount()
    {
    return arrayList.size();
    }
    @Override
    public Object getItem(int i)
    {
        return arrayList.get(i).name;
    }
    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater=getLayoutInflater();
        View view1=inflater.inflate(R.layout.playing_design,null);
        TextView txt_name=(TextView)view1.findViewById(R.id.textView_name);
        TextView txt_duration=(TextView)view1.findViewById(R.id.textView_size);
        TextView txt_date=(TextView)view1.findViewById(R.id.textView_date);
        Button   but_play=(Button)view1.findViewById(R.id.but_play);
        Button   but_delete=(Button)view1.findViewById(R.id.but_delete);

        txt_name.setText(arrayList.get(i).name);
        txt_duration.setText(arrayList.get(i).duration);
        txt_date.setText(arrayList.get(i).date);


        but_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    mediaPlayer2=new MediaPlayer();

                    //to play what w select
                    //adding name to standard path


                    String directory =(Environment.getExternalStorageDirectory()+ File.separator + "AudioRecord"+ File.separator+arrayList.get(i).name).trim();
                    mediaPlayer2.setDataSource(directory);
                    mediaPlayer2.prepare();
                    mediaPlayer2.start();


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext() , e.getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });

but_delete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        String directory =(Environment.getExternalStorageDirectory()+ File.separator + "AudioRecord"+ File.separator+arrayList.get(i).name).trim();


        File DirectExistFile=new File(directory);

            deleteRecursive(DirectExistFile);
            Toast.makeText(getApplicationContext(),"file deleted",Toast.LENGTH_LONG).show();


        final Customlistview customlistview=new Customlistview(ConvertFilesToArray());

        listView.setAdapter(customlistview);





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



}