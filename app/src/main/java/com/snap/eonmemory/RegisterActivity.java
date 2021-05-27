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

import java.util.regex.Pattern;

import model.User;
import model.UserList;

public class RegisterActivity extends AppCompatActivity {

    ImageView register_back_imageView;
    TextInputLayout register_username_textInput, register_email_textInput, register_password_textInput, register_confirm_password_textInput;
    Button register_register_button;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();

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

        createClickListener();

    }

    private void createClickListener() {
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

                Boolean validateUsername = false, validateEmail = false, validatePassword = false;

                if (username.isEmpty()) {
                    register_username_textInput.setError("Please fill the name column");
                } else {
                    register_username_textInput.setError("");
                    validateUsername = true;
                }

                Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

                if (email.isEmpty()) {
                    register_email_textInput.setError("Please fill the email column");
                } else {
                    if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
                        register_email_textInput.setError("Wrong email format");
                    } else {
                        register_email_textInput.setError("");
                        validateEmail = true;
                    }
                }

                Pattern PASSWORD_PATTERN = Pattern.compile("^" + "[a-zA-Z0-9]" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{8,20}" + "$");

                if(password.isEmpty()){
                    register_password_textInput.setError("Please fill the password column");
                } else {
                    if (password.length() < 8 || password.length() > 20) {
                        register_password_textInput.setError("Must contains 8-20 Characters");
                    } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                        register_password_textInput.setError("Must contains at least uppercase/lowercase, number, and special character");
                    } else {
                        register_password_textInput.setError("");
                    }
                }

                if(confirmPassword.isEmpty()){
                    register_confirm_password_textInput.setError("Please confirm your password");
                } else {
                    if(!confirmPassword.equalsIgnoreCase(password)){
                        register_confirm_password_textInput.setError("Password doesn't match");
                    } else {
                        register_confirm_password_textInput.setError("");
                        validatePassword = true;
                    }
                }

                if(validateUsername && validateUsername && validatePassword){
                    User newUser = new User(username, email, password);
                    UserList.addUserToUserList(newUser);
                    clearError();

                    intent = new Intent(getBaseContext(), WelcomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Toast.makeText(RegisterActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(intent);
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