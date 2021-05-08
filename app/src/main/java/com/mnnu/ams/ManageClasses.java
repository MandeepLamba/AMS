package com.mnnu.ams;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mnnu.ams.HelperClasses.MyFirebaseManager;
import com.mnnu.ams.HelperClasses.ViewCreator;

import java.util.ArrayList;

public class ManageClasses extends AppCompatActivity {
    private static final String TAG = "mandeep";

    private ListView listView;
    private ArrayList<String> classes;
    private ProgressBar progressBar;
    private String myEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_classes);
        getSupportActionBar().setTitle("Classes");

        SharedPreferences preferences = getSharedPreferences("username", MODE_PRIVATE);
        myEmail = preferences.getString("username", "");


        listView = findViewById(R.id.classesListView);
        UpdateClassesList();

        progressBar = findViewById(R.id.progressBar2);

        //adding new class
        findViewById(R.id.addClassFloatingButton).setOnClickListener(view -> {
            ViewCreator creator = new ViewCreator();
            final ViewGroup viewGroup = creator.singleEditText(ManageClasses.this, 50, 20, "Enter Class Name");

            final AlertDialog.Builder builder = new AlertDialog.Builder(ManageClasses.this);
            builder.setView(viewGroup);
            builder.setTitle("Create Class");
            builder.setPositiveButton("Create", (dialogInterface, i) -> {
                String cName = ((EditText) viewGroup.getChildAt(0)).getText().toString();
                if (!cName.equals("")) {
                    MyFirebaseManager.getInstance().createClass(myEmail, cName);
                } else {
                    Toast.makeText(ManageClasses.this, "Empty Class Name!", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(ManageClasses.this, "Cancelled", Toast.LENGTH_SHORT).show()).show();
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ManageClasses.this, R.layout.classes_list_item, classes);
                    listView.setAdapter(arrayAdapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(ManageClasses.this, "Add Classes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}