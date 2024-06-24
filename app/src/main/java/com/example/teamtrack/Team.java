package com.example.teamtrack;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public int mTeamColor;
    public String mTeamName;
    public int mCurrentPoints;
    public List<TeamMember> mTeamMembers;

    public Team(){
        mTeamColor = Color.rgb(1,1,1);
        mTeamName = "Team Name";
        mCurrentPoints = 0;
        List<TeamMember> MemberList = new ArrayList<TeamMember>();
        MemberList.add(new TeamMember("Grey", TeamMember.Position.Coach));
        mTeamMembers = MemberList;
    }

    public Team(int TeamColor, String TeamName, int CurrentPoints, List<TeamMember> TeamMembers){
        mTeamColor = TeamColor;
        mTeamName = TeamName;
        mCurrentPoints = CurrentPoints;
        mTeamMembers = TeamMembers;
    }

    public Team(int TeamColor, String TeamName, List<TeamMember> TeamMembers){
        mTeamColor = TeamColor;
        mTeamName = TeamName;
        mCurrentPoints = 0;
        mTeamMembers = TeamMembers;
    }

}
