package com.snap.eonmemory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SettingPageActivity extends AppCompatActivity {

    private ImageView setting_imageView_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        initView();
        setListener();
    }

    private void setListener() {
        setting_imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        setting_imageView_back = findViewById(R.id.setting_imageView_back);
    }
}