package com.mnnu.ams;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mnnu.ams.Adapters.SubjectListAdapter;
import com.mnnu.ams.HelperClasses.MyFirebaseManager;
import com.mnnu.ams.HelperClasses.ViewCreator;
import com.mnnu.ams.Module.Subject;

import java.util.ArrayList;

public class ManageSubjects extends AppCompatActivity {

    private static final String TAG = "mandeep";

    private FloatingActionButton addSubjectButton;
    private String myEmail;
    private ListView listView;
    private ArrayList<Subject> subjects;
    private ArrayList<String> classes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_subjects);
        getSupportActionBar().setTitle("Subjects");


        SharedPreferences preferences = getSharedPreferences("username", MODE_PRIVATE);
        myEmail = preferences.getString("username", "");

        listView = findViewById(R.id.subjectListView);
        addSubjectButton = findViewById(R.id.addSubjectFloatingButton);

        addSubjectButton.setOnClickListener(view -> {
            ViewCreator creator = new ViewCreator();
            final ViewGroup viewGroup = creator.newSubjectPromptView(ManageSubjects.this, classes);
            new AlertDialog.Builder(ManageSubjects.this)
                    .setView(viewGroup)
                    .setTitle("Create Subject")
                    .setPositiveButton("Create", (dialogInterface, i) -> {
                        String sName = ((EditText) viewGroup.getChildAt(0)).getText().toString();
                        String sCode = ((EditText) viewGroup.getChildAt(1)).getText().toString();
                        String sClassName = ((Spinner) viewGroup.getChildAt(2)).getSelectedItem().toString();
                        if (!sName.equals("") && !sClassName.equals("")) {
                            createSubject(sName, sClassName, sCode);
                        } else {
                            Toast.makeText(ManageSubjects.this, "Empty Name!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(ManageSubjects.this, "Cancelled", Toast.LENGTH_SHORT).show()).show();
        });
        UpdateSubjectList();
        UpdateClassesList();

    }

    private void createSubject(String sName, String sClassName, String sCode) {
        MyFirebaseManager.getInstance().addSubject(myEmail, new Subject(sName, sClassName, sCode), aVoid -> UpdateSubjectList());
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
                    SubjectListAdapter listAdapter = new SubjectListAdapter(ManageSubjects.this, subjects);
                    listView.setAdapter(listAdapter);
                } else {
                    Toast.makeText(ManageSubjects.this, "Add Subjects", Toast.LENGTH_SHORT).show();
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
                } else {
                    Toast.makeText(ManageSubjects.this, "Add Classes First", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}