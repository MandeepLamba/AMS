package com.mnnu.ams;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mnnu.ams.Adapters.StudentListAdapter;
import com.mnnu.ams.HelperClasses.MyFirebaseManager;
import com.mnnu.ams.HelperClasses.ViewCreator;
import com.mnnu.ams.Module.Student;

import java.util.ArrayList;
import java.util.List;

public class ManageStudent extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton actionButton;
    private ArrayList<String> classes;
    private String myEmail;
    private ProgressBar progressBar;
    private StudentListAdapter listAdapter;
    private Spinner classSpinner;

    private List<Student> students;

    private static final String TAG = "mandeep";

    @Override
    protected void onStart() {
        UpdateClassesList();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0)
                    listAdapter.getFilter().filter("");
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_student);
        getSupportActionBar().setTitle("Students");

        SharedPreferences preferences = getSharedPreferences("username", MODE_PRIVATE);
        myEmail = preferences.getString("username", "");
        progressBar = findViewById(R.id.progressBar3);
        classSpinner = findViewById(R.id.student_class_name_spinner);

        actionButton = findViewById(R.id.addstudentActionButton);
        listView = findViewById(R.id.studentlist);
        listAdapter = new StudentListAdapter(ManageStudent.this);
        listView.setAdapter(listAdapter);


        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progressBar.setVisibility(View.VISIBLE);
                UpdateStudentsList(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        actionButton.setOnClickListener(view -> {
            ViewCreator creator = new ViewCreator();
            final ViewGroup viewGroup = creator.newStudentPromptView(ManageStudent.this, classes);

            final AlertDialog.Builder builder = new AlertDialog.Builder(ManageStudent.this);
            builder.setView(viewGroup);
            builder.setTitle("New Student");
            builder.setPositiveButton("Add", (dialogInterface, i) -> {

                String sName = ((EditText) viewGroup.getChildAt(0)).getText().toString();
                String sRoll = ((EditText) viewGroup.getChildAt(1)).getText().toString();
                String sClass = ((Spinner) viewGroup.getChildAt(2)).getSelectedItem().toString();
                if (!sName.equals("") && !sClass.equals("") && !sRoll.equals("")) {
                    addStudent(new Student(sName, sRoll, sClass));
                } else {
                    Toast.makeText(ManageStudent.this, "Fill All Details!", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(ManageStudent.this, "Cancelled", Toast.LENGTH_SHORT).show()).show();
        });

        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            final String uRoll = students.get(i).getRoll();
            final String uClass = students.get(i).getClassname();
            new AlertDialog.Builder(ManageStudent.this)
                    .setTitle("Delete?")
                    .setPositiveButton("Delete", (dialogInterface, i12) -> MyFirebaseManager.getInstance().deleteStudent(myEmail, uRoll, uClass, aVoid -> UpdateStudentsList(classSpinner.getItemAtPosition(classSpinner.getSelectedItemPosition()).toString())))
                    .setNegativeButton("Cancel", (dialogInterface, i1) -> Toast.makeText(ManageStudent.this, "Cancelled", Toast.LENGTH_SHORT).show()).show();

            return true;
        });
    }

    private void addStudent(Student s) {
        Log.d(TAG, "addStudent: class" + classSpinner.getItemAtPosition(classSpinner.getSelectedItemPosition()).toString());
        MyFirebaseManager.getInstance().addStudent(myEmail, s, aVoid -> UpdateStudentsList(classSpinner.getItemAtPosition(classSpinner.getSelectedItemPosition()).toString()));
    }


    private void UpdateStudentsList(String cName) {
        students = new ArrayList<>();
        MyFirebaseManager.getInstance().getStudents(myEmail, cName, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot stu : dataSnapshot.getChildren()) {
                        Log.d(TAG, "onDataChange: stu: " + stu);
                        Student student = stu.getValue(Student.class);
                        students.add(student);
                    }
                    Log.d(TAG, "onDataChange: " + students);
                    listAdapter.setData(students);
                    listAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } else {
                    listAdapter.setData(new ArrayList<>());
                    listAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ManageStudent.this, "Add Students", Toast.LENGTH_SHORT).show();
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
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(ManageStudent.this, R.layout.classes_list_item, classes);
                    adapter1.setDropDownViewResource(R.layout.classes_list_item);
                    classSpinner.setAdapter(adapter1);
                } else {
                    Toast.makeText(ManageStudent.this, "Add Classes First", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}