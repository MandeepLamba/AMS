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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mnnu.ams.Adapters.StudentListAdapter;
import com.mnnu.ams.HelperClasses.ViewCreator;
import com.mnnu.ams.Module.Student;

import java.util.ArrayList;
import java.util.List;

public class ManageStudent extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton actionButton;
    private ArrayList<String> classes;
    private List<Student> students;
    private SQLiteDatabase database;

    private static final String TAG = "mandeep";

    @Override
    protected void onStart() {
        UpdateClassesList();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_student);
        getSupportActionBar().setTitle("Students");

        actionButton = findViewById(R.id.addstudentActionButton);
        listView = findViewById(R.id.studentlist);


        database = openOrCreateDatabase("ams", Context.MODE_PRIVATE, null);
        database.setForeignKeyConstraintsEnabled(true);

        UpdateStudentsList();


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCreator creator = new ViewCreator();
                final ViewGroup viewGroup = creator.newStudentPromptView(ManageStudent.this, classes);

                final AlertDialog.Builder builder = new AlertDialog.Builder(ManageStudent.this);
                builder.setView(viewGroup);
                builder.setTitle("New Student");
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sName =((EditText)viewGroup.getChildAt(0)).getText().toString();
                        String sRoll =((EditText)viewGroup.getChildAt(1)).getText().toString();
                        String sClass =((Spinner)viewGroup.getChildAt(2)).getSelectedItem().toString();
                        if(!sName.equals("") && !sClass.equals("") && !sRoll.equals("")){
                            addStudent(sName,sRoll,sClass);
                        }
                        else {
                            Toast.makeText(ManageStudent.this, "Fill All Details!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ManageStudent.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String uRoll = students.get(i).getsRoll();
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageStudent.this);
                builder.setTitle("Delete?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int r = database.delete("student","roll=?", new String[]{uRoll});
                        UpdateStudentsList();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ManageStudent.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).show();

                return true;
            }
        });
        
    }

    private void addStudent(String sName, String sRoll, String sClass) {
        ContentValues values = new ContentValues();
        values.put("name",sName);
        values.put("roll",sRoll);
        values.put("classname",sClass);
        if(database.insert("student",null,values)!=-1){
            Log.d(TAG, "Student added: "+ sName +" : " + sRoll +" : "+ sClass);
            UpdateStudentsList();
        }
        else{
            Toast.makeText(this, "Adding Student failed!", Toast.LENGTH_SHORT).show();
        }
    }



    private void UpdateStudentsList() {
        students = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM student",null);
        while (cursor.moveToNext()){
            Student student = new Student(cursor.getString(0),cursor.getString(1),cursor.getString(2));
            students.add(student);
        }
        cursor.close();
        StudentListAdapter listAdapter = new StudentListAdapter(this,students);
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