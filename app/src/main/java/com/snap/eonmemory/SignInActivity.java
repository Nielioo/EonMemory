package com.snap.eonmemory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    ImageView sign_in_back_imageView;
    TextInputLayout sign_in_email_textInput, sign_in_password_textInput;
    Button sign_in_sign_in_button;
    Intent intent;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialize();

        mAuth = FirebaseAuth.getInstance();

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

        sign_in_email_textInput.getEditText().addTextChangedListener(tmpWatcher);
        sign_in_password_textInput.getEditText().addTextChangedListener(tmpWatcher);

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

                if (!email.isEmpty() && !password.isEmpty()) {

                    mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            intent = new Intent(getBaseContext(), MainPageActivity.class);
                            Toast.makeText(SignInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            sign_in_email_textInput.getEditText().setText("");
                            sign_in_email_textInput.setError("");
                            sign_in_password_textInput.getEditText().setText("");
                            sign_in_password_textInput.setError("");

                            finish();
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignInActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                            Log.d("error",e.toString());
                        }
                    });

                }
            }
        });

    }

    private void initialize() {
        sign_in_back_imageView = findViewById(R.id.sign_in_back_imageView);
        sign_in_email_textInput = findViewById(R.id.sign_in_email_textInput);
        sign_in_password_textInput = findViewById(R.id.sign_in_password_textInput);
        sign_in_sign_in_button = findViewById(R.id.sign_in_sign_in_button);

        sign_in_sign_in_button.setEnabled(false);
        sign_in_sign_in_button.setClickable(false);
    }

}