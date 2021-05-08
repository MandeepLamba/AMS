package com.mnnu.ams;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mnnu.ams.Adapters.AttendanceListAdapter;
import com.mnnu.ams.HelperClasses.MyFirebaseManager;
import com.mnnu.ams.Module.Attendance;
import com.mnnu.ams.Module.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ViewAttendance extends AppCompatActivity {

    private static final String TAG = "mandeep";
    private Map<Long, Attendance> attendances;
    private Spinner classesSpinner, subjectSpinner;
    private BarChart barChart;
    private ProgressBar progressBar;
    private ListView listView;
    private HashMap<String, ArrayList<String>> classSub;
    private AttendanceListAdapter attendanceListAdapter;
    private String selectedClass = "";
    private String myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        getSupportActionBar().setTitle("Attendance");

        SharedPreferences preferences = getSharedPreferences("username", MODE_PRIVATE);
        myEmail = preferences.getString("username", "");


        progressBar = findViewById(R.id.progressBar5);
        classesSpinner = findViewById(R.id.classesSpinner);
        subjectSpinner = findViewById(R.id.subjectSpinner);
        barChart = findViewById(R.id.attendanceChart);

        listView = findViewById(R.id.attendanceListView);

        attendanceListAdapter = new AttendanceListAdapter(this);
        listView.setAdapter(attendanceListAdapter);

        UpdateSubjectList();

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getAttendance(classesSpinner.getSelectedItem().toString(), parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void showSoundPrompt() {
        Toast.makeText(this, "Sound Settings", Toast.LENGTH_SHORT).show();
    }

    private void UpdateSubjectList() {
        MyFirebaseManager.getInstance().getSubjects(myEmail, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    classSub = new HashMap<>();
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Subject subject = s.getValue(Subject.class);
                        if (!classSub.containsKey(subject.getClassname())) {
                            classSub.put(subject.getClassname(), new ArrayList<>());
                        }
                        classSub.get(subject.getClassname()).add(subject.getName());
                    }
                    if (classSub.size() > 0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewAttendance.this, R.layout.classes_list_item, new ArrayList<>(classSub.keySet()));
                        adapter.setDropDownViewResource(R.layout.classes_list_item);
                        classesSpinner.setAdapter(adapter);
                        classesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                selectedClass = adapterView.getSelectedItem().toString();
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(ViewAttendance.this, R.layout.classes_list_item, classSub.get(adapterView.getSelectedItem().toString()));
                                adapter1.setDropDownViewResource(R.layout.classes_list_item);
                                subjectSpinner.setAdapter(adapter1);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        Toast.makeText(ViewAttendance.this, "Add Classes First", Toast.LENGTH_SHORT).show();
                    }
//                    if (subjects.size() > 0) haveSubs = true;
                } else {
                    Toast.makeText(ViewAttendance.this, "Add Subjects", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAttendance(String className, String subName) {
        MyFirebaseManager.getInstance().getAttendance(myEmail, className, subName, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    attendances = new TreeMap<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        attendances.put(Long.valueOf(snapshot.getKey()), snapshot.getValue(Attendance.class));
                    }
                    showAttendance();
                } else {
                    Toast.makeText(ViewAttendance.this, "No Attendance Found", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showAttendance() {
        ArrayList<BarEntry> barEntriesArrayList = new ArrayList<>();

        TreeMap<Long, Float> listdata = new TreeMap<>();
        float count = 0;
        for (Map.Entry<Long, Attendance> entry : attendances.entrySet()) {
            float countTotal = entry.getValue().getList().size();
            float presentTotal = 0;
            for (Object o : entry.getValue().getList().values()) if ((boolean) o) presentTotal++;
            barEntriesArrayList.add(new BarEntry(count++, (presentTotal / countTotal * 100f)));
            listdata.put(entry.getKey(), (presentTotal / countTotal * 100f));
        }
        attendanceListAdapter.setData(listdata);


        BarDataSet barDataSet = new BarDataSet(barEntriesArrayList, "Attendance");
        barDataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(barDataSet);
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(true);

        barChart.setData(barData);
        barChart.setFitBars(false);
        barChart.invalidate();
        progressBar.setVisibility(View.INVISIBLE);


    }


}