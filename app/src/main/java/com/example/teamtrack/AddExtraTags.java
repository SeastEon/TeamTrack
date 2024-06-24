package com.example.teamtrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class AddExtraTags extends AppCompatActivity {

    void CreateExtraTagsBox(Context mainObjectContext, RelativeLayout mainLayout){
        //loads up the elements we will be accessing
        LinearLayout ScrollViewMainLayout = mainLayout.findViewById(R.id.BottomNavigationID);
        ScrollView ScrollViewContainingLinearLayout = mainLayout.findViewById(R.id.ScrollViewMainActivity);

        //Creates the dialog view
        View dialogView =LayoutInflater.from(mainObjectContext).inflate(R.layout.layout_extra_tags, null);
        ScrollViewMainLayout.addView(dialogView, ScrollViewMainLayout.getChildCount());

        //Changes the height of the scroll view
        ViewGroup.LayoutParams ScrollParams = ScrollViewContainingLinearLayout.getLayoutParams();
        ScrollParams.height = 500;
        ScrollViewContainingLinearLayout.setLayoutParams(ScrollParams);

        //Creates the spinner data
        Spinner JumperSpinner = dialogView.findViewById(R.id.SpinnerJumperID);
        ArrayAdapter<CharSequence> jumperAdapter=ArrayAdapter.createFromResource(mainObjectContext, R.array.CurrentTeamNames, android.R.layout.simple_spinner_item);
        jumperAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        JumperSpinner.setAdapter(jumperAdapter);
        }

}
