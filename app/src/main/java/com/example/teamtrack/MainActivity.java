package com.example.teamtrack;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    ActivityVariables mAv; //holds the context view and activity for main
    // private final int CurrTeamIndex = 1; Team CurrTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAv = new ActivityVariables(getWindow().getDecorView().getRootView(), this.getApplicationContext(), this);
        RelativeLayout MainLayout = findViewById(R.id.mainActivity);

        Database db = new Database(MainLayout); db.CreateDatabase();  //Database Creation

        settings settingsMenu = new settings(db);//Creates the settings menu
        CreateNavigation(); //Creates the navigation menu
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Creates the widgets for current view
        WidgetFactory widgetFactory = new WidgetFactory(mAv, getSupportFragmentManager(), db);
        widgetFactory.CreateMainButtons(MainLayout, settingsMenu);

        //Sets the information from the database
        Thread newThread = new Thread((Runnable) db.SetData(widgetFactory)); newThread.start();
        try {newThread.join();} catch (InterruptedException e) {throw new RuntimeException(e);}
        db.SetData(widgetFactory);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {return true;}
        return super.onOptionsItemSelected(item);
    }

    void CreateNavigation(){
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
}