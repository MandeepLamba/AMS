package com.mnnu.ams.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mnnu.ams.Module.Subject;
import com.mnnu.ams.R;

import java.util.List;

public class SubjectListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Subject> subjects;

    public SubjectListAdapter(Activity activity, List<Subject> subjects) {
        this.activity = activity;
        this.subjects = subjects;
    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Object getItem(int i) {
        return subjects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.subject_list_item, null);
        }
        TextView sName = view.findViewById(R.id.subjectList_name);
        TextView sClass = view.findViewById(R.id.subjectList_classname);
        TextView sCode = view.findViewById(R.id.subjectList_code);

        Subject s = subjects.get(i);

        sName.setText(s.getName());
        sClass.setText(s.getClassname());
        sCode.setText(s.getSubjectcode());

        return view;
    }
}
