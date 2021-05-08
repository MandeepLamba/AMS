package com.mnnu.ams.Module;

public class Subject {
    private String name;
    private String classname;
    private String subjectcode;

    public Subject() {
    }

    public Subject(String name, String classname, String subjectcode) {
        this.name = name;
        this.classname = classname;
        this.subjectcode = subjectcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSubjectcode() {
        return subjectcode;
    }

    public void setSubjectcode(String subjectcode) {
        this.subjectcode = subjectcode;
    }
}
