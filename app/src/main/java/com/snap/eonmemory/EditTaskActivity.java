package com.snap.eonmemory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;

public class EditTaskActivity extends AppCompatActivity {

    private TextInputLayout editTask_textInput_title, editTask_textInput_description;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        initView();
    }

    private void loadTask() {

    }

    private void initView() {
        editTask_textInput_title = findViewById(R.id.editTask_textInput_title);
        editTask_textInput_description = findViewById(R.id.editTask_textInput_description);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
    }
}