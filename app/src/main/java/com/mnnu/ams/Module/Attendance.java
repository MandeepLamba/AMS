package com.mnnu.ams.Module;

public class Attendance {
    private String name , roll, classname,subjectname,isPrasent;

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public String getClassname() {
        return classname;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public String getIsPrasent() {
        return isPrasent;
    }

    public Attendance(String name, String roll, String classname, String subjectname, String isPrasent) {
        this.name = name;
        this.roll = roll;
        this.classname = classname;
        this.subjectname = subjectname;
        this.isPrasent = isPrasent;
    }
}
