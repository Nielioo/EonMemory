package com.snap.eonmemory;

import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class NavigationDrawerActivity extends AppCompatActivity {

    Toolbar toolbar_navigation_drawer;
    DrawerLayout drawer;
    ActionBarDrawerToggle drawer_actionBarDrawerToggle;
    NavigationView drawer_navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        initialize();

        drawer_actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar_navigation_drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(drawer_actionBarDrawerToggle);
        drawer_actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer_actionBarDrawerToggle.syncState();

    }

    private void initialize() {
        toolbar_navigation_drawer = findViewById(R.id.toolbar_navigation_drawer);
        drawer = findViewById(R.id.drawer);
        drawer_navigation_view = findViewById(R.id.drawer_navigation_view);

        setSupportActionBar(toolbar_navigation_drawer);
    }

}