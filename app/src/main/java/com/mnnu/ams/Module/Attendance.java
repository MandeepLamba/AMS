package com.mnnu.ams.Module;

import java.util.ArrayList;
import java.util.HashMap;

public class Attendance {

    private HashMap<String,Object> list;

    public HashMap<String, Object> getList() {
        return list;
    }

    public void setList(HashMap<String, Object> list) {
        this.list = list;
    }

    public Attendance(HashMap<String, Object> list) {
        this.list = list;
    }

    public Attendance() {
    }
}
