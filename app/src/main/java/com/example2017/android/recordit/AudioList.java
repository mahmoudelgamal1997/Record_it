package com.example2017.android.recordit;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class AudioList extends AppCompatActivity {

    MediaPlayer mediaPlayer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);


        mediaPlayer2=new MediaPlayer();
        ListView listView = (ListView) findViewById(R.id.listView);

      //  ArrayList<ListItem> arrayList=new ArrayList<>();
        Customlistview customlistview=new Customlistview(ConvertFilesToArray());

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ConvertFilesToArray());
        listView.setAdapter(customlistview);


    }


    public ArrayList ConvertFilesToArray() {
        ArrayList<ListItem> arrayList = new ArrayList<>();
try {
    File[] Files;
    File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "AudioRecord");
    Files = directory.listFiles();
    if (directory.isDirectory()) {

        for (int i = 0; i < (directory.length()-1); i++) {
            mediaPlayer2.setDataSource(Files[i].getAbsolutePath());
            int duration=mediaPlayer2.getDuration();
            String name =Files[i].getName();
            Double size= (Files[i].length() / 1024.0);


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



    public double  approximate(double number){
        int n = (int) (number *100);
        double num = n/100;
    return num;
    }


class Customlistview extends BaseAdapter{

    ArrayList<ListItem> arrayList=new ArrayList<>();

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
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater=getLayoutInflater();
        View view1=inflater.inflate(R.layout.playing_design,null);
        TextView txt_name=(TextView)view1.findViewById(R.id.textView_name);
        TextView txt_duration=(TextView)view1.findViewById(R.id.textView_size);
        TextView txt_date=(TextView)view1.findViewById(R.id.textView_date);


        txt_name.setText(arrayList.get(i).name);
        txt_duration.setText(arrayList.get(i).duration);
        txt_date.setText(arrayList.get(i).date);




        return view1;
    }
}

}