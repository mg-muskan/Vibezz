package com.vibezz.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.rpc.context.AttributeContext;
import com.vibezz.R;
import com.vibezz.databinding.ActivityMain2Binding;

import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {

    ActivityMain2Binding binding;
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        auth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);

        binding.imageGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        binding.imSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity2.this, SignUp.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });

        binding.tvLogin.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity2.this, SignIn.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });

        //If user is already logged in, redirect to the working activity (while opening the app)
        if(auth.getCurrentUser() != null)
        {
            Intent intent = new Intent(MainActivity2.this, Working.class);
            startActivity(intent);
            finish();
        }

    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            //The task returned from this call is always completed, no need to attach listener
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            acc = GoogleSignIn.getLastSignedInAccount(this);
            if(acc != null) {
                String personName = acc.getDisplayName();
                String personEmail = acc.getEmail();
                String personId = acc.getId();
                Uri personPic = acc.getPhotoUrl();

                Intent intent = new Intent(MainActivity2.this, SignIn.class);
                startActivity(intent);
            }
        } catch (ApiException e) {
            Log.d("message", e.toString());
        }
    }
}