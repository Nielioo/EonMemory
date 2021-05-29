package com.snap.eonmemory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class EditTaskActivity extends AppCompatActivity {

    private Toolbar editTask_toolbar;
    private TextInputLayout editTask_TILayout_title, editTask_TILayout_description;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        initView();
        loadTask();
        setListener();
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFocus() instanceof TextInputEditText) {
            getCurrentFocus().clearFocus();
        } else {
            super.onBackPressed();
        }
    }

    private void setListener() {
        editTask_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadTask() {
        String url = "http://192.168.1.6/EonMemory/EonMemoryDB/ReadTaskById.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject parameter = new JSONObject();
        try {
            parameter.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameter,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject task = response.getJSONObject("task");
                            editTask_TILayout_title.getEditText().setText(task.getString("title"));
                            editTask_TILayout_description.getEditText().setText(task.getString("description"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(request);
    }

    private void initView() {
        editTask_toolbar = findViewById(R.id.editTask_toolbar);
        editTask_TILayout_title = findViewById(R.id.editTask_TILayout_title);
        editTask_TILayout_description = findViewById(R.id.editTask_TILayout_description);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
    }
}