package com.snap.eonmemory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import model.Task;
import model.setRefresh;

public class EditTaskActivity extends AppCompatActivity implements setRefresh {

    private Toolbar editTask_toolbar;
    private TextInputLayout editTask_TILayout_title, editTask_TILayout_description;
    private CardView editTask_cardView_dueDate;
    private TextView editTask_textView_dueDate;
    private ImageView editTask_imageView_clearDueDate;
    private String taskId, dueDate;

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
//        String dueDate = this.dueDate;

        DocumentReference taskReference = fStore.collection("user_collection")
                .document(userID).collection("task_collection")
                .document(taskId);

        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", description);
        task.put("dueDate", dueDate);
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

        editTask_cardView_dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open calendar picker
                Calendar calendar = Calendar.getInstance();

                int MONTH = calendar.get(Calendar.MONTH);
                int YEAR = calendar.get(Calendar.YEAR);
                int DAY = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        // Set date text later

                        dueDate = dayOfMonth + "/" + month + "/" + year;

                        editTask_textView_dueDate.setText(dueDate);
                    }
                }, YEAR, MONTH, DAY);

                datePickerDialog.show();
            }
        });

        editTask_imageView_clearDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dueDate = "";
                editTask_textView_dueDate.setText(R.string.set_due_date);
            }
        });
    }

    private void loadTask() {
        DocumentReference taskReference = fStore.collection("user_collection")
                .document(userID).collection("task_collection")
                .document(taskId);

        // Listen to every changes
        taskReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                editTask_toolbar.setTitle(value.getString("category"));
//                editTask_TILayout_title.getEditText().setText(value.getString("title"));
//                editTask_TILayout_description.getEditText().setText(value.getString("description"));
//                editTask_textView_dueDate.setText(value.getString("dueDate"));
            }
        });

        // Read data once
        taskReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String dueDate = documentSnapshot.getString("dueDate");

                editTask_toolbar.setTitle(documentSnapshot.getString("category"));
                editTask_TILayout_title.getEditText().setText(documentSnapshot.getString("title"));
                editTask_TILayout_description.getEditText().setText(documentSnapshot.getString("description"));

                if (dueDate == null) {
                    editTask_textView_dueDate.setText("Set due date");
                } else {
                    editTask_textView_dueDate.setText(documentSnapshot.getString("dueDate"));
                }
            }
        });
    }

    private void initView() {
        editTask_toolbar = findViewById(R.id.editTask_toolbar);
        editTask_TILayout_title = findViewById(R.id.editTask_TILayout_title);
        editTask_TILayout_description = findViewById(R.id.editTask_TILayout_description);
        editTask_textView_dueDate = findViewById(R.id.editTask_textView_dueDate);
        editTask_cardView_dueDate = findViewById(R.id.editTask_cardView_dueDate);
        editTask_imageView_clearDueDate = findViewById(R.id.editTask_imageView_clearDueDate);

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