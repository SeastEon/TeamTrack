package com.example.teamtrack;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class Team {
    public int mTeamColor;
    public String mTeamName;
    public int mCurrentPoints;
    public int mTeamIndex;

    public Team(){
        mTeamColor = Color.rgb(1,1,1);
        mTeamName = "Team Name";
        mCurrentPoints = 0;
        mTeamIndex = 0;
    }

    public Team(int TeamColor, String TeamName, List<TeamMember> TeamMembers){
        mTeamColor = TeamColor;
        mTeamName = TeamName;
        mCurrentPoints = 0;
    }

    public static class DatabaseTeam {
        public List<Team> mTeamList;
        public int mCurrentIndex;
        public String mTeamLog;
        public List<TeamMember> mFullListOfMembers;

        DatabaseTeam(){ }
        DatabaseTeam(List<Team>TeamList, int CurrentIndex, String TeamLog, List<TeamMember> FullListOfMembers){
            mTeamList = TeamList;
            mCurrentIndex = CurrentIndex;
            mTeamLog = TeamLog;
            mFullListOfMembers =FullListOfMembers;
        }
    }
}


