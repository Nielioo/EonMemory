package com.snap.eonmemory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import model.Task;
import model.setRefresh;

public class EditTaskActivity extends AppCompatActivity implements setRefresh {

    private Toolbar editTask_toolbar;
    private TextInputLayout editTask_TILayout_title, editTask_TILayout_description;
    private String taskId;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        initFirebase();
        initView();
        loadTask();
        setListener();
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFocus() instanceof TextInputEditText) {
            getCurrentFocus().clearFocus();
        } else {
            saveTask();
            super.onBackPressed();
        }
    }

    private void saveTask() {
        String title = editTask_TILayout_title.getEditText().getText().toString().trim();
        String description = editTask_TILayout_description.getEditText().getText().toString().trim();

        DocumentReference taskReference = fStore.collection("user_collection")
                .document(userID).collection("task_collection")
                .document(taskId);

        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", description);
        task.put("updated", FieldValue.serverTimestamp());

        taskReference.update(task).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error", e.toString());
            }
        });
    }

    private void setListener() {
        editTask_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
                finish();
            }
        });

        editTask_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_task_menu_delete:
                        DocumentReference taskReference = fStore.collection("user_collection")
                                .document(userID).collection("task_collection").document(taskId);

                        taskReference.delete().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("error", e.toString());
                            }
                        });

                        finish();
                        break;
                }

                return true;
            }
        });
    }

    private void loadTask() {
        DocumentReference taskReference = fStore.collection("user_collection")
                .document(userID).collection("task_collection")
                .document(taskId);

        taskReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                editTask_TILayout_title.getEditText().setText(value.getString("title"));
                editTask_TILayout_description.getEditText().setText(value.getString("description"));
            }
        });
    }

    private void initView() {
        editTask_toolbar = findViewById(R.id.editTask_toolbar);
        editTask_TILayout_title = findViewById(R.id.editTask_TILayout_title);
        editTask_TILayout_description = findViewById(R.id.editTask_TILayout_description);

        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public void setSwipeRefresh() {

    }
}