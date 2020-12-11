package com.mnnu.ams.Module;

public class Student {

    private String sName;
    private String sRoll;
    private String sClass;

    public Student(String sName, String sRoll, String sClass) {
        this.sName = sName;
        this.sRoll = sRoll;
        this.sClass = sClass;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsRoll() {
        return sRoll;
    }

    public void setsRoll(String sRoll) {
        this.sRoll = sRoll;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }
}
