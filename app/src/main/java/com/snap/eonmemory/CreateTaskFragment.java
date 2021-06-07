package com.snap.eonmemory;

import android.app.DatePickerDialog;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import model.Category;
import model.setRefresh;

public class CreateTaskFragment extends BottomSheetDialogFragment {

    private View view;
    private TextInputLayout createTask_TILayout_title;
    private Button createTask_button_category;
    private ImageButton createTask_imageButton_calendar, createTask_imageButton_save;
    private PopupMenu categoryList;
    private boolean validateTitle;
    private String dueDate;

    private ArrayList<String> categoryItemList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    private setRefresh refresh;

    public CreateTaskFragment(setRefresh refresh) {
        this.refresh = refresh;
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
    private void createTask(String title, String category, String dueDate) {
        CollectionReference taskReference = fStore.collection("user_collection")
                .document(userID).collection("task_collection");

        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", "");
        task.put("status", 0);
        task.put("dueDate", dueDate);
        task.put("category", category);
        task.put("created", FieldValue.serverTimestamp());

        taskReference.add(task).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", e.toString());
            }
        });

        dismiss();
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
                Calendar calendar = Calendar.getInstance();

                int MONTH = calendar.get(Calendar.MONTH);
                int YEAR = calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        // Set date text later

                        dueDate = dayOfMonth + "/" + month + "/" + year;
                    }
                }, YEAR, MONTH, DAY);

                datePickerDialog.show();
            }
        });

        createTask_imageButton_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save task to database
                String title = createTask_TILayout_title.getEditText().getText().toString().trim();
                String category = createTask_button_category.getText().toString().trim();

                createTask(title, category, dueDate);

                refresh.setSwipeRefresh();
            }
        });
    }

    private void setPopupMenu() {
        categoryList = new PopupMenu(getContext(), view);

        CollectionReference categoryReference = fStore.collection("user_collection").document(userID)
                .collection("category_collection");

        categoryReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot data : queryDocumentSnapshots) {
                    Category categoryObject = data.toObject(Category.class);

                    categoryItemList = categoryObject.getCategory();

                    for (String category : categoryItemList) {
                        categoryList.getMenu().add(category);
                    }

                    categoryList.getMenuInflater().inflate(R.menu.category_menu, categoryList.getMenu());

                    categoryList.show();
                }
            }
        });

        categoryList.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createTask_button_category.setText(item.getTitle());
//                        Toast.makeText(getContext(), "You clicked " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void initView() {
        createTask_TILayout_title = view.findViewById(R.id.createTask_TILayout_title);
        createTask_button_category = view.findViewById(R.id.createTask_button_category);
        createTask_imageButton_calendar = view.findViewById(R.id.createTask_imageButton_calendar);
        createTask_imageButton_save = view.findViewById(R.id.createTask_imageButton_save);

        createTask_TILayout_title.requestFocus();
        createTask_imageButton_save.setEnabled(false);

        dueDate = "";
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }
}