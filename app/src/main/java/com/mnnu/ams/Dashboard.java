package com.mnnu.ams;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mnnu.ams.Adapters.SubjectListAdapter;
import com.mnnu.ams.HelperClasses.ViewCreator;
import com.mnnu.ams.Module.Subject;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    private static final String TAG = "mandeep";

    private ArrayList<Subject> subjects;
    private ArrayList<String> classes;
    private SQLiteDatabase database;

    @Override
    protected void onStart() {
        UpdateClassesList();
        UpdateSubjectList();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Bundle bundle = getIntent().getExtras();

        getSupportActionBar().setTitle("Dashboard");

        checkOrInitDatabase();
    }


    public void manageClass(View view) {
        startActivity(new Intent(this, ManageClasses.class));
    }

    public void manageStudents(View view) {
        startActivity(new Intent(this, ManageStudent.class));
    }

    public void takeAttendance(View view) {
        ViewCreator creator = new ViewCreator();
        final ViewGroup viewGroup = creator.newStartAttendanceView(this,classes,subjects);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewGroup);
        builder.setTitle("Start Attendance");
        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String cClass =((Spinner)viewGroup.getChildAt(0)).getSelectedItem().toString();
                String cSub =((Spinner)viewGroup.getChildAt(1)).getSelectedItem().toString();

                if(!(cClass.equals(""))&&(!(cSub.equals("")))){
                    Log.d(TAG, "Starting Taking Attendance of class "+ cClass + ", subject "+cSub);
                    Intent intent = new Intent(Dashboard.this, Attendance.class);
                    intent.putExtra("classSubDetails",new String[]{cClass,cSub});
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Dashboard.this, "Select Class and Subject!", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Dashboard.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }

    public void manageSubjects(View view) {
        startActivity(new Intent(this, ManageSubjects.class));
    }

    public void manageAttendance(View view) {
        startActivity(new Intent(this, ViewAttendance.class));
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

    private void checkOrInitDatabase() {
        database = openOrCreateDatabase("ams", Context.MODE_PRIVATE, null);
        database.setForeignKeyConstraintsEnabled(true);
        database.execSQL("create table if not exists class(name TEXT PRIMARY KEY)");
        database.execSQL("create table if not exists student(" +
                "name TEXT NOT NULL," +
                "roll TEXT PRIMARY KEY," +
                "classname TEXT NOT NULL," +
                "FOREIGN KEY (classname) REFERENCES class(name) ON UPDATE CASCADE ON DELETE CASCADE)");

        database.execSQL("create table if not exists subject(" +
                "name TEXT PRIMARY KEY," +
                "class TEXT NOT NULL," +
                "FOREIGN KEY (class) REFERENCES class(name) ON UPDATE CASCADE ON DELETE CASCADE)");

        database.execSQL("create table if not exists attendance(" +
                "name TEXT NOT NULL," +
                "roll TEXT NOT NULL," +
                "classname TEXT NOT NULL," +
                "subjectname TEXT NOT NULL," +
                "isPrasent TEXT NOT NULL," +

                "FOREIGN KEY (classname) REFERENCES class(name) ON UPDATE CASCADE ON DELETE CASCADE)");
    }

}