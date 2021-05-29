package com.snap.eonmemory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ProfilePageActivity extends AppCompatActivity {

    ImageView profile_back_imageView, profile_image_imageView, profile_edit_image_imageView, profile_edit_username_imageView, profile_edit_email_imageView, profile_edit_password_imageView, profile_sign_out_imageView;
    TextView profile_username_textView, profile_email_textView, profile_pending_task_textView, profile_completed_task_textView;
    Button profile_delete_button;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        initialize();

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

    }

    private void initialize() {
        profile_back_imageView = findViewById(R.id.profile_back_imageView);
        profile_image_imageView = findViewById(R.id.profile_image_imageView);
        profile_edit_image_imageView = findViewById(R.id.profile_edit_image_imageView);
        profile_edit_username_imageView = findViewById(R.id.profile_edit_email_imageView);
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