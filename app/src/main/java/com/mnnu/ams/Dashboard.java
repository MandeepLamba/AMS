package com.mnnu.ams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mnnu.ams.HelperClasses.MyFirebaseManager;
import com.mnnu.ams.HelperClasses.ViewCreator;
import com.mnnu.ams.Module.Subject;

import java.util.ArrayList;
import java.util.HashSet;

public class Dashboard extends AppCompatActivity {
    private static final String TAG = "mandeep";

    private ArrayList<Subject> subjects;
    private ArrayList<String> classes;
    private String myEmail;
    private boolean haveSubs, haveClasses;


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
        getSupportActionBar().setTitle("Dashboard");


        SharedPreferences preferences = getSharedPreferences("username", MODE_PRIVATE);
        myEmail = preferences.getString("username", "");

    }


    public void manageClass(View view) {
        startActivity(new Intent(this, ManageClasses.class));
    }

    public void manageStudents(View view) {
        startActivity(new Intent(this, ManageStudent.class));
    }

    public void takeAttendance(View view) {
        if (haveClasses && haveSubs) {
            ViewCreator creator = new ViewCreator();
            final ViewGroup viewGroup = creator.newStartAttendanceView(this, classes, subjects);

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(viewGroup);
            builder.setTitle("Start Attendance");
            builder.setPositiveButton("Start", (dialogInterface, i) -> {
                if (((Spinner) viewGroup.getChildAt(0)).getSelectedItem() != null &&
                        ((Spinner) viewGroup.getChildAt(1)).getSelectedItem() != null) {

                    String cClass = ((Spinner) viewGroup.getChildAt(0)).getSelectedItem().toString();
                    String cSub = ((Spinner) viewGroup.getChildAt(1)).getSelectedItem().toString();
                    Log.d(TAG, "Starting Taking Attendance of class " + cClass + ", subject " + cSub);
                    Intent intent = new Intent(Dashboard.this, AttendanceActivity.class);
                    intent.putExtra("classSubDetails", new String[]{cClass, cSub});
                    startActivity(intent);

                } else {
                    Toast.makeText(Dashboard.this, "Select Class and Subject!", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(Dashboard.this, "Cancelled", Toast.LENGTH_SHORT).show()).show();
        } else {
            Toast.makeText(this, "Loading Classes, Please Wait!", Toast.LENGTH_SHORT).show();
        }
    }

    public void manageSubjects(View view) {
        startActivity(new Intent(this, ManageSubjects.class));
    }

    public void manageAttendance(View view) {
        startActivity(new Intent(this, ViewAttendance.class));
    }

    private void UpdateSubjectList() {
        subjects = new ArrayList<>();
        MyFirebaseManager.getInstance().getSubjects(myEmail, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        subjects.add(s.getValue(Subject.class));
                    }
                    if (subjects.size() > 0) haveSubs = true;
                } else {
                    Toast.makeText(Dashboard.this, "Add Subjects", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void UpdateClassesList() {
        MyFirebaseManager.getInstance().getClasses(myEmail, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    classes = new ArrayList<>();
                    for (DataSnapshot cl : dataSnapshot.getChildren()) {
                        classes.add(cl.getValue().toString());
                    }
                    if (classes.size() > 0) haveClasses = true;
                    getSharedPreferences("username", MODE_PRIVATE).edit().putStringSet("classes", new HashSet<>(classes)).apply();

                } else {
                    Toast.makeText(Dashboard.this, "Add Classes First", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}