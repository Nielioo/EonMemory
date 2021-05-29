package com.snap.eonmemory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {

//    private Toolbar home_toolbar;
    private BottomNavigationView home_bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initView();
        setBottomNavigation();
    }

    private void setBottomNavigation() {
        home_bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.menu_task:
                        fragment = new TaskFragment();
                        break;
                    case R.id.menu_calendar:
                        fragment = new CalendarFragment();
                        break;
                    case R.id.menu_note:
                        fragment = new NoteFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragmentContainer, fragment).commit();

                return true;
            }
        });
    }

    private void initView() {
        home_bottomNavigation = findViewById(R.id.home_bottomNavigation);

//        home_toolbar = (Toolbar) findViewById(R.id.home_toolbar);
//        setSupportActionBar(home_toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragmentContainer, new TaskFragment()).commit();
    }
}