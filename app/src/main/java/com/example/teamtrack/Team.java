package com.example.teamtrack;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public int mTeamColor;
    public String mTeamName;
    public int mCurrentPoints;
    public List<TeamMember> mTeamMembers;
    public int mTeamIndex;

    public Team(){
        mTeamColor = Color.rgb(1,1,1);
        mTeamName = "Team Name";
        mCurrentPoints = 0;
        List<TeamMember> MemberList = new ArrayList<>();
        MemberList.add(new TeamMember("Grey", TeamMember.Position.Coach));
        mTeamMembers = MemberList;
        mTeamIndex = 0;
    }

    public Team(int TeamColor, String TeamName, int CurrentPoints, List<TeamMember> TeamMembers, int TeamIndex){
        mTeamColor = TeamColor;
        mTeamName = TeamName;
        mCurrentPoints = CurrentPoints;
        mTeamMembers = TeamMembers;
        mTeamIndex = TeamIndex;
    }

    public Team(int TeamColor, String TeamName, List<TeamMember> TeamMembers){
        mTeamColor = TeamColor;
        mTeamName = TeamName;
        mCurrentPoints = 0;
        mTeamMembers = TeamMembers;
    }

    static void CreateTeam(View view){
        TextView TxtTeamName = view.findViewById(R.id.TextTeamNameID);
        String TeamName = TxtTeamName.getText().toString();

        View Teamcolor = view.findViewById(R.id.ColorView);
        ColorDrawable viewColor = (ColorDrawable) Teamcolor.getBackground();
        int colorId = viewColor.getColor();

        //Team(TeamName, colorId, );
        view.findViewById(R.id.TeamMembersSpinner);

    }

    void AddTeamMember(TeamMember newTeaMember){
        mTeamMembers.add(newTeaMember);
    }

    public static class DatabaseTeam {
        public List<Team> mTeamList;
        public int mCurrentIndex;

        DatabaseTeam(){ }
        DatabaseTeam(List<Team>TeamList, int CurrentIndex){
            mTeamList = TeamList;
            mCurrentIndex = CurrentIndex;
        }
    }
}


