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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ChangeEmailActivity extends AppCompatActivity {

    ImageView change_email_back_imageView;
    TextInputLayout change_email_new_email_textInput, change_email_confirm_email_textInput;
    Button change_email_save_button;
    Intent intent;

    Dialog dialog;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        initialize();

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();

        TextWatcher tmpWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = change_email_new_email_textInput.getEditText().getText().toString().trim();
                String confirm_email = change_email_confirm_email_textInput.getEditText().getText().toString().trim();

                if (!email.isEmpty() && !confirm_email.isEmpty()) {
                    change_email_save_button.setEnabled(true);
                    change_email_save_button.setClickable(true);
                } else {
                    change_email_save_button.setEnabled(false);
                    change_email_save_button.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        change_email_new_email_textInput.getEditText().addTextChangedListener(tmpWatcher);
        change_email_confirm_email_textInput.getEditText().addTextChangedListener(tmpWatcher);

        change_email_back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), ProfilePageActivity.class);
                finish();
            }
        });

        change_email_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = change_email_new_email_textInput.getEditText().getText().toString().trim();
                String confirm_email = change_email_confirm_email_textInput.getEditText().getText().toString().trim();

                Boolean validateEmail = false, validateConfirmEmail = false;

                Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

                if (email.isEmpty()) {
                    change_email_new_email_textInput.setError("Please fill the email column");
                    validateEmail = false;
                } else {
                    if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
                        change_email_new_email_textInput.setError("Wrong email format");
                        validateEmail = false;
                    } else {
                        change_email_new_email_textInput.setError("");
                        validateEmail = true;
                    }
                }

                if (confirm_email.isEmpty()) {
                    change_email_confirm_email_textInput.setError("Please confirm your email");
                    validateConfirmEmail = false;
                } else {
                    if (!confirm_email.equalsIgnoreCase(email)) {
                        change_email_confirm_email_textInput.setError("Email doesn't match");
                        validateConfirmEmail = false;
                    } else {
                        change_email_confirm_email_textInput.setError("");
                        validateConfirmEmail = true;
                    }
                }

                if (validateEmail && validateConfirmEmail) {
                    change_email_new_email_textInput.getEditText().setText("");
                    change_email_new_email_textInput.setError("");
                    change_email_confirm_email_textInput.getEditText().setText("");
                    change_email_confirm_email_textInput.setError("");

                    user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            DocumentReference emailReference = fStore.collection("user_collection").document(userID);
                            Map<String, Object> newEmail = new HashMap<>();
                            newEmail.put("email", email);
                            emailReference.update(newEmail);

                            intent = new Intent(getBaseContext(), ProfilePageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            dialog = new Dialog(ChangeEmailActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.loading_bar);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();

                            new CountDownTimer(10000, 1000) {

                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    if (!isDestroyed()) {
                                        dialog.dismiss();
                                    }
                                }
                            }.start();


                            Toast.makeText(ChangeEmailActivity.this, "Email changed!", Toast.LENGTH_SHORT).show();

                            finish();
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ChangeEmailActivity.this, "This operation is sensitive. Please re-login first!", Toast.LENGTH_SHORT).show();
                            Log.d("error", e.toString());
                        }
                    });

                }
            }
        });

    }

    private void initialize() {
        change_email_back_imageView = findViewById(R.id.change_email_back_imageView);
        change_email_new_email_textInput = findViewById(R.id.change_email_new_email_textInput);
        change_email_confirm_email_textInput = findViewById(R.id.change_email_confirm_email_textInput);
        change_email_save_button = findViewById(R.id.change_email_save_button);

        change_email_save_button.setEnabled(false);
        change_email_save_button.setClickable(false);
    }

}