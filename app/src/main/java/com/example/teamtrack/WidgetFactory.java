package com.example.teamtrack;

import android.content.Context;
import android.credentials.CreateCredentialException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class WidgetFactory {

    View mCurrentview;
    FragmentManager mfragmentManager;
    boolean ExtraTagsActive = false;

    WidgetFactory(View currentView, FragmentManager fragmentManager){
        mCurrentview = currentView;
        mfragmentManager = fragmentManager;
    }

    void CreateSettingsWidgets(){
        Button BtnEditTeam = mCurrentview.findViewById(R.id.EditTeamBtn);
        BtnEditTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button BtnEditAccount= mCurrentview.findViewById(R.id.EditAccount);
        BtnEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Button BtnEditPosition = mCurrentview.findViewById(R.id.EditPosition);
        BtnEditPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Button BtnNotifications = mCurrentview.findViewById(R.id.Notifications);
        BtnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Button BtnLogouts = mCurrentview.findViewById(R.id.Logout);
        BtnLogouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button BtnExitSettings = mCurrentview.findViewById(R.id.CloseSettings);
        BtnExitSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new settings();
                mfragmentManager.popBackStack();
            }
        });
    }

    void CreateMainButtons(Context mainContext, AddExtraTags addExtraTags, RelativeLayout mainLayout){

        LinearLayout ScrollViewLinearLayout = mCurrentview.findViewById(R.id.BottomNavigationID);
        ScrollView ScrollViewContainingLinearLayout = mCurrentview.findViewById(R.id.ScrollViewMainActivity);

        //We want to remember the layout of the scrollview so we can reset the values
        ViewGroup.LayoutParams ScrollParams = ScrollViewContainingLinearLayout.getLayoutParams();
        int ScrollViewTop = ScrollViewContainingLinearLayout.getTop();

        //Giving spinners their arrays
        Spinner NumberSpinner = mCurrentview.findViewById(R.id.SpinnerInputNumberID);
        ArrayAdapter<CharSequence> numberAdapter=ArrayAdapter.createFromResource(mainContext, R.array.IncreamentValues, android.R.layout.simple_spinner_item);
        numberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        NumberSpinner.setAdapter(numberAdapter);

        //Buttons
        ImageButton AddSingleButton = mCurrentview.findViewById(R.id.ImageViewMainCircle);
        AddSingleButton.setOnClickListener(view -> {
            TextView CurrentTeamPointValue = mCurrentview.findViewById(R.id.TextTeamPointsID);
            CurrentTeamPointValue.setText(Integer.toString(Integer.parseInt(CurrentTeamPointValue.getText().toString()) + 1));
        });

        ImageButton AddDropDownBoxToPoints = mCurrentview.findViewById(R.id.ImageBtnAddID);
        AddDropDownBoxToPoints.setOnClickListener(view -> {
            TextView CurrentTeamPointValue = mCurrentview.findViewById(R.id.TextTeamPointsID);
            Spinner NumberSpinner12 = (Spinner) mCurrentview.findViewById(R.id.SpinnerInputNumberID);
            CurrentTeamPointValue.setText(Integer.toString(Integer.parseInt(CurrentTeamPointValue.getText().toString()) + Integer.parseInt(NumberSpinner12.getSelectedItem().toString())));
        });

        ImageButton SubtractDropDownBoxFromPoints = mCurrentview.findViewById(R.id.ImageBtnSubtractID);
        SubtractDropDownBoxFromPoints.setOnClickListener(view -> {
            TextView CurrentTeamPointValue = mCurrentview.findViewById(R.id.TextTeamPointsID);
            Spinner NumberSpinner1 = (Spinner) mCurrentview.findViewById(R.id.SpinnerInputNumberID);
            CurrentTeamPointValue.setText(Integer.toString(Integer.parseInt(CurrentTeamPointValue.getText().toString()) - Integer.parseInt(NumberSpinner1.getSelectedItem().toString())));
        });

        ImageButton OpenExtraTagsMenuBtn = mCurrentview.findViewById(R.id.ImageBtnOpenMoreTagsMenu);
        OpenExtraTagsMenuBtn.setOnClickListener(view -> {
            if (!ExtraTagsActive){addExtraTags.CreateExtraTagsBox(mainContext, mainLayout); ExtraTagsActive = true;}
            else {
                //We subtract 1 from the child count because we need to 0 index and the count gets an unindexed number
                ScrollViewLinearLayout.removeViewAt(ScrollViewLinearLayout.getChildCount()-1);
                ScrollParams.height = 240;
                ScrollViewContainingLinearLayout.setLayoutParams(ScrollParams);
                ExtraTagsActive = false;
            }
        });

        ImageButton OpenSettingsBtn = mCurrentview.findViewById(R.id.SettingsBtn);
        OpenSettingsBtn.setOnClickListener(view -> {
            Fragment fragment = new settings();
            FragmentTransaction transaction = mfragmentManager.beginTransaction();
            transaction.replace(R.id.mainActivity, fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null);
            transaction.commit();
        });
    }
}
