package com.mnnu.ams;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.mnnu.ams.HelperClasses.MyFirebaseManager;

public class NewUser extends AppCompatActivity {

    private static final String TAG = "mandeep";
    private TextInputEditText emailEditText, pinEditText, nameEditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        emailEditText = findViewById(R.id.new_user_email);
        nameEditText = findViewById(R.id.new_user_name);
        pinEditText = findViewById(R.id.new_user_pin);
        signUpButton = findViewById(R.id.signUp_button);

        signUpButton.setOnClickListener(view -> {
            if ((emailEditText.length() > 3) && (pinEditText.length() > 3) && (nameEditText.length() > 3)) {
                if(nameEditText.getText().toString().contains(".") || nameEditText.getText().toString().contains(" ")){
                    Toast.makeText(this, "Username must not contain space and full stop.", Toast.LENGTH_SHORT).show();
                }
                else {
                    MyFirebaseManager.getInstance().addUser(nameEditText.getText().toString(), emailEditText.getText().toString(), Integer.parseInt(pinEditText.getText().toString()));
                    emailEditText.setText("");
                    nameEditText.setText("");
                    pinEditText.setText("");
                }
            } else
                Toast.makeText(this, "Min Length: 4", Toast.LENGTH_SHORT).show();
        });


    }
}