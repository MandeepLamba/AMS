package com.mnnu.ams.Module;

public class Subject {
    private String name;
    private String classname;

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

    public Subject(String name, String classname) {
        this.name = name;
        this.classname = classname;
    }
}
