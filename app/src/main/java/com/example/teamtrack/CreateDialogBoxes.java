package com.example.teamtrack;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    CreateDialogBoxes(Context currContext, Activity activity){
        mCurrContext = currContext;
        mActivity = activity;
    }
    public void CreateAlertBoxForTeamCreation(int id, String AlertBoxTitle, Database Db) {
        View dialogView = LayoutInflater.from(mActivity).inflate(id, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);

        EditText EditTeamName = dialogView.findViewById(R.id.EditTeamName);

        Button SelectColorBtn = dialogView.findViewById(R.id.SelectColorBtn);
        View ColorView = dialogView.findViewById(R.id.ColorView);
        SelectColorBtn.setOnClickListener(view -> CreateColorPickerBox(ColorView));

        LinearLayout MemberNameTeamLayout = dialogView.findViewById(R.id.MemberNameForTeam);
        List<TeamMember> MembersToAdd = new ArrayList<>();
        List<TeamMember> TeamMembers = Db.GetFullListOfTeamMembers();
        Dictionary<Integer, TeamMember> TeamMemberViews = new Hashtable<>();

        int MemberCount = 1;
        for (TeamMember member: TeamMembers) {
            TextView MemberName = new TextView(dialogView.getContext());
            MemberName.setTextSize(20);
            MemberName.setText(MemberCount + ": " +member.mName); //adds the current member's name to the Label
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
            MemberCount +=1 ;

            alertBuilder.setTitle(AlertBoxTitle)
                .setView(dialogView)
                .setPositiveButton((Html.fromHtml("<font color='#000000'>Confirm</font>", 0)), (dialog, id1) -> {
                    ColorDrawable Color = (ColorDrawable)ColorView.getBackground();
                    Db.LoadedTeams.mTeamList.add(new Team(Color.getColor(), EditTeamName.getText().toString(), MembersToAdd));
                    Db.LoadedTeams.mCurrentIndex = Db.LoadedTeams.mTeamList.size() - 1;
                    fragment_members.Refresh(dialogView, Db);
                    Db.SetCurrentTeam();
                    Db.DBChangesMade(); //should send changes to the database
                })
                .setNegativeButton((Html.fromHtml("<font color='#000000'>Cancel</font>", 0)), (dialog, id1) -> dialog.dismiss());
        alertBuilder.create(); //creates the objects to be used
        alertBuilder.setTitle(AlertBoxTitle); //sets the title the user will see

        }

        AlertDialog dialogAlert = alertBuilder.create();
        dialogAlert.show();
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
}


