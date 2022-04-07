package com.vibezz.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.vibezz.Models.Users;
import com.vibezz.R;
import com.vibezz.databinding.ActivitySignUpBinding;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Binding and taking the realtime data through firebase
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Hiding the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //SignUp of user
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setMessage("Creating Your Account!");

        //On click of Sign Up button
        binding.btnSignUp.setOnClickListener(view -> {
            progressDialog.show();
            //Get user's email and password
            auth.createUserWithEmailAndPassword
                    (binding.etEmailAddress.getText().toString() , binding.etPassword.getText().toString()).
                    addOnCompleteListener(task -> {

                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            Users user = new Users(binding.etName.getText().toString(),
                                    binding.etEmailAddress.getText().toString(), binding.etPassword.getText().toString());

                            String id = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                            database.getReference().child("Users").child(id).setValue(user);

                            //Give the message of successful Sign Up and get user to the working activity
                            Toast.makeText(SignUp.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp.this, Working.class);
                            startActivity(intent);
                        }
                        else {
                            //Give an error message
                            Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    });

        });

        //On clicking the textView, redirect to the Sign In page/activity
        binding.bGoBack.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this, MainActivity2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        });

    }
}