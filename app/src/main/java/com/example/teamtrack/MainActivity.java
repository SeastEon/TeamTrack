package com.example.teamtrack;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    AddExtraTags addExtraTags = new AddExtraTags();
    boolean ExtraTagsActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context MainContext = this.getApplicationContext();

        //Loads the elements we want to used
        RelativeLayout MainLayout = findViewById(R.id.mainActivity);
        LinearLayout ScrollViewLinearLayout = findViewById(R.id.BottomNavigationID);
        ScrollView ScrollViewContainingLinearLayout = findViewById(R.id.ScrollViewMainActivity);

        //Database Creation
        Database db = new Database(MainLayout);
        db.CreateDatabase();

        //Sets the information from the database
        Team Teams = db.SetData();

        //We want to remember the layout of the scrollview so we can reset the values
        ViewGroup.LayoutParams ScrollParams = ScrollViewContainingLinearLayout.getLayoutParams();
        int ScrollViewTop = ScrollViewContainingLinearLayout.getTop();

        //Giving spinners their arrays
        Spinner NumberSpinner = findViewById(R.id.SpinnerInputNumberID);
        ArrayAdapter<CharSequence> numberAdapter=ArrayAdapter.createFromResource(this, R.array.IncreamentValues, android.R.layout.simple_spinner_item);
        numberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        NumberSpinner.setAdapter(numberAdapter);

        //Buttons
        ImageButton AddSingleButton = findViewById(R.id.ImageViewMainCircle);
        AddSingleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView CurrentTeamPointValue = findViewById(R.id.TextTeamPointsID);
                CurrentTeamPointValue.setText(Integer.toString(Integer.parseInt(CurrentTeamPointValue.getText().toString()) + 1));
            }
        });

        ImageButton AddDropDownBoxToPoints = findViewById(R.id.ImageBtnAddID);
        AddDropDownBoxToPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView CurrentTeamPointValue = findViewById(R.id.TextTeamPointsID);
                Spinner NumberSpinner = (Spinner) findViewById(R.id.SpinnerInputNumberID);
                CurrentTeamPointValue.setText(Integer.toString(Integer.parseInt(CurrentTeamPointValue.getText().toString()) + Integer.parseInt(NumberSpinner.getSelectedItem().toString())));
            }
        });

        ImageButton SubtractDropDownBoxFromPoints = findViewById(R.id.ImageBtnSubtractID);
        SubtractDropDownBoxFromPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView CurrentTeamPointValue = findViewById(R.id.TextTeamPointsID);
                Spinner NumberSpinner = (Spinner) findViewById(R.id.SpinnerInputNumberID);
                CurrentTeamPointValue.setText(Integer.toString(Integer.parseInt(CurrentTeamPointValue.getText().toString()) - Integer.parseInt(NumberSpinner.getSelectedItem().toString())));
            }
        });

        ImageButton OpenExtraTagsMenuBtn = findViewById(R.id.ImageBtnOpenMoreTagsMenu);
        OpenExtraTagsMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ExtraTagsActive){addExtraTags.CreateExtraTagsBox(MainContext, MainLayout); ExtraTagsActive = true;}
                else {
                    //We subtract 1 from the child count because we need to 0 index and the count gets an unindexed number
                    ScrollViewLinearLayout.removeViewAt(ScrollViewLinearLayout.getChildCount()-1);
                    ScrollParams.height = 240;
                    ScrollViewContainingLinearLayout.setLayoutParams(ScrollParams);
                    ExtraTagsActive = false;
                }
            }
        });
    }
}