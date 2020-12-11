package com.mnnu.ams.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mnnu.ams.Module.Attendance;
import com.mnnu.ams.Module.Student;
import com.mnnu.ams.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AttendanceListAdapter extends BaseAdapter {

    private static final String TAG = "mandeep";

    private Activity activity;
    private HashMap<String,Float> fAttend;
    private ArrayList<String> keys;

    public AttendanceListAdapter(Activity activity, HashMap<String,Float> fAttend) {
        this.activity = activity;
        this.fAttend = fAttend;
        keys = new ArrayList<>();
        keys.addAll(fAttend.keySet());
        Log.d(TAG, "AttendanceListAdapter: started");
        
    }

    @Override
    public int getCount() {
        return fAttend.size();
    }

    @Override
    public Object getItem(int i) {
        return keys.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = activity.getLayoutInflater().inflate(R.layout.attendance_list_item,null);
        }
        Float aFloat = fAttend.get(getItem(i));
        if(aFloat>=90){
            view.setBackgroundResource(R.color.great);
        }
        else if(aFloat>=75){
            view.setBackgroundResource(R.color.safe);
        }
        else{
            view.setBackgroundResource(R.color.danger);
        }

        TextView sName = view.findViewById(R.id.attendanceList_name);
        TextView sAttendance = view.findViewById(R.id.attendanceList_attendance);

        Log.d(TAG, "getView: key:"+getItem(i).toString() + "value: "+aFloat.toString());
        sName.setText(getItem(i).toString());
        Double r = Math.round(aFloat*100)/100.0;
        sAttendance.setText(r.toString());

        return view;
    }


}
