package com.snap.eonmemory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class WelcomePageActivity extends AppCompatActivity {

    Button welcome_page_sign_in_button;
    ImageView welcome_page_google_imageView, welcome_page_facebook_imageView;
    TextView welcome_page_register_textView;
    Intent intent;

    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        firebaseAuth = FirebaseAuth.getInstance();

        initialize();
        google_signIn();
        createClickListener();

    }

    private void google_signIn() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1045758085755-udjgm8hc6juome0mrebgpblg0des56au.apps.googleusercontent.com")
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(WelcomePageActivity.this, googleSignInOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            intent = new Intent(getBaseContext(), MainPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    private void createClickListener() {
        welcome_page_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        welcome_page_register_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        welcome_page_google_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            if (signInAccountTask.isSuccessful()) {
                Toast.makeText(this, "Google Sign In Successful!", Toast.LENGTH_SHORT).show();

                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);

                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    intent = new Intent(getBaseContext(), MainPageActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                    Toast.makeText(WelcomePageActivity.this, "Authentication successful!", Toast.LENGTH_SHORT).show();

                                    startActivity(intent);
                                } else {
                                    Toast.makeText(WelcomePageActivity.this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }

        } else {
            Toast.makeText(this, "Google Sign In Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initialize() {
        welcome_page_sign_in_button = findViewById(R.id.welcome_page_sign_in_button);
        welcome_page_google_imageView = findViewById(R.id.welcome_page_google_imageView);
        welcome_page_facebook_imageView = findViewById(R.id.welcome_page_facebook_imageView);
        welcome_page_register_textView = findViewById(R.id.welcome_page_register_textView);
    }

}