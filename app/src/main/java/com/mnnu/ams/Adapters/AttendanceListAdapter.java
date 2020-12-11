package com.mnnu.ams.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mnnu.ams.Module.Attendance;
import com.mnnu.ams.Module.Student;
import com.mnnu.ams.R;

import java.util.List;

public class AttendanceListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Attendance> attendances;

//    public AttendanceListAdapter(Activity activity, ) {
//        this.activity = activity;
//        this.attendances = attendances;
//    }

    @Override
    public int getCount() {
        return attendances.size();
    }

    @Override
    public Object getItem(int i) {
        return attendances.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = activity.getLayoutInflater().inflate(R.layout.student_list_item,null);
        }
        TextView sName = (TextView)view.findViewById(R.id.attendanceList_name);
        TextView sAttendance = (TextView)view.findViewById(R.id.attendanceList_attendance);

        Attendance attendance = attendances.get(i);
//
//        sName.setText(attendance);
//        sAttendance.setText(s.getsRoll());

        return view;
    }
}
