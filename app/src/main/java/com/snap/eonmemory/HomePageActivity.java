package com.snap.eonmemory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {

    private Toolbar home_toolbar;
    private BottomNavigationView home_bottomNavigation;
    private Dialog createCategory_dialog;
    private TextView createCategory_textView_create, createCategory_textView_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initView();
        setBottomNavigation();
        setListener();
        setDialog();
    }

    private void setDialog() {
        createCategory_dialog = new Dialog(HomePageActivity.this);
        createCategory_dialog.setContentView(R.layout.dialog_create_category);
        createCategory_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_create_category_shape));
        createCategory_dialog.getWindow().setElevation(8f);
        createCategory_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        createCategory_dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        createCategory_textView_create = createCategory_dialog.findViewById(R.id.createCategory_textView_create);
        createCategory_textView_cancel = createCategory_dialog.findViewById(R.id.createCategory_textView_cancel);

        createCategory_textView_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Category created", Toast.LENGTH_SHORT).show();
            }
        });

        createCategory_textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCategory_dialog.dismiss();
            }
        });
    }

    private void setListener() {
        home_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_toolbar_menu_addCategory:
                        createCategory_dialog.show();
                        break;
                }
                return true;
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.home_toolbar_menu, menu);
//        return true;
//    }

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
        home_toolbar = findViewById(R.id.home_toolbar);
//        setSupportActionBar(home_toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        home_bottomNavigation = findViewById(R.id.home_bottomNavigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragmentContainer, new TaskFragment()).commit();
    }
}