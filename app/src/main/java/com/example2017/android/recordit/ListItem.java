package com.example2017.android.recordit;

/**
 * Created by M7moud on 14-Jun-18.
 */
public class ListItem {

    String name,duration,date;

    public ListItem() {
    }

    public ListItem(String name, String duration, String date) {
        this.name = name;
        this.duration = duration;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
