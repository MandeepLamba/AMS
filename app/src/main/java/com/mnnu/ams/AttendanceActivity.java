package com.mnnu.ams;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mnnu.ams.Adapters.StudentListAdapter;
import com.mnnu.ams.HelperClasses.MyFirebaseManager;
import com.mnnu.ams.HelperClasses.ViewCreator;
import com.mnnu.ams.Module.Attendance;
import com.mnnu.ams.Module.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {
    private static final String TAG = "mandeep";

    private ListView listView;
    private FloatingActionButton actionButton;
    private ProgressBar progressBar;
    private List<Student> students;
    private String aClass, aSubject, myEmail;
    private StudentListAdapter listAdapter;
    private HashMap<String, Object> attendanceList;
    private boolean isSpeak = false;
    private boolean haveTextToSpeech = false;
    private FrameLayout buttonLayout;
    private Boolean frameVisible = false;
    private int checkedCount = 0;
    private ImageButton attendanceYes, attendanceNo;
    private TextToSpeech textToSpeech;

    @Override
    protected void onStart() {
        UpdateStudentsList(aClass);
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(getApplicationContext(), "Language Error", Toast.LENGTH_SHORT).show();
                } else {
                    haveTextToSpeech = true;
                    getMenuInflater().inflate(R.menu.sound_menu, menu);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.app_bar_speak) {
            if (haveTextToSpeech) {
                if (isSpeak) {
                    Log.d(TAG, "onOptionsItemSelected: turning off");
                    isSpeak = false;
                    item.setIcon(R.drawable.ic_baseline_volume_off_24);
                } else {
                    Log.d(TAG, "onOptionsItemSelected: turning on");
                    isSpeak = true;
                    item.setIcon(R.drawable.ic_baseline_volume_up_24);
                }
            } else {
                Toast.makeText(this, "Unable To Start Service", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        initViews();

        actionButton.setOnClickListener(view -> {
            ViewCreator creator = new ViewCreator();
            ViewGroup viewGroup = creator.newAttendanceCheckLayout(AttendanceActivity.this);

            attendanceYes = (FloatingActionButton) viewGroup.getChildAt(0);
            attendanceNo = (FloatingActionButton) viewGroup.getChildAt(1);

            if (frameVisible) {
                actionButton.setImageResource(R.drawable.ic_play_arrow);
                buttonLayout.removeAllViews();
                attendanceList = new HashMap<>();
                clearAttendance();
                checkedCount = 0;
                frameVisible = false;
                Checking(false);
            } else {
                actionButton.setImageResource(R.drawable.ic_baseline_cancel_24);
                buttonLayout.addView(viewGroup);
                frameVisible = true;
                Checking(true);
            }
        });

    }

    private void clearAttendance() {
        for (int i = 0; i < students.size(); i++)
            listView.getChildAt(i).setBackgroundColor(Color.WHITE);
    }

    private void initViews() {

        String[] details = (String[]) getIntent().getExtras().get("classSubDetails");
        aClass = details[0];
        aSubject = details[1];

        attendanceList = new HashMap<>();

        listView = findViewById(R.id.attendanceStudentList);
        actionButton = findViewById(R.id.startAttendanceActionButton);
        buttonLayout = findViewById(R.id.buttonframe);
        progressBar = findViewById(R.id.progressBar4);

        SharedPreferences preferences = getSharedPreferences("username", MODE_PRIVATE);
        myEmail = preferences.getString("username", "");

        listAdapter = new StudentListAdapter(this);
        listView.setAdapter(listAdapter);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(details[0]);
        supportActionBar.setSubtitle(details[1]);
    }

    private void Checking(Boolean is) {
        if (is) {
            listView.getChildAt(checkedCount).setBackgroundColor(Color.rgb(200, 200, 200));
            if (isSpeak) {
                textToSpeech.speak(students.get(checkedCount).getRoll(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
            attendanceYes.setOnClickListener(view -> {
                listView.getChildAt(checkedCount).setBackgroundColor(Color.rgb(150, 255, 150));

                addAttendance(checkedCount, true);
                if (checkedCount < students.size() - 1) {
                    checkedCount++;
                    listView.getChildAt(checkedCount).setBackgroundColor(Color.rgb(200, 200, 200));
                    if (isSpeak) {
                        textToSpeech.speak(students.get(checkedCount).getRoll(), TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                } else {
                    saveAttendance();
                    buttonLayout.removeAllViews();
                    actionButton.setImageResource(R.drawable.ic_play_arrow);
                }
            });

            attendanceNo.setOnClickListener(view -> {
                listView.getChildAt(checkedCount).setBackgroundColor(Color.rgb(255, 150, 150));
                addAttendance(checkedCount, false);
                if (checkedCount < students.size() - 1) {
                    checkedCount++;
                    listView.getChildAt(checkedCount).setBackgroundColor(Color.rgb(200, 200, 200));
                    if (isSpeak) {
                        textToSpeech.speak(students.get(checkedCount).getRoll(), TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                } else {
                    saveAttendance();
                    buttonLayout.removeAllViews();
                    actionButton.setImageResource(R.drawable.ic_play_arrow);
                }
            });
        } else {
            listView.getChildAt(checkedCount).setBackgroundColor(Color.WHITE);
        }

    }

    private void addAttendance(int i, boolean b) {
        attendanceList.put(students.get(i).getRoll(), b);
    }

    private void saveAttendance() {
        MyFirebaseManager.getInstance().saveAttendance(myEmail, aClass, aSubject,
                new Attendance(attendanceList),
                aVoid -> {
                    Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    clearAttendance();
                });
    }


    private void UpdateStudentsList(String cName) {
        students = new ArrayList<>();
        MyFirebaseManager.getInstance().getStudents(myEmail, cName, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot stu : dataSnapshot.getChildren()) {
                        Student student = stu.getValue(Student.class);
                        students.add(student);
                    }
                    listAdapter.setData(students);
                    listAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } else {
                    listAdapter.setData(new ArrayList<>());
                    listAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AttendanceActivity.this, "Add Students", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}