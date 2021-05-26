package com.snap.eonmemory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {

    ImageView sign_in_back_imageView;
    TextInputLayout sign_in_username_textInput, sign_in_password_textInput;
    Button sign_in_sign_in_button;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialize();
        createErrorListener();
        createClickListener();
    }



    private void createClickListener() {
        sign_in_back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), WelcomePageActivity.class);
                finish();
            }
        });

        sign_in_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createErrorListener() {

    }

    private void initialize() {
        sign_in_back_imageView = findViewById(R.id.sign_in_back_imageView);
        sign_in_username_textInput = findViewById(R.id.sign_in_username_textInput);
        sign_in_password_textInput = findViewById(R.id.sign_in_password_textInput);
        sign_in_sign_in_button = findViewById(R.id.sign_in_sign_in_button);
    }

}