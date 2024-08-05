package com.example.teamtrack;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public class fragment_members extends Fragment {
    ActivityVariables mAv;
    Database mDb;
    public fragment_members( Database db) {mDb = db;}

    public fragment_members() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        mAv = new ActivityVariables(inflater.inflate(R.layout.fragment_members, container, false), this.getContext(),getActivity());

         TextView MembersText = mAv.mView.findViewById(R.id.MembersTxtView);
         MembersText.setText(CreateTextDataFromDictionary(mDb.mPositionsAndMembers));

         ImageView AddMemberButton = mAv.mView.findViewById(R.id.AddMember);
         AddMemberButton.setOnClickListener(view -> CreateAddMemberDialogBox(R.layout.layout_add_team_members, mAv, mDb));
         mAv.mView.setOnTouchListener(new OnSwipeTouchListener(mAv.mContext) {
             @Override
             public void onSwipeLeft() {closeFragment();}});
         return mAv.mView;
    }

     static Spanned CreateTextDataFromDictionary(Dictionary<String, List<String>> PositionAndMembers){
        Enumeration<String> DictKeys = PositionAndMembers.keys();
        StringBuilder PositionAndMembersString = new StringBuilder(); String LastElement = "";

        while (DictKeys.hasMoreElements()){
            String Element = DictKeys.nextElement();
            if (!Objects.equals(Element, LastElement)){PositionAndMembersString.append("<b>").append(Element).append("</b><br>");}
            for (int i =0; i < PositionAndMembers.get(Element).size(); i++ ){PositionAndMembersString.append(PositionAndMembers.get(Element).get(i)).append("<br>");}
            LastElement = Element;
        }
         return Html.fromHtml(PositionAndMembersString.toString(), 0);
    }

    static public void CreateAddMemberDialogBox(int id, ActivityVariables mAv, Database mDb) {
        View dialogView = LayoutInflater.from(mAv.mActivity).inflate(id, null);

        EditText JumperName = dialogView.findViewById(R.id.NewMemberJumperName);

        Spinner JumperPosition = dialogView.findViewById(R.id.TeamPositionSpinner);
        ArrayAdapter<CharSequence> PositionAdapter = ArrayAdapter.createFromResource(mAv.mContext, R.array.Positions, android.R.layout.simple_spinner_item);
        PositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        JumperPosition.setAdapter(PositionAdapter);

        Spinner JumperTeam = dialogView.findViewById(R.id.TeamSpinner);
        ArrayAdapter<String> TeamAdapter = new ArrayAdapter<>(mAv.mContext, android.R.layout.simple_spinner_item, mDb.GetSpinnerArray(mDb.LoadedTeams));
        JumperTeam.setAdapter(TeamAdapter);

        ImageButton AddMemberToPending = dialogView.findViewById(R.id.ImageBtnAddMemberID);
        TextView PendingMemberPosition = dialogView.findViewById(R.id.TxtAddedMemberPosition);
        TextView PendingMemberName = dialogView.findViewById(R.id.TxtAddedMemberNames);
        TextView PendingMemberTeam = dialogView.findViewById(R.id.TxtAddedMemberTeam);


        AddMemberToPending.setOnClickListener(view -> {
            PendingMemberName.setText(JumperName.getText().toString());
            PendingMemberPosition.setText(JumperPosition.getSelectedItem().toString());
            PendingMemberTeam.setText(JumperTeam.getSelectedItem().toString());
        });

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mAv.mActivity)
                .setView(dialogView)
                .setPositiveButton((Html.fromHtml("<font color='#000000'>Confirm Pending</font>", 0)), (dialog, id1) -> SetTeamMember(PendingMemberPosition.getText().toString(),PendingMemberTeam.getText().toString(), PendingMemberName.getText().toString(), mDb, mAv.mView))
                .setNegativeButton((Html.fromHtml("<font color='#000000'>Cancel</font>", 0)), (dialog, id1) -> dialog.dismiss());
        alertBuilder.create(); //creates the objects to be used
        alertBuilder.show();
    }

    static void SetTeamMember(String MemberPosition, String MemberTeam, String MemberName, Database Db, View mView){
        Db.LoadedTeams.mFullListOfMembers.add(new TeamMember( MemberName, TeamMember.Position.valueOf(MemberPosition), MemberTeam)) ;
        Refresh(mView, Db);
    }

    static void Refresh(View mView, Database mDb ){
        if (mView.findViewById(R.id.MembersTxtView) != null){
            TextView MembersText = mView.findViewById(R.id.MembersTxtView);
            mDb.SetCurrentTeam();
            MembersText.setText(CreateTextDataFromDictionary(mDb.mPositionsAndMembers));
            mDb.DBChangesMade(); //should send changes to the database
        }
        mDb.SetCurrentTeam();
        mDb.DBChangesMade(); //should send changes to the database
    }

    private void closeFragment() {requireActivity().getOnBackPressedDispatcher().onBackPressed();}
}