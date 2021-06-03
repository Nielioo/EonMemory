package com.snap.eonmemory;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawer_actionBarDrawerToggle;
    private NavigationView drawer_navigation_view;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Toolbar home_toolbar;
    private BottomNavigationView home_bottomNavigation;
    private Dialog createCategory_dialog;
    private TextInputLayout createCategory_textInput_category;
    private TextView createCategory_textView_create, createCategory_textView_cancel;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initFirebase();
        initView();

        drawer_actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                home_toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(drawer_actionBarDrawerToggle);
        drawer_actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer_actionBarDrawerToggle.syncState();

        setBottomNavigation();
        setListener();
        setDialog();
        drawer_navigation_view.setNavigationItemSelectedListener(this);
    }

    private void createCategory(String category) {
        DocumentReference categoryList = fStore.collection("user_collection").document(userID)
                .collection("category_collection").document("category_list");

        categoryList.update("category", FieldValue.arrayUnion(category));
    }

    private void setDialog() {
        createCategory_dialog = new Dialog(HomePageActivity.this);
        createCategory_dialog.setContentView(R.layout.dialog_create_category);
        createCategory_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_create_category_shape));
        createCategory_dialog.getWindow().setElevation(8f);
        createCategory_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        createCategory_dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        createCategory_textInput_category = createCategory_dialog.findViewById(R.id.createCategory_textInput_category);
        createCategory_textView_create = createCategory_dialog.findViewById(R.id.createCategory_textView_create);
        createCategory_textView_cancel = createCategory_dialog.findViewById(R.id.createCategory_textView_cancel);

        createCategory_textView_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = createCategory_textInput_category.getEditText().getText().toString().trim();

                createCategory(category);

                createCategory_dialog.dismiss();
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
                        createCategory_textInput_category.getEditText().requestFocus();
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
        home_bottomNavigation = findViewById(R.id.home_bottomNavigation);
//        setSupportActionBar(home_toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = findViewById(R.id.drawer);
        drawer_navigation_view = findViewById(R.id.navigation_view);
        drawer_navigation_view.setNavigationItemSelectedListener(this);

        setSupportActionBar(home_toolbar);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction().add(R.id.home_fragmentContainer, new TaskFragment());
        fragmentTransaction.commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragmentContainer, new TaskFragment()).commit();
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_profile) {

            intent = new Intent(getBaseContext(), ProfilePageActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.menu_settings) {

        }
        if (item.getItemId() == R.id.menu_task) {
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.home_fragmentContainer, new TaskFragment());
            fragmentTransaction.commit();
        }
        if (item.getItemId() == R.id.menu_calendar) {
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.home_fragmentContainer, new CalendarFragment());
            fragmentTransaction.commit();
        }
        if (item.getItemId() == R.id.menu_note) {
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.home_fragmentContainer, new NoteFragment());
            fragmentTransaction.commit();
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}