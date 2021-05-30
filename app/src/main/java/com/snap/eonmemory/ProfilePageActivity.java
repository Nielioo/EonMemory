package com.snap.eonmemory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProfilePageActivity extends AppCompatActivity {

    ImageView profile_back_imageView, profile_image_imageView, profile_edit_image_imageView, profile_edit_username_imageView, profile_edit_email_imageView, profile_edit_password_imageView, profile_sign_out_imageView;
    TextView profile_username_textView, profile_email_textView, profile_pending_task_textView, profile_completed_task_textView;
    Button profile_delete_button;
    Intent intent;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        initialize();

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = mAuth.getCurrentUser().getUid();

        DocumentReference userReference = fStore.collection("user_collection").document(userID);
        userReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException error) {
                profile_username_textView.setText(documentSnapshot.getString("username"));
                profile_email_textView.setText(documentSnapshot.getString("email"));
            }
        });

        profile_edit_username_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), ChangeUsernameActivity.class);
                startActivity(intent);
            }
        });

        profile_edit_email_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), ChangeEmailActivity.class);
                startActivity(intent);
            }
        });

        profile_edit_password_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        profile_sign_out_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                intent = new Intent(getBaseContext(), WelcomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(ProfilePageActivity.this, "Cya! Have a Nice Day!", Toast.LENGTH_SHORT).show();

                finish();
                startActivity(intent);
            }
        });

        profile_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void initialize() {
        profile_back_imageView = findViewById(R.id.profile_back_imageView);
        profile_image_imageView = findViewById(R.id.profile_image_imageView);
        profile_edit_image_imageView = findViewById(R.id.profile_edit_image_imageView);
        profile_edit_username_imageView = findViewById(R.id.profile_edit_username_imageView);
        profile_edit_email_imageView = findViewById(R.id.profile_edit_email_imageView);
        profile_edit_password_imageView = findViewById(R.id.profile_edit_password_imageView);
        profile_sign_out_imageView = findViewById(R.id.profile_sign_out_imageView);
        profile_username_textView = findViewById(R.id.profile_username_textView);
        profile_email_textView = findViewById(R.id.profile_email_textView);
        profile_pending_task_textView = findViewById(R.id.profile_pending_task_textView);
        profile_completed_task_textView = findViewById(R.id.profile_completed_task_textView);
        profile_delete_button = findViewById(R.id.profile_delete_button);
    }
}