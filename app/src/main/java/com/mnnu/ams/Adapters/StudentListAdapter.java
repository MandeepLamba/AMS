package com.mnnu.ams.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mnnu.ams.Module.Student;
import com.mnnu.ams.R;

import java.util.List;

public class StudentListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Student> students;

    public StudentListAdapter(Activity activity, List<Student> students) {
        this.activity = activity;
        this.students = students;
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int i) {
        return students.get(i);
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
        TextView sName = (TextView)view.findViewById(R.id.studentList_name);
        TextView sRoll = (TextView)view.findViewById(R.id.studentList_roll);
        TextView sClass = (TextView)view.findViewById(R.id.studentList_class);

        Student s = students.get(i);

        sName.setText(s.getsName());
        sRoll.setText(s.getsRoll());
        sClass.setText(s.getsClass());

        return view;
    }
}
