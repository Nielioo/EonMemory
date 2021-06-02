package com.snap.eonmemory;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.HashMap;
import java.util.Map;

import model.Task;

public class CreateTaskFragment extends BottomSheetDialogFragment {

    private View view;
    private TextInputLayout createTask_TILayout_title;
    private Button createTask_button_category;
    private ImageButton createTask_imageButton_calendar, createTask_imageButton_save;
    private PopupMenu categoryList;
    private int maxVarChar;
    private boolean validateTitle;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    public CreateTaskFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_task, container, false);

        initFirebase();
        initView();
        setListener();

        return view;
    }

    // Using Firebase
    private void uploadTask(String title, String description) {
        CollectionReference taskReference = fStore.collection("user_collection")
                .document(userID).collection("task_collection").document("category_list").collection("category");
        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", description);
        task.put("created", FieldValue.serverTimestamp());

        taskReference.add(task).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", e.toString());
            }
        });
    }

    private void isSaveValid(boolean validateTitle) {
        createTask_imageButton_save.setEnabled(validateTitle);
    }

    private void setListener() {
        createTask_TILayout_title.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String title = createTask_TILayout_title.getEditText().getText().toString().trim();

                if (title.isEmpty()) {
                    validateTitle = false;
                } else {
                    createTask_TILayout_title.setError(null);
                    validateTitle = true;
                }

                isSaveValid(validateTitle);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createTask_button_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open category list
                setPopupMenu();
            }
        });

        createTask_imageButton_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open calendar picker
                Toast.makeText(getContext(), "Calendar", Toast.LENGTH_SHORT).show();
            }
        });

        createTask_imageButton_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save task to database
                String title = createTask_TILayout_title.getEditText().getText().toString().trim();

                if (title.length() > maxVarChar) {
                    Toast.makeText(getContext(), "Title must not exceed 255 characters", Toast.LENGTH_SHORT).show();
                } else {
                    uploadTask(title, "");

                    // Close bottom sheet
                    dismiss();
                }
            }
        });
    }

    private void setPopupMenu() {
        categoryList = new PopupMenu(getContext(), view);
        categoryList.getMenuInflater().inflate(R.menu.category_menu, categoryList.getMenu());

        categoryList.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getContext(), "You clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        categoryList.show();
    }

    private void initView() {
        createTask_TILayout_title = view.findViewById(R.id.createTask_TILayout_title);
        createTask_button_category = view.findViewById(R.id.createTask_button_category);
        createTask_imageButton_calendar = view.findViewById(R.id.createTask_imageButton_calendar);
        createTask_imageButton_save = view.findViewById(R.id.createTask_imageButton_save);

        createTask_TILayout_title.requestFocus();
        createTask_imageButton_save.setEnabled(false);

        maxVarChar = 255;
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

}