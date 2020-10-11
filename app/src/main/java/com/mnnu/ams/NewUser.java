package com.mnnu.ams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class NewUser extends AppCompatActivity {

    private SQLiteDatabase database;
    private EditText username_EditText, pin_EditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        database = openOrCreateDatabase("ams", Context.MODE_PRIVATE, null);
        database.execSQL("create table if not exists teacher(name varchar,pin varchar)");

        username_EditText = (EditText) findViewById(R.id.newTextPersonName);
        pin_EditText = (EditText) findViewById(R.id.newPineditText);
        signUpButton = (Button) findViewById(R.id.signUp_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Cursor cursor = database.rawQuery("select * from teacher where name='" + username_EditText.getText().toString().trim() + "'", null);
                    if (cursor.getCount() != 0) {
                        Snackbar.make(findViewById(R.id.newUserActivityLayout), "Name Already Exists!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        if (pin_EditText.getText().toString().length() == 4) {
                            try {
                                database.execSQL("INSERT INTO teacher VALUES('"
                                        + username_EditText.getText().toString().trim() + "',"
                                        + pin_EditText.getText().toString().trim() + ")");
                                Toast.makeText(NewUser.this, "Data Inserting", Toast.LENGTH_SHORT).show();
                                finish();
                            } catch (Exception e) {
                                Snackbar.make(findViewById(R.id.newUserActivityLayout), "Failed to Insert data. Exception: " + e.toString(), Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(findViewById(R.id.newUserActivityLayout), "Pin Must be of length 4 ", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                    cursor.close();
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.newUserActivityLayout), e.toString(), Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }
}