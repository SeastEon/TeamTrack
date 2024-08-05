package com.example.teamtrack;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public class CreateDialogBoxes {
    Context mCurrContext;
    Activity mActivity;
    List<TeamMember> MembersToAdd;

    CreateDialogBoxes(Context currContext, Activity activity){
        mCurrContext = currContext;
        mActivity = activity;
    }

    public void CreateAlertBoxForTeamCreation(int id, String AlertBoxTitle, @NonNull Database Db) {
        View dialogView = LayoutInflater.from(mActivity).inflate(id, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);

        EditText EditTeamName = dialogView.findViewById(R.id.EditTeamName);

        Button SelectColorBtn = dialogView.findViewById(R.id.SelectColorBtn);
        View ColorView = dialogView.findViewById(R.id.ColorView);
        SelectColorBtn.setOnClickListener(view -> CreateColorPickerBox(ColorView));
        LinearLayout MemberNameTeamLayout = dialogView.findViewById(R.id.MemberNameForTeam);
        MembersToAdd = new ArrayList<>(); //We reset the list of members to add so when they are reference we wont get values from another function
        SetMemberScrollBar(MemberNameTeamLayout, Db.LoadedTeams.mFullListOfMembers);

        alertBuilder.setTitle(AlertBoxTitle)
            .setView(dialogView)
            .setPositiveButton((Html.fromHtml("<font color='#000000'>Confirm</font>", 0)), (dialog, id1) -> {
                Db.LoadedTeams.mTeamList.add(new Team( ((ColorDrawable)ColorView.getBackground()).getColor(), EditTeamName.getText().toString(), MembersToAdd));
                Db.LoadedTeams.mCurrentIndex = Db.LoadedTeams.mTeamList.size() - 1;
                //Sets the members team to the team
                for (TeamMember member: MembersToAdd) {member.mTeamName = EditTeamName.getText().toString();}
                fragment_members.Refresh(dialogView, Db);
                Db.SetCurrentTeam();
                Db.DBChangesMade(); //should send changes to the database
            })
            .setNegativeButton((Html.fromHtml("<font color='#000000'>Cancel</font>", 0)), (dialog, id1) -> dialog.dismiss());
        alertBuilder.setTitle(AlertBoxTitle); //sets the title the user will see

        AlertDialog dialogAlert = alertBuilder.create();
        dialogAlert.show();
    }

    //creates objects to allow the user to add and remove members for a linear layout inside of a scroll bar
    void SetMemberScrollBar(LinearLayout MemberNameTeamLayout, List<TeamMember> TeamMembers){
        Dictionary<Integer, TeamMember> TeamMemberViews = new Hashtable<>();
        int MemberCount = 1;
        for (TeamMember member: TeamMembers) {
            TextView MemberName = new TextView(MemberNameTeamLayout.getContext());
            MemberName.setTextSize(20);
            MemberName.setText(MemberCount + ": " + member.mName); //adds the current member's name to the Label
            TeamMemberViews.put(MemberCount, member);
            MemberName.setOnTouchListener(new OnSwipeTouchListener(mActivity) {
                @Override
                public void onSwipeLeft() {
                    String MemberString = MemberName.getText().toString();
                    int MemberIndex = Integer.parseInt(MemberString.split(":")[0]);
                    MemberName.setText(MemberIndex +": " + Objects.requireNonNull(TeamMemberViews.get(MemberIndex)).mName);
                    MembersToAdd.remove(TeamMemberViews.get(MemberIndex));
                }
                @Override
                public void onSwipeRight(){
                    String MemberString = MemberName.getText().toString();
                    int MemberIndex = Integer.parseInt(MemberString.split(":")[0]);
                    if (!MembersToAdd.contains(TeamMemberViews.get(MemberIndex))) {
                        MemberName.setText( MemberName.getText().toString() + ", Added");
                        MembersToAdd.add(TeamMemberViews.get(MemberIndex));
                    }
                }
            });
            MemberNameTeamLayout.addView(MemberName);
            MemberCount +=1;
        }
    }

    public void CreateColorPickerBox(View ColorView){
        new ColorPickerDialog.Builder(mCurrContext)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton((Html.fromHtml("<font color='#000000'>Confirm</font>", 0)), (ColorEnvelopeListener) (envelope, fromUser) -> ColorView.setBackgroundColor(envelope.getColor()))
            .setNegativeButton((Html.fromHtml("<font color='#000000'>Cancel</font>", 0)), (dialogInterface, i) -> dialogInterface.dismiss())
            .attachAlphaSlideBar(true) // the default value is true.
            .attachBrightnessSlideBar(true)  // the default value is true.
            .setBottomSpace(12) // set a bottom space between the last sidebar and buttons.
                .show();
    }

    public void CreateEditTeamBox(int LayoutID, Database Db){
        View dialogView = LayoutInflater.from(mActivity).inflate(LayoutID, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        Team CurrTeam = null;

        Spinner TeamSelect = dialogView.findViewById(R.id.SelectTeamToEdit);
        ArrayAdapter<String> TeamAdapter = new ArrayAdapter<>(mCurrContext, android.R.layout.simple_spinner_item, Db.GetSpinnerArray(Db.LoadedTeams));
        TeamSelect.setAdapter(TeamAdapter);

        TeamSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CreateEditTeamSectionBasedOnSelectedTeam(dialogView, (String) adapterView.getSelectedItem(), Db);}

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

        LinearLayout EditTeamExpansion =  dialogView.findViewById(R.id.ExpansionHolder);
        ScrollView EditTeamScroll = (ScrollView) EditTeamExpansion.getChildAt(2);

        for (Team currteam: Db.LoadedTeams.mTeamList) { if (currteam.mTeamName.equals(TeamSelect.getSelectedItem())){CurrTeam = currteam;}}
        if (CurrTeam != null){MembersToAdd =  Db.getMembersOfTeam(CurrTeam.mTeamName);} //We reset the list of members to add so when they are reference we wont get values from another function

        int TeamIndex = Db.LoadedTeams.mTeamList.indexOf(CurrTeam);
        alertBuilder.setTitle("Edit Team")
                .setView(dialogView)
                .setPositiveButton((Html.fromHtml("<font color='#000000'>Confirm</font>", 0)), (dialog, id1) -> {
                    Db.LoadedTeams.mTeamList.get(TeamIndex).mTeamName = (((EditText)EditTeamExpansion.getChildAt(0)).getText().toString());
                    Db.LoadedTeams.mTeamList.get(TeamIndex).mTeamColor = ((ColorDrawable)(((LinearLayout)(EditTeamExpansion.getChildAt(1))).getChildAt(1)).getBackground()).getColor();
                    for (TeamMember member: MembersToAdd) {member.mTeamName = Db.LoadedTeams.mTeamList.get(TeamIndex).mTeamName;}
                    fragment_members.Refresh(dialogView, Db);
                    Db.SetCurrentTeam();
                    Db.DBChangesMade(); //should send changes to the database
                })
                .setNegativeButton((Html.fromHtml("<font color='#000000'>Cancel</font>", 0)), (dialog, id1) -> dialog.dismiss());
        AlertDialog dialogAlert = alertBuilder.create();
        dialogAlert.show();
    }

    void CreateEditTeamSectionBasedOnSelectedTeam(View CurrentView, String SelectedItem, Database Db){
        Team CurrTeam = null;
        //Loops through the teams looking for a team with the name passed in
        for (Team currteam: Db.LoadedTeams.mTeamList) { if (currteam.mTeamName.equals(SelectedItem)){CurrTeam = currteam;} }

        //Access linearLayout from view so we can add items to the layout
        LinearLayout EditTeamLayout = CurrentView.findViewById(R.id.ExpansionHolder);
        EditTeamLayout.setOrientation(LinearLayout.VERTICAL);
        EditText TeamNameET =  (EditText) EditTeamLayout.getChildAt(0);
        if (TeamNameET == null){
            //We create the holder for the team name and set it's value
            EditText TeamName = new EditText(mCurrContext);
            assert CurrTeam != null; TeamName.setText(CurrTeam.mTeamName);
            EditTeamLayout.addView(TeamName); //0 in Edit Team LinearLayout

            // we create a layout to hold the a view containing the color and a button to change the color
            LinearLayout ColorLayout = getLinearLayout(CurrTeam);
            EditTeamLayout.addView(ColorLayout); //1 in Edit Team LinearLayout

            //We create the scroll view and set up the linearlayout to contain the full list of members
            ScrollView memberView = new ScrollView(mCurrContext);
            LinearLayout MembersForScroll = new LinearLayout(mCurrContext);
            MembersForScroll.setOrientation(LinearLayout.VERTICAL);
            SetMemberScrollBar( MembersForScroll, Db.LoadedTeams.mFullListOfMembers);
            memberView.addView(MembersForScroll);
            EditTeamLayout.addView(memberView); //2 in Edit Team LinearLayout
        } else{
            assert CurrTeam != null;
            TeamNameET.setText(CurrTeam.mTeamName);
            ((LinearLayout)EditTeamLayout.getChildAt(1)).getChildAt(1).setBackgroundColor(CurrTeam.mTeamColor);
        }
    }

    private @NonNull LinearLayout getLinearLayout(Team CurrTeam) {
        LinearLayout ColorLayout = new LinearLayout(mCurrContext);
        ColorLayout.setOrientation(LinearLayout.HORIZONTAL);

        //We create a view and set the color of said view to the team inputted
        View ColorView = new View(mCurrContext);
        ColorView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        ColorView.setBackgroundColor(CurrTeam.mTeamColor);

        //We create the button to enable to user to change the team color
        Button BtnChangeColor = new Button(mCurrContext);
        BtnChangeColor.setText("Change Team Color");
        BtnChangeColor.setOnClickListener(view -> CreateColorPickerBox(ColorView));

        //We add the views to the layout
        ColorLayout.addView(BtnChangeColor);
        ColorLayout.addView(ColorView);
        return ColorLayout;
    }
}


