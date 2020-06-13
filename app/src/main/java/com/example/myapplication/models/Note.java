package com.example.myapplication.models;

import android.app.PendingIntent;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity

public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
      int id;
    public String text;
    @TypeConverters({Converters.class})
    public Date date;

    public int pid;
    public boolean ischecked;
    public int imgid;



}
