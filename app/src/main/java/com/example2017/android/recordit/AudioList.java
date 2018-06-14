package com.example2017.android.recordit;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class AudioList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);

        /*
        ListView listView=(ListView) findViewById(R.id.listView);
        File [] listAudio ;

        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "AudioRecord");

        if (directory.isDirectory()){
            listAudio=directory.listFiles();

        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listAudio);
        listView.setAdapter(arrayAdapter);


    }
*/
    }
}
