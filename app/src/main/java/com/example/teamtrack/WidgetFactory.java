package com.example.teamtrack;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.Dictionary;
import java.util.List;
import java.util.Locale;

public class WidgetFactory {
    ActivityVariables ThisActivity;
    FragmentManager mFragmentManager;
    boolean ExtraTagsActive = false;
    Dictionary<String, List<String>> mPositionAndMember;
    CreateDialogBoxes mDialogBoxCreator;

    WidgetFactory(View currentView, FragmentManager fragmentManager, Context CurrContext, Activity mainActivity ){
        ThisActivity = new ActivityVariables(currentView, CurrContext, mainActivity);
        mFragmentManager = fragmentManager;
        mDialogBoxCreator = new CreateDialogBoxes(CurrContext, mainActivity);
    }

    void CreateMembersWidgets(fragment_members Fragment_members){
        ImageButton OpenMemberSideBar = ThisActivity.mView.findViewById(R.id.AccessTeamMembers);
        OpenMemberSideBar.setOnClickListener(view -> {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.mainActivity, Fragment_members).setReorderingAllowed(true).addToBackStack(null);
            transaction.commit();
        });
    }

    void CreateSettingsWidgets(){
        Button BtnCreateNewTeam = ThisActivity.mView.findViewById(R.id.CreateTeamBtn);
        BtnCreateNewTeam.setOnClickListener(view -> mDialogBoxCreator.CreateAlertBoxForTeamCreation(R.layout.layout_add_team, "Create New Team"));

        Button BtnEditTeam = ThisActivity.mView.findViewById(R.id.EditTeamBtn);
        BtnEditTeam.setOnClickListener(view -> {

        });

        Button BtnEditAccount= ThisActivity.mView.findViewById(R.id.EditAccount);
        BtnEditAccount.setOnClickListener(view -> {

        });
        Button BtnEditPosition = ThisActivity.mView.findViewById(R.id.EditPosition);
        BtnEditPosition.setOnClickListener(view -> {

        });
        Button BtnNotifications = ThisActivity.mView.findViewById(R.id.Notifications);
        BtnNotifications.setOnClickListener(view -> {

        });
        Button BtnLogouts = ThisActivity.mView.findViewById(R.id.Logout);
        BtnLogouts.setOnClickListener(view -> {

        });

        Button BtnExitSettings = ThisActivity.mView.findViewById(R.id.CloseSettings);
        BtnExitSettings.setOnClickListener(view -> mFragmentManager.popBackStack());
    }

    @SuppressLint("ClickableViewAccessibility")
    void CreateMainButtons(RelativeLayout mainLayout, settings SettingsFragment){

        LinearLayout ScrollViewLinearLayout = ThisActivity.mView.findViewById(R.id.BottomNavigationID);
        ScrollView ScrollViewContainingLinearLayout = ThisActivity.mView.findViewById(R.id.ScrollViewMainActivity);

        //We want to remember the layout of the scrollview so we can reset the values
        ViewGroup.LayoutParams ScrollParams = ScrollViewContainingLinearLayout.getLayoutParams();

        //Giving spinners their arrays
        Spinner NumberSpinner = ThisActivity.mView.findViewById(R.id.SpinnerInputNumberID);
        ArrayAdapter<CharSequence> numberAdapter=ArrayAdapter.createFromResource(ThisActivity.mContext, R.array.IncreamentValues, android.R.layout.simple_spinner_item);
        numberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        NumberSpinner.setAdapter(numberAdapter);

        //Buttons
        ImageButton AddSingleButton = ThisActivity.mView.findViewById(R.id.ImageViewMainCircle);
        AddSingleButton.setOnClickListener(view -> {
            TextView CurrentTeamPointValue = ThisActivity.mView.findViewById(R.id.TextTeamPointsID);
            CurrentTeamPointValue.setText(String.format(Locale.getDefault(),"%d",Integer.parseInt(CurrentTeamPointValue.getText().toString()) + 1));
        });

        ImageButton AddDropDownBoxToPoints = ThisActivity.mView.findViewById(R.id.ImageBtnAddID);
        AddDropDownBoxToPoints.setOnClickListener(view -> {
            TextView CurrentTeamPointValue = ThisActivity.mView.findViewById(R.id.TextTeamPointsID);
            CurrentTeamPointValue.setText(String.format(Locale.getDefault(),"%d",Integer.parseInt(CurrentTeamPointValue.getText().toString()) + Integer.parseInt(NumberSpinner.getSelectedItem().toString())));
        });

        ImageButton SubtractDropDownBoxFromPoints = ThisActivity.mView.findViewById(R.id.ImageBtnSubtractID);
        SubtractDropDownBoxFromPoints.setOnClickListener(view -> {
            TextView CurrentTeamPointValue = ThisActivity.mView.findViewById(R.id.TextTeamPointsID);
            CurrentTeamPointValue.setText(String.format(Locale.getDefault(),"%d",Integer.parseInt(CurrentTeamPointValue.getText().toString()) - Integer.parseInt(NumberSpinner.getSelectedItem().toString())));
        });

        ImageButton OpenExtraTagsMenuBtn = ThisActivity.mView.findViewById(R.id.ImageBtnOpenMoreTagsMenu);
        OpenExtraTagsMenuBtn.setOnClickListener(view -> {
            if (!ExtraTagsActive){AddExtraTags.CreateExtraTagsBox(ThisActivity.mContext, mainLayout, ThisActivity.mView); ExtraTagsActive = true;}
            else {
                //We subtract 1 from the child count because we need to 0 index and the count gets an not indexed number
                ScrollViewLinearLayout.removeViewAt(ScrollViewLinearLayout.getChildCount()-1);
                ScrollParams.height = 380;
                ScrollViewContainingLinearLayout.setLayoutParams(ScrollParams);
                ExtraTagsActive = false;
            }
        });

        ImageButton OpenSettingsBtn = ThisActivity.mView.findViewById(R.id.SettingsBtn);
        OpenSettingsBtn.setOnClickListener(view -> {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.mainActivity, SettingsFragment).setReorderingAllowed(true).addToBackStack(null);
            transaction.commit();
        });


        mainLayout.setOnTouchListener(new OnSwipeTouchListener(ThisActivity.mContext) {
            @Override
            public void onSwipeLeft() {

            }

            @Override
            public void onSwipeRight() {

            }
        });

    }



}
