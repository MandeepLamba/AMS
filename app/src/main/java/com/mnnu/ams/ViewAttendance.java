package com.mnnu.ams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mnnu.ams.Adapters.SubjectListAdapter;
import com.mnnu.ams.Module.Attendance;
import com.mnnu.ams.Module.Subject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewAttendance extends AppCompatActivity {

    private ArrayList<String> classes;
    private static final String TAG = "mandeep";
    private ArrayList<Subject> subjects;
    private ArrayList<Attendance> attendances;
    private SQLiteDatabase database;
    private Spinner classesSpinner,subjectSpinner;
    private ListView listView;
    private HashMap<String,ArrayList<String>> classSub;
    private String selectedClass="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        getSupportActionBar().setTitle("Attendance");
        classesSpinner = findViewById(R.id.classesSpinner);
        subjectSpinner = findViewById(R.id.subjectSpinner);
        listView = findViewById(R.id.attendanceListView);
        database = openOrCreateDatabase("ams", Context.MODE_PRIVATE, null);
        database.setForeignKeyConstraintsEnabled(true);
        UpdateClassesList();
        UpdateSubjectList();

        setDataToSpinners();

    }

    private void setDataToSpinners() {
        classSub = new HashMap<>();
        for (Subject s :
                subjects) {
            if(!(classSub.containsKey(s.getClassname()))){
                classSub.put(s.getClassname(),new ArrayList<String>());
            }
            classSub.get(s.getClassname()).add(s.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classesSpinner.setAdapter(adapter);
        classesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedClass = adapterView.getSelectedItem().toString();
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(ViewAttendance.this, android.R.layout.simple_spinner_item,classSub.get(adapterView.getSelectedItem().toString()));
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subjectSpinner.setAdapter(adapter1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedSub = adapterView.getSelectedItem().toString();
                if((!selectedClass.equals("")) && (!selectedSub.equals(""))){
                    UpdateAttendancelist(selectedClass,selectedSub);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void UpdateAttendancelist(String c, String s) {
        attendances = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM attendance where classname = ? and subjectname = ?",new String[]{c,s});
        while (cursor.moveToNext()){
            attendances.add(new Attendance(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
        }
        if(attendances.size()<1){
            Toast.makeText(this, "No Attendance found!", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        showAttendanceInList();
    }

    private void showAttendanceInList() {
        HashMap<String,int[]> attended = new HashMap<>();
        for (Attendance a : attendances){
            if(!attended.containsKey(a.getName())){
                attended.put(a.getName(), new int[]{0, 0});
            }
            if(a.getIsPrasent().equals(true)){
                attended.get(a.getName())[0]++;
            }
            attended.get(a.getName())[1]++;
        }
        Log.d(TAG, "showAttendanceInList: keys:"+attended.keySet().toString());
//        Log.d(TAG, "showAttendanceInList: values:"+attended.values().);

    }

    private void UpdateSubjectList() {
        subjects = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM subject",null);
        while (cursor.moveToNext()){
            subjects.add(new Subject(cursor.getString(0),cursor.getString(1)));
        }
        if(subjects.size()<1){
            Toast.makeText(this, "List is Empty, Add Subject", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    private void UpdateClassesList() {
        classes = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM class",null);
        while (cursor.moveToNext()){
            classes.add(cursor.getString(0));
        }
        cursor.close();
    }
}