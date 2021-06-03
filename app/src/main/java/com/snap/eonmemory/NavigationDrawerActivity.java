package com.snap.eonmemory;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar home_toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle drawer_actionBarDrawerToggle;
    NavigationView drawer_navigation_view;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        initialize();

        drawer_actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                home_toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(drawer_actionBarDrawerToggle);
        drawer_actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer_actionBarDrawerToggle.syncState();

    }

    private void initialize() {
        home_toolbar = findViewById(R.id.home_toolbar);
        drawer = findViewById(R.id.drawer);
        drawer_navigation_view = findViewById(R.id.drawer_navigation_view);
        drawer_navigation_view.setNavigationItemSelectedListener(this);

        setSupportActionBar(home_toolbar);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.home_fragmentContainer, new TaskFragment());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_profile) {

        }
        if (item.getItemId() == R.id.menu_settings) {

        }
        if (item.getItemId() == R.id.menu_task) {

        }
        if (item.getItemId() == R.id.menu_calendar) {

        }
        if (item.getItemId() == R.id.menu_note) {

        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}