package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.myapplication.models.App;
import com.example.myapplication.models.AppDatabase;
import com.example.myapplication.models.Note;
import com.example.myapplication.models.NoteDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Note> notes = new ArrayList<>();
    List<Note> filters=new ArrayList<>();
    private int mYear, mMonth, mDay, mHour, mMinute;
    RecyclerView r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialData();
        FloatingActionButton fab = findViewById(R.id.fab);
r=findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DataAdapter adapter = new DataAdapter(this, notes);
        r.setLayoutManager(layoutManager);
      r.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i=new Intent(getApplicationContext(),AddActivity.class);
              startActivity(i);

            }
        });

    }
    private void setInitialData(){
        AppDatabase db = App.getInstance().getDatabase();
        NoteDao noteDao = db.noteDao();
        notes=noteDao.getAll();



    }
    public void click(View v)
    {



            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
filters.clear();


                            Date d=new Date();
                            d.setYear(year);
                            d.setMonth(monthOfYear);
                            d.setDate(dayOfMonth);




                           for(Note element:notes)
                           {
if(d.getYear()==(element.date.getYear()+1900) && d.getMonth()==element.date.getMonth() &&  d.getDate()==element.date.getDate())
{
   filters.add(element);
}
                           }
                            LinearLayoutManager layoutManager
                                    = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                            r.setLayoutManager(layoutManager);
                           DataAdapter adap=new DataAdapter(MainActivity.this,filters);
                            r.setAdapter(adap);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

    }
}
