package com.mnnu.ams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mnnu.ams.Adapters.StudentListAdapter;
import com.mnnu.ams.HelperClasses.ViewCreator;
import com.mnnu.ams.Module.Student;

import java.util.ArrayList;
import java.util.List;

public class Attendance extends AppCompatActivity {
    private static final String TAG = "mandeep";

    private ListView listView;
    private FloatingActionButton actionButton;
    private List<Student> students;
    private String aClass, aSubject;
    private SQLiteDatabase database;
    private FrameLayout buttonLayout;
    private Boolean frameVisible=false;
    private int checkedCount=0;
    private ImageButton attendanceYes, attendanceNo;

    @Override
    protected void onStart() {
        UpdateStudentsList();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        String[] details = (String[]) getIntent().getExtras().get("classSubDetails");
        aClass = details[0];
        aSubject = details[1];
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(details[0]);
        supportActionBar.setSubtitle(details[1]);

        database = openOrCreateDatabase("ams", Context.MODE_PRIVATE, null);
        database.setForeignKeyConstraintsEnabled(true);
        listView = findViewById(R.id.attendanceStudentList);
        actionButton = findViewById(R.id.startAttendanceActionButton);
        buttonLayout = findViewById(R.id.buttonframe);


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCreator creator = new ViewCreator();
                ViewGroup viewGroup = creator.newAttendanceCheckLayout(Attendance.this);
                
                attendanceYes = (ImageButton) viewGroup.getChildAt(0);
                attendanceNo = (ImageButton) viewGroup.getChildAt(1);
                
                if(frameVisible){
                    actionButton.setImageResource(R.drawable.ic_play_arrow);
                    buttonLayout.removeAllViews();
                    frameVisible=false;
                    Checking(false);
                }
                else {
                    actionButton.setImageResource(R.drawable.ic_baseline_cancel_24);
                    buttonLayout.addView(viewGroup);
                    frameVisible=true;
                    Checking(true);
                }
            }
        });

    }

    private void Checking(Boolean is) {
        if(is){
            listView.getChildAt(checkedCount).setBackgroundColor(Color.YELLOW);
            attendanceYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listView.getChildAt(checkedCount).setBackgroundColor(Color.GREEN);
                    saveAttendance(checkedCount,true);
                    if(checkedCount<students.size()-1) {
                        checkedCount++;
                        listView.getChildAt(checkedCount).setBackgroundColor(Color.YELLOW);
                    }
                    else{
                        buttonLayout.removeAllViews();
                        actionButton.setImageResource(R.drawable.ic_play_arrow);
                    }
                }
            });
            attendanceNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listView.getChildAt(checkedCount).setBackgroundColor(Color.RED);
                    saveAttendance(checkedCount,false);
                    if(checkedCount<students.size()-1) {
                        checkedCount++;
                        listView.getChildAt(checkedCount).setBackgroundColor(Color.YELLOW);
                    }
                    else{
                        buttonLayout.removeAllViews();
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                    }
                }
            });
        }
        else{
            listView.getChildAt(checkedCount).setBackgroundColor(Color.WHITE);
        }
        
    }

    private void saveAttendance(int i, boolean b) {

        ContentValues values = new ContentValues();
        values.put("name", students.get(i).getsName());
        values.put("roll", students.get(i).getsRoll());
        values.put("classname", students.get(i).getsClass());
        values.put("subjectname", aSubject);
        values.put("isPrasent", b);
        database.insert("attendance",null, values);
    }

    private void UpdateStudentsList() {
        students = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM student where classname =?",new String[]{aClass});
        while (cursor.moveToNext()){
            Student student = new Student(cursor.getString(0),cursor.getString(1),cursor.getString(2));
            students.add(student);
        }
        cursor.close();
        StudentListAdapter listAdapter = new StudentListAdapter(this,students);
        listView.setAdapter(listAdapter);
    }
}