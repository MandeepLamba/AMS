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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mnnu.ams.HelperClasses.ViewCreator;

import java.util.ArrayList;

public class ManageClasses extends AppCompatActivity {
    private static final String TAG = "mandeep";

    private FloatingActionButton addClassButton;
    private SQLiteDatabase database;
    private ListView listView;
    private ArrayList<String> classes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_classes);
        getSupportActionBar().setTitle("Classes");

        database = openOrCreateDatabase("ams", Context.MODE_PRIVATE, null);
        database.setForeignKeyConstraintsEnabled(true);

        listView = findViewById(R.id.classesListView);
        UpdateClassesList();


        addClassButton = findViewById(R.id.addClassFloatingButton);
        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCreator creator = new ViewCreator();
                final ViewGroup viewGroup = creator.singleEditText(ManageClasses.this, 50,20, "Enter Class Name");

                final AlertDialog.Builder builder = new AlertDialog.Builder(ManageClasses.this);
                builder.setView(viewGroup);
                builder.setTitle("Create Class");
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String cName =((EditText)viewGroup.getChildAt(0)).getText().toString();
                        if(!cName.equals("")){
                            createClass(cName);
                        }
                        else {
                            Toast.makeText(ManageClasses.this, "Empty Class Name!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ManageClasses.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ManageClasses.this, "Long Click to Delete!", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final String classname = classes.get(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageClasses.this);
                builder.setTitle("Delete?");
                builder.setMessage("All Students of this Class will also be removed.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int r = database.delete("class","name=?", new String[]{classname});
                        UpdateClassesList();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ManageClasses.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).show();

                return true;
            }
        });
    }

    private void createClass(String cName) {
        ContentValues values = new ContentValues();
        values.put("name",cName);
        if(database.insert("class",null,values)!=-1){
            Log.d(TAG, "createClass: Class created: "+cName);
            UpdateClassesList();
        }
        else{
            Toast.makeText(this, "Creating Class failed!", Toast.LENGTH_SHORT).show();
        }

    }

    private void UpdateClassesList() {
        classes = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM class",null);
        while (cursor.moveToNext()){
            classes.add(cursor.getString(0));
        }
        cursor.close();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.classes_list_item,classes);
        listView.setAdapter(arrayAdapter);
    }
}