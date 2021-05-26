package com.snap.eonmemory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomePageActivity extends AppCompatActivity {

    Button welcome_page_sign_in_button;
    ImageView welcome_page_google_imageView, welcome_page_facebook_imageView;
    TextView welcome_page_register_textView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        initialize();
        createClickListener();

    }

    private void createClickListener() {
        welcome_page_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        welcome_page_register_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initialize() {
        welcome_page_sign_in_button = findViewById(R.id.welcome_page_sign_in_button);
        welcome_page_google_imageView = findViewById(R.id.welcome_page_google_imageView);
        welcome_page_facebook_imageView = findViewById(R.id.welcome_page_facebook_imageView);
        welcome_page_register_textView = findViewById(R.id.welcome_page_register_textView);
    }

}