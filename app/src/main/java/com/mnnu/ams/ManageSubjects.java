package com.mnnu.ams;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mnnu.ams.Adapters.StudentListAdapter;
import com.mnnu.ams.Adapters.SubjectListAdapter;
import com.mnnu.ams.HelperClasses.ViewCreator;
import com.mnnu.ams.Module.Student;
import com.mnnu.ams.Module.Subject;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;

public class ManageSubjects extends AppCompatActivity {

    private static final String TAG = "mandeep";

    private FloatingActionButton addSubjectButton;
    private SQLiteDatabase database;
    private ListView listView;
    private ArrayList<Subject> subjects;
    private ArrayList<String> classes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_subjects);

        getSupportActionBar().setTitle("Subjects");

        database = openOrCreateDatabase("ams", Context.MODE_PRIVATE, null);
        database.setForeignKeyConstraintsEnabled(true);


        listView = findViewById(R.id.subjectListView);
        addSubjectButton = findViewById(R.id.addSubjectFloatingButton);

        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCreator creator = new ViewCreator();
                final ViewGroup viewGroup = creator.newSubjectPromptView(ManageSubjects.this, classes);
                final AlertDialog.Builder builder = new AlertDialog.Builder(ManageSubjects.this);
                builder.setView(viewGroup);
                builder.setTitle("Create Subject");
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sName =((EditText)viewGroup.getChildAt(0)).getText().toString();
                        String sClassName =((Spinner)viewGroup.getChildAt(1)).getSelectedItem().toString();
                        if(!sName.equals("")&&!sClassName.equals("")){
                            createSubject(sName,sClassName);
                        }
                        else {
                            Toast.makeText(ManageSubjects.this, "Empty Name!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ManageSubjects.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
        UpdateSubjectList();
        UpdateClassesList();

    }

    private void createSubject(String sName, String sClassName) {
        ContentValues values = new ContentValues();
        values.put("name",sName);
        values.put("class",sClassName);
        if(database.insert("subject",null,values)!=-1){
            Log.d(TAG, "createSubject: Subject Created");
            UpdateSubjectList();
        }
        else{
            Toast.makeText(this, "Creating Class failed!", Toast.LENGTH_SHORT).show();
        }
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
        SubjectListAdapter listAdapter = new SubjectListAdapter(this,subjects);

        listView.setAdapter(listAdapter);
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