package com.snap.eonmemory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {

//    private Toolbar toolbar;
    private FloatingActionButton home_FAB_createTask;
    private BottomNavigationView home_bottomNavigation;
    private ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initView();
        setListener();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.home_fragmentContainer);

        if (currentFragment == null) {
            fm.add(R.id.home_fragmentContainer, fragment).commit();
        } else {
            fm.replace(R.id.home_fragmentContainer, fragment).commit();
        }

    }

    private void setListener() {
        home_bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

                setFragment(fragment);

                return fragment != null;
            }
        });
    }

    private void initView() {
        home_FAB_createTask = findViewById(R.id.home_FAB_createTask);
        home_bottomNavigation = findViewById(R.id.home_bottomNavigation);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setFragment(new TaskFragment());
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}