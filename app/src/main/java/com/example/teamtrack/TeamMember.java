package com.example.teamtrack;

public class TeamMember {
    public String mName;
    public enum Position {Junior, Senior, Captain, Coach, Head_Coach}
    public Position mPosition;
    public int mIndividualPoints;

    public TeamMember(){
        mName = "";
        mPosition = Position.Junior;
        mIndividualPoints = 0;
    }

    public TeamMember(String Name, Position Position, int IndividualPoints ){
        mName = Name;
        mPosition = Position;
        mIndividualPoints = IndividualPoints;
    }

    public TeamMember(String Name, Position Position){
        mName = Name;
        mPosition = Position;
        mIndividualPoints = 0;
    }
}
