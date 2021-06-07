package com.snap.eonmemory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChangeUsernameActivity extends AppCompatActivity {

    ImageView change_username_back_imageView;
    TextInputLayout change_username_new_username_textInput, change_username_confirm_username_textInput;
    Button change_username_save_button;
    Intent intent;

    Dialog dialog;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        initialize();

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        user = mAuth.getCurrentUser();

        TextWatcher tmpWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = change_username_new_username_textInput.getEditText().getText().toString().trim();
                String confirm_username = change_username_confirm_username_textInput.getEditText().getText().toString().trim();

                if (!username.isEmpty() && !confirm_username.isEmpty()) {
                    change_username_save_button.setEnabled(true);
                    change_username_save_button.setClickable(true);
                } else {
                    change_username_save_button.setEnabled(false);
                    change_username_save_button.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        change_username_new_username_textInput.getEditText().addTextChangedListener(tmpWatcher);
        change_username_confirm_username_textInput.getEditText().addTextChangedListener(tmpWatcher);

        change_username_back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), ProfilePageActivity.class);
                finish();
            }
        });

        change_username_save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = change_username_new_username_textInput.getEditText().getText().toString().trim();
                String confirm_username = change_username_confirm_username_textInput.getEditText().getText().toString().trim();

                Boolean validateUsername= false, validateConfirmUsername = false;

                if (username.isEmpty()) {
                    change_username_new_username_textInput.setError("Please fill the name column");
                    validateUsername = false;
                } else {
                    change_username_new_username_textInput.setError("");
                    validateUsername = true;
                }

                if (confirm_username.isEmpty()) {
                    change_username_confirm_username_textInput.setError("Please confirm your username");
                    validateConfirmUsername = false;
                } else {
                    if (!confirm_username.equalsIgnoreCase(username)) {
                        change_username_confirm_username_textInput.setError("Username doesn't match");
                        validateConfirmUsername = false;
                    } else {
                        change_username_confirm_username_textInput.setError("");
                        validateConfirmUsername = true;
                    }
                }

                if (validateUsername && validateConfirmUsername) {
                    change_username_new_username_textInput.getEditText().setText("");
                    change_username_new_username_textInput.setError("");
                    change_username_confirm_username_textInput.getEditText().setText("");
                    change_username_confirm_username_textInput.setError("");

                    DocumentReference usernameReference = fStore.collection("user_collection").document(user.getUid());
                    Map<String, Object> newUsername = new HashMap<>();
                    newUsername.put("username", username);

                    usernameReference.update(newUsername).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            intent = new Intent(getBaseContext(), ProfilePageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            dialog = new Dialog(ChangeUsernameActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.loading_bar);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();

                            new CountDownTimer(5000, 1000) {

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


                            Toast.makeText(ChangeUsernameActivity.this, "Username changed!", Toast.LENGTH_SHORT).show();

                            finish();
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ChangeUsernameActivity.this, "Failed to change username", Toast.LENGTH_SHORT).show();
                            Log.d("error", e.toString());
                        }
                    });

                }

            }
        });


    }

    private void initialize(){
        change_username_back_imageView = findViewById(R.id.change_username_back_imageView);
        change_username_new_username_textInput = findViewById(R.id.change_username_new_username_textInput);
        change_username_confirm_username_textInput = findViewById(R.id.change_username_confirm_username_textInput);
        change_username_save_button = findViewById(R.id.change_username_save_button);

        change_username_save_button.setEnabled(false);
        change_username_save_button.setClickable(false);
    }

}