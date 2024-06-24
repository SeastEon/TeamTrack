package com.example.teamtrack;

public class TeamMember {
    public String mName;
    public enum Position {Coach, Captain, Jumpers}
    public Position mPosition;
    public int mIndividualPoints;

    public TeamMember(){
        mName = "";
        mPosition = Position.Jumpers;
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

    void AddTeamMemberDialog(){

    }
}
