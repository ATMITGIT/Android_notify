package com.example.myapplication;

public class Time {
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Time(int time, int minute) {
        this.time = time;
        this.minute = minute;
    }

    int time;
    int minute;
}
