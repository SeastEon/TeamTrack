package com.example.teamtrack;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    AddExtraTags addExtraTags = new AddExtraTags();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context MainContext = this.getApplicationContext();
        RelativeLayout MainLayout = findViewById(R.id.mainActivity);

        //gets the roots view for main
        View rootView = getWindow().getDecorView().getRootView();

        //Creates the widgets for current view
        WidgetFactory widgetFactory = new WidgetFactory(rootView, getSupportFragmentManager());
        widgetFactory.CreateMainButtons(MainContext, addExtraTags, MainLayout);

        //Database Creation
        Database db = new Database(MainLayout);
        db.CreateDatabase();

        //Sets the information from the database
        Team Teams = db.SetData();
    }
}