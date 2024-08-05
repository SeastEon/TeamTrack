package com.example.teamtrack;

import static com.example.teamtrack.fragment_members.CreateAddMemberDialogBox;

import android.annotation.SuppressLint;
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
    ActivityVariables mAv;
    FragmentManager mFragmentManager;
    boolean ExtraTagsActive = false;
    Dictionary<String, List<String>> mPositionAndMember;
    CreateDialogBoxes mDialogBoxCreator;
    Database mDb;
    WidgetFactory mself ;

    WidgetFactory(ActivityVariables Av, FragmentManager fragmentManager, Database Db ){
        mAv = new ActivityVariables(Av.mView, Av.mContext, Av.mActivity);
        mFragmentManager = fragmentManager;
        mDialogBoxCreator = new CreateDialogBoxes(Av.mContext, Av.mActivity);
        mDb = Db;
        mself = this;
    }

    void CreateMembersWidgets(fragment_members Fragment_members){
        ImageButton OpenMemberSideBar = mAv.mView.findViewById(R.id.AccessTeamMembers);
        OpenMemberSideBar.setOnClickListener(view -> {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.mainActivity, Fragment_members).setReorderingAllowed(true).addToBackStack(null);
            transaction.commit();
        });
    }

    void CreateSettingsWidgets(){
        Button BtnAddMember = mAv.mView.findViewById(R.id.AddTeamMember);
        BtnAddMember.setOnClickListener(view -> CreateAddMemberDialogBox(R.layout.layout_add_team_members, mAv, mDb));

        Button BtnCreateNewTeam = mAv.mView.findViewById(R.id.CreateTeamBtn);
        BtnCreateNewTeam.setOnClickListener(view -> mDialogBoxCreator.CreateAlertBoxForTeamCreation(R.layout.layout_add_team, "Create New Team", mDb));

        Button BtnEditTeam = mAv.mView.findViewById(R.id.EditTeamBtn);
        BtnEditTeam.setOnClickListener(view -> mDialogBoxCreator.CreateEditTeamBox(R.layout.layout_edit_teams, mDb));

        Button BtnEditAccount= mAv.mView.findViewById(R.id.EditAccount);
        BtnEditAccount.setOnClickListener(view -> {

        });
        Button BtnEditPosition = mAv.mView.findViewById(R.id.EditPosition);
        BtnEditPosition.setOnClickListener(view -> {

        });
        Button BtnNotifications = mAv.mView.findViewById(R.id.Notifications);
        BtnNotifications.setOnClickListener(view -> {

        });
        Button BtnLogouts = mAv.mView.findViewById(R.id.Logout);
        BtnLogouts.setOnClickListener(view -> {

        });

        Button BtnExitSettings = mAv.mView.findViewById(R.id.CloseSettings);
        BtnExitSettings.setOnClickListener(view -> mFragmentManager.popBackStack());
    }

    @SuppressLint("ClickableViewAccessibility")
    void CreateMainButtons(RelativeLayout mainLayout, settings SettingsFragment){

        LinearLayout ScrollViewLinearLayout = mAv.mView.findViewById(R.id.BottomNavigationID);
        ScrollView ScrollViewContainingLinearLayout = mAv.mView.findViewById(R.id.ScrollViewMainActivity);

        //We want to remember the layout of the scrollview so we can reset the values
        ViewGroup.LayoutParams ScrollParams = ScrollViewContainingLinearLayout.getLayoutParams();

        //Giving spinners their arrays
        Spinner NumberSpinner = mAv.mView.findViewById(R.id.SpinnerInputNumberID);
        ArrayAdapter<CharSequence> numberAdapter=ArrayAdapter.createFromResource(mAv.mContext, R.array.IncreamentValues, android.R.layout.simple_spinner_item);
        numberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        NumberSpinner.setAdapter(numberAdapter);

        //Buttons
        ImageButton AddSingleButton = mAv.mView.findViewById(R.id.ImageViewMainCircle);
        AddSingleButton.setOnClickListener(view -> {
            TextView CurrentTeamPointValue = mAv.mView.findViewById(R.id.TextTeamPointsID);
            int ReturnValue = Integer.parseInt(CurrentTeamPointValue.getText().toString()) + 1;
            CurrentTeamPointValue.setText(String.format(Locale.getDefault(),"%d",Integer.parseInt(CurrentTeamPointValue.getText().toString()) + 1));
            mDb.AddToLog("1 Point added to" + mDb.LoadedTeams.mTeamList.get(mDb.LoadedTeams.mCurrentIndex).toString() ) ;
            mDb.LoadedTeams.mTeamList.get(mDb.LoadedTeams.mCurrentIndex).mCurrentPoints = ReturnValue;
            mDb.DBChangesMade();
        });

        ImageButton AddDropDownBoxToPoints = mAv.mView.findViewById(R.id.ImageBtnAddID);
        AddDropDownBoxToPoints.setOnClickListener(view -> {
            TextView CurrentTeamPointValue = mAv.mView.findViewById(R.id.TextTeamPointsID);
            int AddValue = Integer.parseInt(NumberSpinner.getSelectedItem().toString());
            int ReturnValue = Integer.parseInt(CurrentTeamPointValue.getText().toString()) + AddValue;
            CurrentTeamPointValue.setText(String.format(Locale.getDefault(),"%d",ReturnValue));
            mDb.LoadedTeams.mTeamList.get(mDb.LoadedTeams.mCurrentIndex).mCurrentPoints = ReturnValue;
            mDb.CreateLogText(true, AddValue, ExtraTagsActive,mAv);
        });

        ImageButton SubtractDropDownBoxFromPoints = mAv.mView.findViewById(R.id.ImageBtnSubtractID);
        SubtractDropDownBoxFromPoints.setOnClickListener(view -> {
            TextView CurrentTeamPointValue = mAv.mView.findViewById(R.id.TextTeamPointsID);
            int SubtractionValue = Integer.parseInt(NumberSpinner.getSelectedItem().toString());
            int ReturnValue = Integer.parseInt(CurrentTeamPointValue.getText().toString()) - SubtractionValue;
            CurrentTeamPointValue.setText(String.format(Locale.getDefault(),"%d",ReturnValue));
            mDb.LoadedTeams.mTeamList.get(mDb.LoadedTeams.mCurrentIndex).mCurrentPoints = ReturnValue;
            mDb.CreateLogText(false, SubtractionValue, ExtraTagsActive,mAv);
        });

        ImageButton OpenExtraTagsMenuBtn = mAv.mView.findViewById(R.id.ImageBtnOpenMoreTagsMenu);
        OpenExtraTagsMenuBtn.setOnClickListener(view -> {
            if (!ExtraTagsActive){
                AddExtraTags.CreateExtraTagsBox(mAv.mContext, mainLayout, mDb.GetSpinnerArray(mDb.LoadedTeams.mFullListOfMembers));
                ExtraTagsActive = true;
            }
            else {
                //We subtract 1 from the child count because we need to 0 index and the count gets an not indexed number
                ScrollViewLinearLayout.removeViewAt(ScrollViewLinearLayout.getChildCount()-1);
                ScrollParams.height = 380;
                ScrollViewContainingLinearLayout.setLayoutParams(ScrollParams);
                ExtraTagsActive = false;
            }
        });

        ImageButton OpenSettingsBtn = mAv.mView.findViewById(R.id.SettingsBtn);
        OpenSettingsBtn.setOnClickListener(view -> {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.mainActivity, SettingsFragment).setReorderingAllowed(true).addToBackStack(null);
            transaction.commit();
        });

        mainLayout.setOnTouchListener(new OnSwipeTouchListener(mAv.mContext) {
            @Override
            public void onSwipeLeft() {
                if (mDb.LoadedTeams.mTeamList.size() != 1) {
                    if (mDb.LoadedTeams.mCurrentIndex == 0) {
                            mDb.LoadedTeams.mCurrentIndex = mDb.LoadedTeams.mTeamList.size() - 1;
                        mDb.SetCurrentTeam();
                    } else {
                        mDb.LoadedTeams.mCurrentIndex -= 1; //pushes the index back by one
                        mDb.SetCurrentTeam();
                    }
                }
            }

            @Override
            public void onSwipeRight() {
                if (mDb.LoadedTeams.mTeamList.size() != 1) {
                    if (mDb.LoadedTeams.mCurrentIndex == mDb.LoadedTeams.mTeamList.size() - 1) {
                        mDb.LoadedTeams.mCurrentIndex = 0;
                        mDb.SetCurrentTeam();
                    } else {
                        mDb.LoadedTeams.mCurrentIndex += 1; //pushes the index back by one
                        mDb.SetCurrentTeam();
                    }
                }
            }
        });
    }
}
