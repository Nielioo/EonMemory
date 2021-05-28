package com.snap.eonmemory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import model.Task;

public class HomePageActivity extends AppCompatActivity {

//    private Toolbar home_toolbar;
    private FloatingActionButton home_FAB_createTask;
    private BottomNavigationView home_bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initView();
        setBottomNavigation();
        setListener();
    }

    private void setListener() {
        home_FAB_createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open create task
            }
        });
    }

    private void setBottomNavigation() {
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

                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragmentContainer, fragment).commit();

                return true;
            }
        });
    }

    private void initView() {
        home_FAB_createTask = findViewById(R.id.home_FAB_createTask);
        home_bottomNavigation = findViewById(R.id.home_bottomNavigation);

//        home_toolbar = (Toolbar) findViewById(R.id.home_toolbar);
//        setSupportActionBar(home_toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragmentContainer, new TaskFragment()).commit();
    }
}