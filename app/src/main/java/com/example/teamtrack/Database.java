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

    List<TeamMember> GetFullListOfTeamMembers(){
        List<TeamMember> FullListOfTeamMembers = new ArrayList<>();
        for (Team team : LoadedTeams.mTeamList) {FullListOfTeamMembers.addAll(team.mTeamMembers);}
        return FullListOfTeamMembers;
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
            if (!Member.equals("")){
                if (!SReason.equals("")){LogEntry += "because " + Member +" "+ SReason;}
                else{LogEntry += "because of " + Member;}
            } else {
                if (!SReason.equals("")){LogEntry += "because " + Member +" "+ SReason;}
            }
        }
        AddToLog(LogEntry);
        DBChangesMade();
    }

    void AddToLog(String Input){
        LoadedTeams.mTeamLog +=  "\n" + Input;
    }
}
