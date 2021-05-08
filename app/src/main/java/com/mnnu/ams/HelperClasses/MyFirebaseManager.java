package com.mnnu.ams.HelperClasses;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mnnu.ams.Module.Attendance;
import com.mnnu.ams.Module.Student;
import com.mnnu.ams.Module.Subject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyFirebaseManager {

    private DatabaseReference root;
    private DatabaseReference usersRoot;
    private static final String TAG = "mandeep";
    private static MyFirebaseManager inst = null;

    private MyFirebaseManager() {
        root = FirebaseDatabase.getInstance().getReference();
        usersRoot = root.child("users");
    }

    public static MyFirebaseManager getInstance() {
        if (inst == null)
            inst = new MyFirebaseManager();
        return inst;
    }

    public void addUser(String username, String email, int pin) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", username);
        data.put("email", email);
        data.put("pin", pin);
        usersRoot.child(username).setValue(data)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "addUser: Successfully added"))
                .addOnFailureListener(e -> Log.d(TAG, "addUser: Failed due to : " + e.getMessage()));
    }

    public void getUser(String username, ValueEventListener listener) {
        // Read from the database
        usersRoot.child(username).child("pin").addListenerForSingleValueEvent(listener);
    }

    public void createClass(String username, String className) {
        usersRoot.child(username).child("classes").push().setValue(className);
    }

    public void getClasses(String username, ValueEventListener listener) {
        usersRoot.child(username).child("classes").addListenerForSingleValueEvent(listener);
    }

    public void addStudent(String username, Student student, OnSuccessListener<Void> successListener) {
        usersRoot.child(username).child("students").child(student.getClassname()).child(student.getRoll()).setValue(student).addOnSuccessListener(successListener);
    }

    public void getStudents(String username, String className, ValueEventListener listener) {
        usersRoot.child(username).child("students").child(className).addListenerForSingleValueEvent(listener);
    }

    public void deleteStudent(String username, String roll, String className, OnSuccessListener<Void> successListener) {
        usersRoot.child(username).child("students").child(className).child(roll).removeValue().addOnSuccessListener(successListener);
    }

    public void addSubject(String username, Subject subject, OnSuccessListener<Void> successListener) {
        usersRoot.child(username).child("subjects").child(subject.getSubjectcode()).setValue(subject).addOnSuccessListener(successListener);
    }

    public void getSubjects(String username, ValueEventListener listener) {
        usersRoot.child(username).child("subjects").addListenerForSingleValueEvent(listener);
    }

    public void saveAttendance(String username, String className, String subName, Attendance attendance, OnSuccessListener<Void> successListener) {
        usersRoot.child(username).child("attendance").child(className).child(subName).child(String.valueOf(Calendar.getInstance().getTimeInMillis()))
                .setValue(attendance).addOnSuccessListener(successListener);
    }

    public void getAttendance(String username, String className, String subName, ValueEventListener listener) {
        usersRoot.child(username).child("attendance").child(className).child(subName).addListenerForSingleValueEvent(listener);
    }

}
