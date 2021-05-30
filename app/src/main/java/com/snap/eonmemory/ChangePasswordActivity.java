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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {

    ImageView change_password_back_imageView;
    TextInputLayout change_password_new_password_textInput, change_password_confirm_password_textInput;
    Button change_password_save_button;
    Intent intent;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initialize();

        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();

        TextWatcher tmpWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = change_password_new_password_textInput.getEditText().getText().toString().trim();
                String confirm_password = change_password_confirm_password_textInput.getEditText().getText().toString().trim();

                if (!password.isEmpty() && !confirm_password.isEmpty()) {
                    change_password_save_button.setEnabled(true);
                    change_password_save_button.setClickable(true);
                } else {
                    change_password_save_button.setEnabled(false);
                    change_password_save_button.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        change_password_new_password_textInput.getEditText().addTextChangedListener(tmpWatcher);
        change_password_confirm_password_textInput.getEditText().addTextChangedListener(tmpWatcher);

        change_password_back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), ProfilePageActivity.class);
                finish();
            }
        });

        change_password_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = change_password_new_password_textInput.getEditText().getText().toString().trim();
                String confirm_password = change_password_confirm_password_textInput.getEditText().getText().toString().trim();

                Boolean validatePassword = false, validateConfirmPassword = false;

                Pattern PASSWORD_PATTERN = Pattern.compile("^" + "[a-zA-Z0-9]" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{7,20}" + "$");

                if (password.isEmpty()) {
                    change_password_new_password_textInput.setError("Please fill the password column");
                    validatePassword = false;
                } else {
                    if (password.length() < 8 || password.length() > 20) {
                        change_password_new_password_textInput.setError("Must contains 8-20 Characters");
                        validatePassword = false;
                    } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                        change_password_new_password_textInput.setError("Must contains at least uppercase/lowercase, number, and special character");
                        validatePassword = false;
                    } else {
                        change_password_new_password_textInput.setError("");
                        validatePassword = true;
                    }
                }

                if (confirm_password.isEmpty()) {
                    change_password_confirm_password_textInput.setError("Please confirm your password");
                    validateConfirmPassword = false;
                } else {
                    if (!confirm_password.equalsIgnoreCase(password)) {
                        change_password_confirm_password_textInput.setError("Password doesn't match");
                        validateConfirmPassword = false;
                    } else {
                        change_password_confirm_password_textInput.setError("");
                        validateConfirmPassword = true;
                    }
                }

                if (validatePassword && validateConfirmPassword) {
                    change_password_new_password_textInput.getEditText().setText("");
                    change_password_new_password_textInput.setError("");
                    change_password_confirm_password_textInput.getEditText().setText("");
                    change_password_confirm_password_textInput.setError("");

                    user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ChangePasswordActivity.this, "Password changed!", Toast.LENGTH_SHORT).show();
                            intent = new Intent(getBaseContext(), ProfilePageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            finish();
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ChangePasswordActivity.this, "This operation is sensitive. Please re-login first!", Toast.LENGTH_SHORT).show();
                            Log.d("error", e.toString());
                        }
                    });


                }

            }
        });

    }

    private void initialize() {
        change_password_back_imageView = findViewById(R.id.change_password_back_imageView);
        change_password_new_password_textInput = findViewById(R.id.change_password_new_password_textInput);
        change_password_confirm_password_textInput = findViewById(R.id.change_password_confirm_password_textInput);
        change_password_save_button = findViewById(R.id.change_password_save_button);

        change_password_save_button.setEnabled(false);
        change_password_save_button.setClickable(false);
    }

}