package com.mnnu.ams.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mnnu.ams.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

public class AttendanceListAdapter extends BaseAdapter {

    private static final String TAG = "mandeep";

    private Activity activity;
    private TreeMap<Long, Float> fAttend;
    private ArrayList<Long> keys;

    public AttendanceListAdapter(Activity activity) {
        this.activity = activity;
        this.fAttend = new TreeMap<>();
        keys = new ArrayList<>();
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
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.attendance_list_item, null);
        }
        Float aFloat = fAttend.get(getItem(i));
        if (aFloat >= 90) {
            view.setBackgroundResource(R.color.great);
        } else if (aFloat >= 75) {
            view.setBackgroundResource(R.color.safe);
        } else {
            view.setBackgroundResource(R.color.danger);
        }

        TextView sName = view.findViewById(R.id.attendanceList_name);
        TextView sAttendance = view.findViewById(R.id.attendanceList_attendance);

        sName.setText(new SimpleDateFormat("dd MMM").format(new Date(Long.valueOf((Long) getItem(i)))));
//        sName.setText(fAttend.get((Long)getItem(i)));
        Double r = Math.round(aFloat * 100) / 100.0;
        sAttendance.setText(r.toString());

        return view;
    }

    public void setData(TreeMap<Long, Float> attendances) {
        fAttend = attendances;
        keys.addAll(fAttend.keySet());
        Collections.sort(keys);
        notifyDataSetChanged();
    }
}
