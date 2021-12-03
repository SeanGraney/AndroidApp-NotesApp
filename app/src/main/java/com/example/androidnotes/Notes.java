package com.example.androidnotes;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.util.Date;

public class Notes implements Serializable {

    private String title;
    private String notes;
    private String lastSave;
    private long cmpDate;

    private static int count = 1;
    private final String pattern = "EEE MMM dd, hh:mm a";
    private final SimpleDateFormat simpleDateformat = new SimpleDateFormat(pattern);

    Notes() {
        this.title = "Note Title " +count;
        this.notes = "notes...... " +count;
        this.lastSave = timeStamp();
        this.cmpDate = cmpDate();
        count++;
    }

    Notes(String title, String notes) {
        this.title = title;
        this.notes = notes;
        this.lastSave = timeStamp();
        this.cmpDate = cmpDate();
        count++;
    }

    public String getTitle(){
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public String getLastSave() { return lastSave; }

    public long getCmpDate() { return cmpDate; }

    public static int getCount() { return count; }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setLastSave(){
        this.lastSave = timeStamp();
    }

    public void setCmpDate() {
        this.cmpDate = cmpDate();
    }

    public long cmpDate() {
        Date date = new Date();
        return date.getTime();
    }

    public String timeStamp() {
        return simpleDateformat.format(new Date());
    }


    @NonNull
    @Override
    public String toString(){
        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("title").value(getTitle());
            jsonWriter.name("notes").value(getNotes());
            jsonWriter.name("lastSave").value(getLastSave());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
