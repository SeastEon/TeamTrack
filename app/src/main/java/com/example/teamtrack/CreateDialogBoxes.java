package com.example.teamtrack;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class CreateDialogBoxes {
    Context mCurrContext;
    Activity mActivity;
    CreateDialogBoxes(Context currContext, Activity activity){
        mCurrContext = currContext;
        mActivity = activity;
    }
     public void CreateAlertBoxForTeamCreation(int id, String AlertBoxTitle) {
        View dialogView = LayoutInflater.from(mActivity).inflate(id, null);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity)
                .setTitle(AlertBoxTitle)
                .setView(dialogView)
                .setPositiveButton((Html.fromHtml("<font color='#000000'>Confirm</font>", 0)), (dialog, id1) -> {})
                .setNegativeButton((Html.fromHtml("<font color='#000000'>Cancel</font>", 0)), (dialog, id1) -> dialog.dismiss());
        alertBuilder.create(); //creates the objects to be used
        alertBuilder.setTitle(AlertBoxTitle); //sets the title the user will see

        Button SelectColorBtn = dialogView.findViewById(R.id.SelectColorBtn);
        View ColorView = dialogView.findViewById(R.id.ColorView);
        SelectColorBtn.setOnClickListener(view -> CreateColorPickerBox(ColorView));

        AlertDialog dialogAlert = alertBuilder.create();
        dialogAlert.show();
    }
    public void CreateColorPickerBox(View ColorView){
        new ColorPickerDialog.Builder(mCurrContext)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton((Html.fromHtml("<font color='#000000'>Confirm</font>", 0)), (ColorEnvelopeListener) (envelope, fromUser) -> ColorView.setBackgroundColor(envelope.getColor()))
            .setNegativeButton((Html.fromHtml("<font color='#000000'>Cancel</font>", 0)), (dialogInterface, i) -> dialogInterface.dismiss())
            .attachAlphaSlideBar(true) // the default value is true.
            .attachBrightnessSlideBar(true)  // the default value is true.
            .setBottomSpace(12) // set a bottom space between the last sidebar and buttons.
                .show();
    }
}


