package com.mnnu.ams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private static final String TAG = "mandeep";

    private EditText loginPin;

    private Button login_button;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginPin = findViewById(R.id.activity_main_pin);
        spinner = (Spinner) findViewById(R.id.spinner);
        login_button = findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Dashboard.class));
//                if (spinner.getSelectedItem().toString().isEmpty() || loginPin.getText().toString().length() < 4) {
//                    Snackbar.make(findViewById(R.id.mainactivitylayout), "Select user and enter Pin!", Snackbar.LENGTH_SHORT).show();
//                } else {
//                    try {
//                        String q = "select * from teacher where name='" + spinner.getSelectedItem().toString().trim() + "' and pin= '"+loginPin.getText().toString()+"'";
//                        Log.d(TAG, "onClick: query: "+q);
//                        Cursor cursor = database.rawQuery(q, null);
//                        if (cursor.getCount() != 1) {
//                            Snackbar.make(findViewById(R.id.mainactivitylayout), "User not found!", Snackbar.LENGTH_SHORT).show();
//                        } else {
//                            cursor.moveToFirst();
//                            Intent intent = new Intent(MainActivity.this, Dashboard.class);
//                            intent.putExtra("name",cursor.getString(0));
//                            startActivity(intent);
//                            finish();
//                        }
//                        cursor.close();
//                    } catch (Exception e) {
//                        Log.d(TAG, "onClick: Database Exception: "+e.toString());
//                        Snackbar.make(findViewById(R.id.mainactivitylayout), "Database Exception!", Snackbar.LENGTH_SHORT).show();
//                    }
//                }
            }
        });


        database = openOrCreateDatabase("ams", Context.MODE_PRIVATE, null);
        database.execSQL("create table if not exists teacher(name varchar,pin varchar)");

        try {
            Cursor cursor = database.rawQuery("select name from teacher", null);
            if (cursor.getCount() < 1) {
                Snackbar.make(findViewById(R.id.mainactivitylayout), "Create A user from Menu!", Snackbar.LENGTH_SHORT).show();
            } else {
                List<String> names = new ArrayList<>();
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    names.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "onCreate: Exception on select names: " + e.toString());
            Snackbar.make(findViewById(R.id.mainactivitylayout), "Database Error!", Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_new_user) {
            startActivity(new Intent(this, NewUser.class));
        }
        return super.onOptionsItemSelected(item);
    }
}