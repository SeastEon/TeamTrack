package com.example.teamtrack;

import static android.content.ContentValues.TAG;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;


public class Database {
    DocumentReference myRef;
    Team.DatabaseTeam LoadedTeams;
    RelativeLayout mMainActivity;
    ArrayList<String> mSpinnerArray;
    fragment_members fm;
    WidgetFactory mWidgetFactory;

    public Dictionary<String, List<String>> mPositionsAndMembers;
    Database(RelativeLayout mainActivity, WidgetFactory widgetFactory){
        mMainActivity = mainActivity;
        mWidgetFactory = widgetFactory;
    }

    void CreateDatabase() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        myRef = database.collection("BDAF").document("NewTeam");
        List<TeamMember> MemberList = new ArrayList<>();
        MemberList.add(new TeamMember("Grey", TeamMember.Position.Coach));
        Team NewTeam = new Team(Color.rgb(2, 8, 7), "GreyTeam", 1000, MemberList, 0);
        List<Team> TeamList = new ArrayList<>();
        TeamList.add(NewTeam);
        Team.DatabaseTeam dbTeam = new Team.DatabaseTeam(TeamList, 0);
        myRef.set(dbTeam).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    Team.DatabaseTeam SetData(WidgetFactory widgetFactory){
        myRef.get().addOnCompleteListener((OnCompleteListener<DocumentSnapshot>) task -> {
            if (task.isSuccessful()) {
                LoadedTeams = task.getResult().toObject(Team.DatabaseTeam.class);
                assert LoadedTeams != null;
                SetCurrentTeam(LoadedTeams); //gets the first team
                widgetFactory.mPositionAndMember = mPositionsAndMembers;
                fm = new fragment_members(this);
                mWidgetFactory.CreateMembersWidgets(fm);
            }
        }); return LoadedTeams;
    }

    ArrayList<String> GetSpinnerArray (Team.DatabaseTeam dbTeam){
        mSpinnerArray = new ArrayList<>();
        for (Team team: dbTeam.mTeamList) {mSpinnerArray.add(team.mTeamName);}
        return mSpinnerArray;
    }

    void SetCurrentTeam(Team.DatabaseTeam dbTeam){
        mPositionsAndMembers = new Hashtable<>();
        Team CurrTeam = dbTeam.mTeamList.get(dbTeam.mCurrentIndex);

        TextView TeamNameTxt = mMainActivity.findViewById(R.id.TextTeamNameID);
        TeamNameTxt.setText(CurrTeam.mTeamName);

        TextView PointsTxt = mMainActivity.findViewById(R.id.TextTeamPointsID);
        PointsTxt.setText(String.format(String.format(Locale.getDefault(),"%d", CurrTeam.mCurrentPoints)));

        mMainActivity.setBackgroundColor(CurrTeam.mTeamColor);

        ImageButton CircleButton = mMainActivity.findViewById(R.id.ImageViewMainCircle);
        CircleButton.setBackgroundColor(CurrTeam.mTeamColor);

        //Creates the lists need to store the jumpers
        List<String> HeadCoachList = new ArrayList<>();
        List<String> CoachList = new ArrayList<>();
        List<String> CaptainList = new ArrayList<>();
        List<String> SeniorList = new ArrayList<>();
        List<String> JuniorList = new ArrayList<>();

        //this will add the jumpers from the database to list organised by their Position
        for (TeamMember mTeamMember : CurrTeam.mTeamMembers) {
            switch(mTeamMember.mPosition){
                case Junior: JuniorList.add(mTeamMember.mName); break;
                case Senior: SeniorList.add(mTeamMember.mName); break;
                case Captain: CaptainList.add(mTeamMember.mName); break;
                case Coach: CoachList.add(mTeamMember.mName); break;
                case Head_Coach: HeadCoachList.add(mTeamMember.mName); break;
            }
        }
        //Adds all the members to the dictionary for further use
        mPositionsAndMembers.put("Junior", JuniorList);
        mPositionsAndMembers.put("Senior", SeniorList);
        mPositionsAndMembers.put("Captain", CaptainList);
        mPositionsAndMembers.put("Coach", CoachList);
        mPositionsAndMembers.put("Head Coach", HeadCoachList);
    }

    static Dictionary<String, List<String>> SetCurrentTeamStaticVer(Team.DatabaseTeam dbTeam){
        Dictionary<String, List<String>> PositionsAndMembers = new Hashtable<>();
        Team CurrTeam = dbTeam.mTeamList.get(dbTeam.mCurrentIndex);

        //Creates the lists need to store the jumpers
        List<String> HeadCoachList = new ArrayList<>();
        List<String> CoachList = new ArrayList<>();
        List<String> CaptainList = new ArrayList<>();
        List<String> SeniorList = new ArrayList<>();
        List<String> JuniorList = new ArrayList<>();

        //this will add the jumpers from the database to list organised by their Position
        for (TeamMember mTeamMember : CurrTeam.mTeamMembers) {
            switch(mTeamMember.mPosition){
                case Junior: JuniorList.add(mTeamMember.mName); break;
                case Senior: SeniorList.add(mTeamMember.mName); break;
                case Captain: CaptainList.add(mTeamMember.mName); break;
                case Coach: CoachList.add(mTeamMember.mName); break;
                case Head_Coach: HeadCoachList.add(mTeamMember.mName); break;
            }
        }
        //Adds all the members to the dictionary for further use
        PositionsAndMembers.put("Junior", JuniorList);
        PositionsAndMembers.put("Senior", SeniorList);
        PositionsAndMembers.put("Captain", CaptainList);
        PositionsAndMembers.put("Coach", CoachList);
        PositionsAndMembers.put("Head Coach", HeadCoachList);

        return PositionsAndMembers;
    }
}
