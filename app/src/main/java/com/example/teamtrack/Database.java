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
import java.util.List;


public class Database {
    DocumentReference myRef;
    Team LoadedTeams;
    RelativeLayout mMainActivity;
    Database(RelativeLayout mainActivity){mMainActivity = mainActivity; }
    void CreateDatabase() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        myRef = database.collection("BDAF").document("NewTeam");
        List<TeamMember> MemberList = new ArrayList<TeamMember>();
        MemberList.add(new TeamMember("Grey", TeamMember.Position.Coach));
        Team NewTeam = new Team(Color.rgb(2, 8, 7), "GreyTeam", 1000, MemberList);
        // Add a new document with a generated ID
        myRef.set(NewTeam).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    Team SetData(){
        myRef.get().addOnCompleteListener((OnCompleteListener<DocumentSnapshot>) task -> {
            if (task.isSuccessful()) {
                LoadedTeams = task.getResult().toObject(Team.class);
                assert LoadedTeams != null;
                SetCurrentTeam(LoadedTeams);
            }
        });
        return LoadedTeams;
    }

    void SetCurrentTeam(Team CurrTeam){
        TextView TeamNameTxt = mMainActivity.findViewById(R.id.TextTeamNameID);
        TeamNameTxt.setText(CurrTeam.mTeamName);
        TextView PointsTxt = mMainActivity.findViewById(R.id.TextTeamPointsID);
        PointsTxt.setText(Integer.toString(CurrTeam.mCurrentPoints) );
        mMainActivity.setBackgroundColor(CurrTeam.mTeamColor);
        ImageButton CircleButton = mMainActivity.findViewById(R.id.ImageViewMainCircle);
        CircleButton.setBackgroundColor(CurrTeam.mTeamColor);
    }
}
