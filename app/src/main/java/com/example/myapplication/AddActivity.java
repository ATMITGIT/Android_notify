package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.models.App;
import com.example.myapplication.models.AppDatabase;
import com.example.myapplication.models.Note;
import com.example.myapplication.models.NoteDao;

import java.util.Calendar;
import java.util.Random;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
EditText e;
    TextView txtDate,txtTime;
    Button addBtn,btnDatePicker,btnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Data d;
    Time t;
    Calendar k;
    Note note;
    boolean logic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        e=findViewById(R.id.text);
        txtDate=findViewById(R.id.textView6);
        txtTime=findViewById(R.id.textView8);
        addBtn=findViewById(R.id.button);
        btnDatePicker=findViewById(R.id.button2);
        btnTimePicker=findViewById(R.id.button3);
        addBtn.setOnClickListener(this);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        createNotificationChanel();
        k=Calendar.getInstance();
        Intent i=getIntent();
        if(i.getExtras()==null)
        {
            Toast.makeText(this,"null",Toast.LENGTH_SHORT).show();
            logic=false;
        }
        else
        {
            note= (Note) getIntent().getSerializableExtra("note");
            logic=true;
            e.setText(note.text);
        }

    }


    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            d=new Data(year,monthOfYear,dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
t=new Time(hourOfDay,minute);
                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if(v==addBtn) {
            if (e.getText().toString().length() > 0 && txtTime.getText().toString().length() > 0 && txtDate.getText().toString().length() > 0) {
                k.clear();
                k.set(Calendar.YEAR, d.getYear());

                k.set(Calendar.MONTH, d.getMonth());
                k.set(Calendar.DAY_OF_MONTH, d.getDay());
                k.set(Calendar.HOUR_OF_DAY, t.getTime());
                k.set(Calendar.MINUTE, t.getMinute());
                if(logic)
                {
                    cancelNotification(this,note.pid);
                }
                int rand = new Random().nextInt();

                Intent intent = new Intent(AddActivity.this, Reciver.class);
                intent.putExtra("text", e.getText().toString());
                intent.putExtra("id", String.valueOf(rand));

                PendingIntent p = PendingIntent.getBroadcast(AddActivity.this, rand, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager al = (AlarmManager) getSystemService(ALARM_SERVICE);


                al.set(AlarmManager.RTC_WAKEUP, k.getTimeInMillis(), p);
                AppDatabase db = App.getInstance().getDatabase();
                NoteDao noteDao = db.noteDao();

                if(logic)
                {  note.text = e.getText().toString();

                    note.date = k.getTime();

                    note.pid = rand;
                    note.ischecked = false;
                    note.imgid = android.R.drawable.ic_notification_clear_all;
                    noteDao.update(note);
                }
                else
                {
                    Note note1 = new Note();
                    note1.text = e.getText().toString();

                    note1.date = k.getTime();

                    note1.pid = rand;
                    note1.ischecked = false;
                    note1.imgid = android.R.drawable.ic_notification_clear_all;
                    noteDao.insert(note1);
                }


                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
            else
            {
                Toast.makeText(this,"Добавитье текцт дату и время",Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void createNotificationChanel()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            CharSequence name="LemubitReminderChanel";
            String description="Chanel for lemubit";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel chanel=new NotificationChannel("notifyLemubit",name,importance);
            chanel.setDescription(description);
            NotificationManager m =getSystemService(NotificationManager.class);
            assert m != null;
            m.createNotificationChannel(chanel);

        }
    }
    public static void cancelNotification(Context context, int code) {
        Intent intent = new Intent(context, Reciver.class);

        PendingIntent pending = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pending);
    }
}
