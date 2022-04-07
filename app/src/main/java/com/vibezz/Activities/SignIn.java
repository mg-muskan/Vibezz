package com.vibezz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vibezz.R;
import com.vibezz.databinding.ActivitySignInBinding;

import java.util.Objects;

public class SignIn extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hiding the Action Bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Binding of this activity to use it further to forward to another activity
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Using Firebase for authentication and processing
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setMessage("Login to your account!");

        //On click of Login button
        binding.btnSignIn.setOnClickListener(view -> {

            //Show error if email or password field is empty
            if(binding.etEmailAddress.getText().toString().isEmpty()) {
                binding.etEmailAddress.setError("Enter your email");
                return;
            }
            if(binding.etPassword.getText().toString().isEmpty()) {
                binding.etPassword.setError("Enter your password");
                return;
            }

            //On login
            progressDialog.show();
            auth.signInWithEmailAndPassword(binding.etEmailAddress.getText().toString(), binding.etPassword.getText().toString()).
                    addOnCompleteListener(task -> {

                        progressDialog.dismiss();
                        if(task.isSuccessful())
                        {
                            //On successful Login/sign in redirect to the working activity
                            Intent intent = new Intent(SignIn.this, Working.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            //Give the error message
                            Toast.makeText(SignIn.this, Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    });

        });

        //On click of go back button, go to the Sign Up page/activity
        binding.bGoBack.setOnClickListener(view -> {
            Intent intent = new Intent(SignIn.this, MainActivity2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        });

    }

}