package com.example.myapplication;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.models.App;
import com.example.myapplication.models.AppDatabase;
import com.example.myapplication.models.Note;
import com.example.myapplication.models.NoteDao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Note> notes;
    Context mContext;
    DataAdapter(Context context, List<Note> notes) {
        this.notes = notes;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        final Note note = notes.get(position);
final  int pp=position;
        holder.imageView.setImageResource(android.R.drawable.ic_notification_clear_all);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification(mContext,note.pid);
                AppDatabase db = App.getInstance().getDatabase();
                NoteDao noteDao = db.noteDao();
                noteDao.delete(note);
                notes.remove(pp);
                notifyDataSetChanged();
            }
        });
        holder.text.setText(note.text);
       final Calendar c=Calendar.getInstance();
        c.setTime(note.date);
        holder.date.setText(note.date.toString());
        holder.ch.setChecked(note.ischecked);
        holder.ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                 @Override
                                                 public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                     AppDatabase db = App.getInstance().getDatabase();
                                                     NoteDao noteDao = db.noteDao();
                                                 if(isChecked)
                                                 {
                                                     note.ischecked=true;

                                                cancelNotification(mContext,note.pid);
                                                 }
                                                 else
                                                 {
                                                     note.ischecked=false;
                                                     scheduleNotification(mContext,c,note);
                                                 }
                                                 noteDao.update(note);
                                                 }
                                             }
        );
holder.text.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       Intent i=new Intent(mContext,AddActivity.class);
       i.putExtra("note",  note);
       mContext.startActivity(i);
    }
});
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView text, date;
        CheckBox ch;
        LinearLayout l;
        ViewHolder(View view){
            super(view);
           imageView = view.findViewById(R.id.delete);
            date =  view.findViewById(R.id.textView2);
            ch = view.findViewById(R.id.completed);
            text=view.findViewById(R.id.note_text);

        }
    }
    public static void cancelNotification(Context context, int code) {
        Intent intent = new Intent(context, Reciver.class);

        PendingIntent pending = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pending);
    }
    public static void scheduleNotification(Context context,Calendar c,Note n) {
        int rand=new Random().nextInt();
        Intent intent = new Intent(context, Reciver.class);
        intent.putExtra("text", n.text);
        intent.putExtra("id",String.valueOf(rand));
        PendingIntent p = PendingIntent.getBroadcast(context, rand, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager al = (AlarmManager)context. getSystemService(Context.ALARM_SERVICE);
        al.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), p);
        AppDatabase db = App.getInstance().getDatabase();
        NoteDao noteDao = db.noteDao();
        n.pid=rand;
        noteDao.update(n);
    }
}