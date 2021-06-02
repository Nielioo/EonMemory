package com.snap.eonmemory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfilePageActivity extends AppCompatActivity {

    ImageView profile_back_imageView, profile_image_imageView, profile_edit_image_imageView, profile_edit_username_imageView, profile_edit_email_imageView, profile_edit_password_imageView, profile_sign_out_imageView;
    TextView profile_username_textView, profile_email_textView, profile_pending_task_textView, profile_completed_task_textView;
    Button profile_delete_button;
    Intent intent;

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore fStore;
    String userID;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        initialize();

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        user = mAuth.getCurrentUser();
        userID = mAuth.getCurrentUser().getUid();

        StorageReference profilePictureReference = storageReference.child("user_collection/" + userID + "/profile_picture.png");
        profilePictureReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profile_image_imageView);
            }
        });

        DocumentReference userReference = fStore.collection("user_collection").document(userID);
        userReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException error) {
                profile_username_textView.setText(documentSnapshot.getString("username"));
                profile_email_textView.setText(documentSnapshot.getString("email"));
            }
        });

        profile_back_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                finish();
                startActivity(intent);
            }
        });

        profile_edit_image_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

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

                Toast.makeText(ProfilePageActivity.this, "Cya! Have a Nice Day!", Toast.LENGTH_SHORT).show();

                intent = new Intent(getBaseContext(), WelcomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                finish();
                startActivity(intent);
            }
        });

        profile_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProfilePageActivity.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your account from the system and you won't be able to access it anymore.");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userReference.delete();
                        profilePictureReference.delete();
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProfilePageActivity.this, "Account deleted!", Toast.LENGTH_SHORT).show();

                                    intent = new Intent(getBaseContext(), WelcomePageActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    finish();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ProfilePageActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                profile_image_imageView.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);

            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference imageReference = storageReference.child("user_collection/" + userID + "/profile_picture.png");
        imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ProfilePageActivity.this, "Image Uploaded!", Toast.LENGTH_SHORT).show();
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profile_image_imageView);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ProfilePageActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                Log.d("error", e.toString());
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