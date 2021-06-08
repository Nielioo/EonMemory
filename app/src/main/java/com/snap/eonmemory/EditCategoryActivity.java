package com.snap.eonmemory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import model.Task;

public class EditCategoryActivity extends AppCompatActivity {

    private Toolbar editCategory_toolbar;
    private TextInputLayout editCategory_TILayout_category;
    private TextInputEditText editCategory_TIEditText_category;
    private Button editCategory_button_save;
    private String oldCategory;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        initFirebase();
        initView();
        setListener();
    }

    private void saveCategory() {
        String category = editCategory_TILayout_category.getEditText().getText().toString().trim();

        // Delete old category and add new category
        DocumentReference categoryList = fStore.collection("user_collection").document(userID)
                .collection("category_collection").document("category_list");

        categoryList.update("category", FieldValue.arrayRemove(oldCategory));
        categoryList.update("category", FieldValue.arrayUnion(category));

        // Rename category in task
        CollectionReference categoryReference = fStore.collection("user_collection").document(userID)
                .collection("task_collection");

        categoryReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot data : queryDocumentSnapshots) {
                    String id = data.getId();
                    Task task = data.toObject(Task.class).withId(id);
                    String currentCategory = task.getCategory();

                    if (currentCategory.equalsIgnoreCase(oldCategory)) {
                        DocumentReference taskReference = fStore.collection("user_collection")
                                .document(userID).collection("task_collection").document(task.TaskId);

                        taskReference.update("category", category);
                    }
                }
            }
        });
    }

    private void setListener() {
        editCategory_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editCategory_button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCategory();
                finish();
            }
        });
    }

    private void initView() {
        editCategory_toolbar = findViewById(R.id.editCategory_toolbar);
        editCategory_TILayout_category = findViewById(R.id.editCategory_TILayout_category);
        editCategory_TIEditText_category = findViewById(R.id.editCategory_TIEditText_category);
        editCategory_button_save = findViewById(R.id.editCategory_button_save);

        Intent intent = getIntent();
        oldCategory = intent.getStringExtra("category");
        editCategory_TIEditText_category.setText(oldCategory);
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }
}