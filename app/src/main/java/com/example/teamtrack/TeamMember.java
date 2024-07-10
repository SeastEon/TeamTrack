package com.example.teamtrack;

public class TeamMember {
    public String mName;
    public String mlog;
    public enum Position {Junior, Senior, Captain, Coach, Head_Coach}
    public Position mPosition;
    public int mIndividualPoints;

    public TeamMember(){
        mName = "";
        mPosition = Position.Junior;
        mIndividualPoints = 0;
        mlog = "";
    }

    public TeamMember(String Name, Position Position, int IndividualPoints ){
        mName = Name;
        mPosition = Position;
        mIndividualPoints = IndividualPoints;
        mlog = "";
    }

    public TeamMember(String Name, Position Position){
        mName = Name;
        mPosition = Position;
        mIndividualPoints = 0;
        mlog = "";
    }

    public void addToMemberLog(String LogEntry){
        mlog += LogEntry;
    }
}
