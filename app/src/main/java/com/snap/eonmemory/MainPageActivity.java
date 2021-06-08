package com.snap.eonmemory;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.CategoryList;
import model.Task;

public class MainPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar main_toolbar;
    DrawerLayout drawer_layout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView drawer_navigation_view;
    ImageView drawer_image;
    TextView drawer_username, createCategory_textView_create, createCategory_textView_cancel;
    Dialog createCategory_dialog;
    TextInputLayout createCategory_textInput_category;
    BottomNavigationView main_bottomNavigation;
    View headerView;
    Intent intent;
    String categoryType;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore fStore;
    String userID;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        initialize();
        setDialog();
        setListener();
        setBottomNavigation();

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        user = mAuth.getCurrentUser();
        userID = mAuth.getCurrentUser().getUid();

        StorageReference profilePictureReference = storageReference.child("user_collection/" + userID + "/profile_picture.png");
        profilePictureReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(drawer_image);
            }
        });

        DocumentReference userReference = fStore.collection("user_collection").document(userID);
        userReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException error) {
                drawer_username.setText(documentSnapshot.getString("username"));
            }
        });

        setDrawerNavigationCategoryMenu();
    }

    private void setDrawerNavigationCategoryMenu() {
        Menu menu = drawer_navigation_view.getMenu();
        SubMenu subMenu = menu.addSubMenu("Category");

        CollectionReference categoryReference = fStore.collection("user_collection").document(userID)
                .collection("category_collection");

        categoryReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                subMenu.clear();

                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        CategoryList categoryObject = doc.toObject(CategoryList.class);
                        ArrayList<String> categoryItemList = categoryObject.getCategory();

                        for (int i = 0; i < categoryItemList.size(); i++) {
                            subMenu.add(3, i, i, categoryItemList.get(i)).setIcon(R.drawable.ic_baseline_dehaze_24);

                            if (i == categoryItemList.size() - 1) {
                                subMenu.add(3, -1, (i + 1), "Manage category").setIcon(R.drawable.ic_baseline_category_24);
                            }
                        }
                    }
                }
            }
        });
    }

    private void setBottomNavigation() {
        main_bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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

                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();

                return true;
            }
        });
    }

    private void createCategory(String category) {
        DocumentReference categoryList = fStore.collection("user_collection").document(userID)
                .collection("category_collection").document("category_list");

        categoryList.update("category", FieldValue.arrayUnion(category));
    }

    private void setDialog() {
        createCategory_dialog = new Dialog(MainPageActivity.this);
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

                if (!category.isEmpty()) {
                    createCategory(category);

//                    InputMethodManager inputMethodManager = (InputMethodManager) getBaseContext().getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
//                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    createCategory_dialog.dismiss();

                    Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Category created", Toast.LENGTH_SHORT).show();
                        }
                    }, 300);
                } else {
                    Toast.makeText(getBaseContext(), "Category name can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createCategory_textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InputMethodManager inputMethodManager = (InputMethodManager) getBaseContext().getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
//                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                createCategory_dialog.dismiss();
            }
        });
    }

    private void setListener() {
        main_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_toolbar_menu_addCategory:
                        createCategory_textInput_category.getEditText().setText("");
                        createCategory_dialog.show();
                        createCategory_textInput_category.getEditText().requestFocus();

//                        InputMethodManager inputMethodManager = (InputMethodManager) getBaseContext().getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
//                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                        break;
                }
                return true;
            }
        });
    }

    private void initialize() {
        main_toolbar = findViewById(R.id.main_toolbar);
        drawer_layout = findViewById(R.id.drawer_layout);
        drawer_navigation_view = findViewById(R.id.drawer_navigation_view);
        drawer_navigation_view.setNavigationItemSelectedListener(this);
        headerView = drawer_navigation_view.getHeaderView(0);
        drawer_image = headerView.findViewById(R.id.drawer_image);
        drawer_username = headerView.findViewById(R.id.drawer_username);
        main_bottomNavigation = findViewById(R.id.bottomNavigationView);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close);
        drawer_layout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction().add(R.id.main_container, new TaskFragment());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer_layout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.menu_profile) {
            intent = new Intent(getBaseContext(), ProfilePageActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.menu_settings) {
            intent = new Intent(getBaseContext(), SettingPageActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.menu_task) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new TaskFragment());
            fragmentTransaction.commit();
//            main_toolbar.setTitle("Task");
        }
        if (item.getItemId() == R.id.menu_calendar) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new CalendarFragment());
            fragmentTransaction.commit();
//            main_toolbar.setTitle("Calendar");
        }
        if (item.getItemId() == R.id.menu_note) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new NoteFragment());
            fragmentTransaction.commit();
//            main_toolbar.setTitle("Note");
        }
        if (item.getGroupId() == 3) {
            if (item.getItemId() == -1) {
                intent = new Intent(getBaseContext(), ManageCategoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                categoryType = item.getTitle().toString().trim();

                Bundle bundle = new Bundle();
                bundle.putString("category", categoryType);

                main_bottomNavigation.setSelectedItemId(R.id.menu_task);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                TaskFragment taskFragment = new TaskFragment();
                taskFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.main_container, taskFragment);
                fragmentTransaction.commit();
            }
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}