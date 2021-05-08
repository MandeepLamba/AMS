package com.mnnu.ams.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mnnu.ams.Module.Student;
import com.mnnu.ams.R;

import java.util.ArrayList;
import java.util.List;

public class StudentListAdapter extends BaseAdapter implements Filterable {

    private Activity activity;
    private List<Student> studentListFiltered, studentList;

    public StudentListAdapter(Activity activity) {
        this.activity = activity;
        this.studentList = new ArrayList<>();
        this.studentListFiltered = new ArrayList<>();
    }

    public void setData(List<Student> students) {
        this.studentList = students;
        this.studentListFiltered = students;
    }

    @Override
    public int getCount() {
        return studentListFiltered.size();
    }

    @Override
    public Object getItem(int i) {
        return studentListFiltered.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.student_list_item, null);
        }
        TextView sName = (TextView) view.findViewById(R.id.studentList_name);
        TextView sRoll = (TextView) view.findViewById(R.id.studentList_roll);
        TextView sClass = (TextView) view.findViewById(R.id.studentList_class);

        Student s = studentListFiltered.get(i);

        sName.setText(s.getName());
        sRoll.setText(s.getRoll());
        sClass.setText(s.getClassname());

        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = studentList.size();
                    filterResults.values = studentList;

                } else {
                    List<Student> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for (Student stu : studentList) {
                        if (stu.getName().toLowerCase().contains(searchStr)
                                || stu.getClassname().toLowerCase().contains(searchStr)
                                || stu.getRoll().toLowerCase().contains(searchStr)) {
                            resultsModel.add(stu);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                studentListFiltered = (List<Student>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
