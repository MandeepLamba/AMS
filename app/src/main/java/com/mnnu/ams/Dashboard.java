package com.mnnu.ams;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Bundle bundle = getIntent().getExtras();
        TextView textView = findViewById(R.id.nametext);
        textView.setText(bundle.getString("name"));
        getSupportActionBar().setTitle(bundle.getString("name"));
    }
}