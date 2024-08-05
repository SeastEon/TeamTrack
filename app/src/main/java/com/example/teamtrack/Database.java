package com.example.teamtrack;

import static android.content.ContentValues.TAG;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public Dictionary<String, List<String>> mPositionsAndMembers;

    Database(RelativeLayout mainActivity){
        mMainActivity = mainActivity;
    }

    void CreateDatabase() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        myRef = database.collection("BDAF").document("NewTeam");
    }

    void DBChangesMade(){myRef.set(LoadedTeams).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));}

    Team.DatabaseTeam SetData(WidgetFactory widgetFactory){
        myRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                LoadedTeams = task.getResult().toObject(Team.DatabaseTeam.class);
                assert LoadedTeams != null;
                SetCurrentTeam(); //gets the first team
                widgetFactory.mPositionAndMember = mPositionsAndMembers;
                fm = new fragment_members(this);
                widgetFactory.CreateMembersWidgets(fm);
            }
        }); return LoadedTeams;
    }

    ArrayList<String> GetSpinnerArray (Team.DatabaseTeam dbTeam){
        mSpinnerArray = new ArrayList<>();
        for (Team team: dbTeam.mTeamList) {mSpinnerArray.add(team.mTeamName);}
        return mSpinnerArray;
    }

    ArrayList<String> GetSpinnerArray (List<TeamMember> Members){
        mSpinnerArray = new ArrayList<>();
        for (TeamMember members: Members) {mSpinnerArray.add(members.mName);}
        return mSpinnerArray;
    }

    void SetCurrentTeam(){
        mPositionsAndMembers = new Hashtable<>();
        Team CurrTeam = LoadedTeams.mTeamList.get(LoadedTeams.mCurrentIndex);

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

        List<TeamMember>TeamMembers = getMembersOfTeam(CurrTeam.mTeamName);

        for (TeamMember member:TeamMembers) {
            switch(member.mPosition){
                case Junior: JuniorList.add(member.mName); break;
                case Senior: SeniorList.add(member.mName); break;
                case Captain: CaptainList.add(member.mName); break;
                case Coach: CoachList.add(member.mName); break;
                case Head_Coach: HeadCoachList.add(member.mName); break;
            }

        }

        //Adds all the members to the dictionary for further use
        mPositionsAndMembers.put("Junior", JuniorList);
        mPositionsAndMembers.put("Senior", SeniorList);
        mPositionsAndMembers.put("Captain", CaptainList);
        mPositionsAndMembers.put("Coach", CoachList);
        mPositionsAndMembers.put("Head Coach", HeadCoachList);
    }

    List<TeamMember> getMembersOfTeam(String TeamName){
        List<TeamMember> members = new ArrayList<>();
        for (TeamMember member: LoadedTeams.mFullListOfMembers) {
            if (member.mTeamName.equals(TeamName)){members.add(member); }
        } return members;
    }

    void CreateLogText(Boolean Add, int Value, Boolean ExtraTagsActive, ActivityVariables Av) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String AddOrSub;
        if (Add){ AddOrSub =  ": Add ";}  else {  AddOrSub =  ": Minus ";}

        String LogEntry = date + AddOrSub + Value + " from " + LoadedTeams.mTeamList.get(LoadedTeams.mCurrentIndex).toString();
        if (ExtraTagsActive){
            EditText TxtReason = Av.mView.findViewById(R.id.TextReasonID);
            Spinner JumperSpinner = Av.mView.findViewById(R.id.SpinnerJumperID);
            String SReason = TxtReason.getText().toString();
            String Member = JumperSpinner.getSelectedItem().toString();
            if (!Member.isEmpty()){
                if (!SReason.isEmpty()){LogEntry += "because " + Member +" "+ SReason;}
                else{LogEntry += "because of " + Member;}
            } else {
                if (!SReason.isEmpty()){LogEntry += "because " + Member +" "+ SReason;}
            }
        }
        AddToLog(LogEntry);
        DBChangesMade();
    }

    void AddToLog(String Input){
        LoadedTeams.mTeamLog +=  "\n" + Input;
    }
}
