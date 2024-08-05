package com.example.teamtrack;

public class TeamMember {
    public String mName;
    public String mlog;
    public String mTeamName;
    public enum Position {Junior, Senior, Captain, Coach, Head_Coach}
    public Position mPosition;
    public int mIndividualPoints;

    public TeamMember(){
        mName = "";
        mPosition = Position.Junior;
        mIndividualPoints = 0;
        mTeamName = "";
        mlog = "";
    }

    public TeamMember(String Name, Position Position, int IndividualPoints, String TeamName){
        mName = Name;
        mPosition = Position;
        mIndividualPoints = IndividualPoints;
        mTeamName = TeamName;
        mlog = "";
    }

    public TeamMember(String Name, Position Position, String TeamName){
        mName = Name;
        mPosition = Position;
        mIndividualPoints = 0;
        mTeamName = TeamName;
        mlog = "";
    }

    public void addToMemberLog(String LogEntry){
        mlog += LogEntry;
    }
}
