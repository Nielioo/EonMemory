package com.snap.eonmemory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import model.User;
import model.UserList;

public class SignInActivity extends AppCompatActivity {

    ImageView sign_in_back_imageView;
    TextInputLayout sign_in_email_textInput, sign_in_password_textInput;
    Button sign_in_sign_in_button;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialize();

        TextWatcher tmpWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = sign_in_email_textInput.getEditText().getText().toString().trim();
                String password = sign_in_password_textInput.getEditText().getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    sign_in_sign_in_button.setEnabled(true);
                    sign_in_sign_in_button.setClickable(true);
                } else {
                    sign_in_sign_in_button.setEnabled(false);
                    sign_in_sign_in_button.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

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

                String email = sign_in_email_textInput.getEditText().getText().toString().trim();
                String password = sign_in_password_textInput.getEditText().getText().toString().trim();

                if(!email.isEmpty() && !password.isEmpty()){
                    User thisUser = UserList.getUser(email, password);

                    if(thisUser != null){
                        Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getBaseContext(), HomePageActivity.class);
                        intent.putExtra("thisUser", thisUser);

                        sign_in_email_textInput.getEditText().setText("");
                        sign_in_email_textInput.setError("");
                        sign_in_password_textInput.getEditText().setText("");
                        sign_in_password_textInput.setError("");

                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignInActivity.this, "invalid email or password", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void initialize() {
        sign_in_back_imageView = findViewById(R.id.sign_in_back_imageView);
        sign_in_email_textInput = findViewById(R.id.sign_in_email_textInput);
        sign_in_password_textInput = findViewById(R.id.sign_in_password_textInput);
        sign_in_sign_in_button = findViewById(R.id.sign_in_sign_in_button);
    }

}