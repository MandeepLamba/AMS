package com.mnnu.ams.Module;

import androidx.annotation.NonNull;

public class Student {

    private String name;
    private String roll;
    private String classname;


    public Student() {
    }


    public Student(String name, String roll, String classname) {
        this.name = name;
        this.roll = roll;
        this.classname = classname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: " + name + ", Roll: " + roll + ", class: " + classname;
    }
}
