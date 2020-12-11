package com.mnnu.ams.HelperClasses;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.mnnu.ams.Module.Subject;
import com.mnnu.ams.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewCreator {
    private static final String TAG = "mandeep";

    public ViewGroup singleEditText(Context context, int HorizontalMargin, int VerticalMargin, String Hint){
        LinearLayout linearLayout = new LinearLayout(context);
        EditText editText = new EditText(context);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(HorizontalMargin,VerticalMargin,HorizontalMargin,VerticalMargin);
        editText.setLayoutParams(params);
        editText.setHint(Hint);
        linearLayout.addView(editText);
        return linearLayout;
    }

    public ViewGroup newSubjectPromptView(Context context, ArrayList<String> classes){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        EditText editText = new EditText(context);
        editText.setHint("Enter Subject Name");
        Spinner spinner = new Spinner(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(50,20,50,20);
        editText.setLayoutParams(params);
        spinner.setLayoutParams(params);
        linearLayout.addView(editText);
        linearLayout.addView(spinner);
        return linearLayout;

    }

    public ViewGroup newStudentPromptView(Context context, ArrayList<String> classes){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        EditText editText = new EditText(context);
        EditText editText1 = new EditText(context);
        editText.setHint("Enter Student Name");
        editText1.setHint("Enter Roll Number");
        Spinner spinner = new Spinner(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(50,20,50,20);
        editText.setLayoutParams(params);
        editText1.setLayoutParams(params);
        spinner.setLayoutParams(params);
        linearLayout.addView(editText);
        linearLayout.addView(editText1);
        linearLayout.addView(spinner);
        return linearLayout;
    }
    public ViewGroup newStartAttendanceView(final Context context, ArrayList<String> classes, ArrayList<Subject> subjects){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final Spinner spinner = new Spinner(context);
        final Spinner spinner1 = new Spinner(context);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(50,20,50,20);
        spinner.setLayoutParams(params);
        spinner1.setLayoutParams(params);

        final HashMap<String,ArrayList<String>> classSub = new HashMap<>();
        for (Subject s :
                subjects) {
            if(!(classSub.containsKey(s.getClassname()))){
                classSub.put(s.getClassname(),new ArrayList<String>());
            }
            classSub.get(s.getClassname()).add(s.getName());
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,classSub.get(adapterView.getSelectedItem().toString()));
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(adapter1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        linearLayout.addView(spinner);
        linearLayout.addView(spinner1);
        return linearLayout;
    }

    public ViewGroup newAttendanceCheckLayout(Context context){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setWeightSum(2);
        ImageButton button = new ImageButton(context);
        ImageButton button1 = new ImageButton(context);
        button.setImageResource(R.drawable.ic_baseline_check_24);
        button1.setImageResource(R.drawable.ic_baseline_cancel_24);
        button.setBackgroundResource(R.color.colorAccent);
        button1.setBackgroundResource(R.color.colorAccent);

        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(200,150);
        params.setMargins(50,20,50,20);


        button.setLayoutParams(params);
        button1.setLayoutParams(params);
        linearLayout.addView(button);
        linearLayout.addView(button1);
        return linearLayout;
    }

}
