package com.mnnu.ams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mnnu.ams.HelperClasses.MyFirebaseManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mandeep";

    private TextInputEditText loginUsername, loginPin;
    private Button login_button;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loginUsername = findViewById(R.id.login_username);
        loginPin = findViewById(R.id.activity_main_pin);
        login_button = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progressBar);

        SharedPreferences preferences = getSharedPreferences("username", MODE_PRIVATE);
        loginUsername.setText(preferences.getString("username", ""));


        login_button.setOnClickListener(view -> {
            if (loginUsername.length() > 3 && loginPin.length() > 3) {
                login_button.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);
                MyFirebaseManager.getInstance()
                        .getUser(loginUsername.getText().toString(),
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            if (dataSnapshot.getValue().toString().equals(loginPin.getText().toString())) {
                                                preferences.edit().putString("username", loginUsername.getText().toString()).commit();
                                                startActivity(new Intent(MainActivity.this, Dashboard.class));
                                                finish();
                                            } else {
                                                login_button.setClickable(true);
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(MainActivity.this, "Wrong Pin", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            login_button.setClickable(true);
                                            Toast.makeText(MainActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
            } else
                Toast.makeText(this, "Min Length: 4", Toast.LENGTH_SHORT).show();
        });
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