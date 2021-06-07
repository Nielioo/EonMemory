package com.snap.eonmemory;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    ImageView register_back_imageView;
    TextInputLayout register_username_textInput, register_email_textInput, register_password_textInput, register_confirm_password_textInput;
    Button register_register_button;
    Intent intent;

    Dialog dialog;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        TextWatcher tmpWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = register_username_textInput.getEditText().getText().toString().trim();
                String email = register_email_textInput.getEditText().getText().toString().trim();
                String password = register_password_textInput.getEditText().getText().toString().trim();
                String confirmPassword = register_confirm_password_textInput.getEditText().getText().toString().trim();

                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                    register_register_button.setEnabled(true);
                    register_register_button.setClickable(true);
                } else {
                    register_register_button.setEnabled(false);
                    register_register_button.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        register_username_textInput.getEditText().addTextChangedListener(tmpWatcher);
        register_email_textInput.getEditText().addTextChangedListener(tmpWatcher);
        register_password_textInput.getEditText().addTextChangedListener(tmpWatcher);
        register_confirm_password_textInput.getEditText().addTextChangedListener(tmpWatcher);

        register_back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), WelcomePageActivity.class);
                finish();
            }
        });

        register_register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = register_username_textInput.getEditText().getText().toString().trim();
                String email = register_email_textInput.getEditText().getText().toString().trim();
                String password = register_password_textInput.getEditText().getText().toString().trim();
                String confirmPassword = register_confirm_password_textInput.getEditText().getText().toString().trim();

                Boolean validateUsername = false, validateEmail = false, validatePassword = false, validateConfirmPassword = false;

                if (username.isEmpty()) {
                    register_username_textInput.setError("Please fill the name column");
                    validateUsername = false;
                } else {
                    register_username_textInput.setError("");
                    validateUsername = true;
                }

                Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

                if (email.isEmpty()) {
                    register_email_textInput.setError("Please fill the email column");
                    validateEmail = false;
                } else {
                    if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
                        register_email_textInput.setError("Wrong email format");
                        validateEmail = false;
                    } else {
                        register_email_textInput.setError("");
                        validateEmail = true;
                    }
                }

                Pattern PASSWORD_PATTERN = Pattern.compile("^" + "[a-zA-Z0-9]" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{7,20}" + "$");

                if (password.isEmpty()) {
                    register_password_textInput.setError("Please fill the password column");
                    validatePassword = false;
                } else {
                    if (password.length() < 8 || password.length() > 20) {
                        register_password_textInput.setError("Must contains 8-20 Characters");
                        validatePassword = false;
                    } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                        register_password_textInput.setError("Must contains at least uppercase/lowercase, number, and special character");
                        validatePassword = false;
                    } else {
                        register_password_textInput.setError("");
                        validatePassword = true;
                    }
                }

                if (confirmPassword.isEmpty()) {
                    register_confirm_password_textInput.setError("Please confirm your password");
                    validateConfirmPassword = false;
                } else {
                    if (!confirmPassword.equalsIgnoreCase(password)) {
                        register_confirm_password_textInput.setError("Password doesn't match");
                        validateConfirmPassword = false;
                    } else {
                        register_confirm_password_textInput.setError("");
                        validateConfirmPassword = true;
                    }
                }

                if (validateUsername && validateEmail && validatePassword && validateConfirmPassword) {

                    clearError();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                userID = mAuth.getCurrentUser().getUid();
                                DocumentReference userReference = fStore.collection("user_collection").document(userID);
                                Map<String, Object> user_info = new HashMap<>();
                                user_info.put("username", username);
                                user_info.put("email", email);

                                userReference.set(user_info).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(RegisterActivity.this, "Account Registered!", Toast.LENGTH_SHORT).show();

                                        FirebaseAuth.getInstance().signOut();

                                        intent = new Intent(getBaseContext(), WelcomePageActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(RegisterActivity.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                                        Log.d("error", e.toString());
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed to Create Account!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });

    }

    private void clearError() {
        register_username_textInput.getEditText().setText("");
        register_username_textInput.setError("");
        register_email_textInput.getEditText().setText("");
        register_email_textInput.setError("");
        register_password_textInput.getEditText().setText("");
        register_password_textInput.setError("");
        register_confirm_password_textInput.getEditText().setText("");
        register_confirm_password_textInput.setError("");
    }

    private void initialize() {
        register_back_imageView = findViewById(R.id.register_back_imageView);
        register_username_textInput = findViewById(R.id.register_username_textInput);
        register_email_textInput = findViewById(R.id.register_email_textInput);
        register_password_textInput = findViewById(R.id.register_password_textInput);
        register_confirm_password_textInput = findViewById(R.id.register_confirm_password_textInput);
        register_register_button = findViewById(R.id.register_register_button);

        register_register_button.setEnabled(false);
        register_register_button.setClickable(false);
    }

}