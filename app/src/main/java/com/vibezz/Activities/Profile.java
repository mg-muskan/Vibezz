package com.vibezz.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.vibezz.Extra.Fullscreen;
import com.vibezz.Extra.ProfileBottomSheet;
import com.vibezz.Models.Users;
import com.vibezz.R;
import com.vibezz.databinding.ActivityProfileBinding;

import java.util.HashMap;
import java.util.Objects;

public class Profile extends AppCompatActivity {

    ActivityProfileBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(binding.getRoot());

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //Going back to Working activity
        binding.leftArrow.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, Working.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_out, R.anim.static_anim);
        });

        //Save the information
        binding.btnSave.setOnClickListener(view -> {
            String status = binding.About.getText().toString();
            String user = binding.User.getText().toString();

            //To update any information in the firebase, use HashMap
            HashMap<String, Object> obj = new HashMap<>();
            obj.put("Name", user);
            obj.put("About", status);

            //All this is done inside database
            database.getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                    .updateChildren(obj);
        });

        //Updating the user image in the working activity
        database.getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get().load(Objects.requireNonNull(users).getProfilePic())
                                .placeholder(R.drawable.user_stylish).into(binding.circle);

                        binding.About.setText(users.getAbout());
                        binding.User.setText(users.getUser());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Taking the image from the user and redirecting the user to phone interface
        binding.plus.setOnClickListener(view -> {
            ProfileBottomSheet bottomSheet = new ProfileBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "Sheet");
        });

        binding.circle.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, Fullscreen.class);
            startActivity(intent);
        });

        //Logging out of ID
        binding.tvLogout.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(Profile.this, SignUp.class);
            Toast.makeText(Profile.this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Taking realtime data to take image/profile picture from the user
        if(Objects.requireNonNull(data).getData() != null) {
            Uri file = data.getData();
            binding.circle.setImageURI(file);

            //Setting up the image/profile picture user chose
            final StorageReference reference = storage.getReference().child("ProfilePic")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

            //Printing the message
            reference.putFile(Objects.requireNonNull(file)).addOnSuccessListener(taskSnapshot ->
                    Toast.makeText(Profile.this, "Uploaded", Toast.LENGTH_SHORT).show());

            //Updating the user picture and showing the message
            reference.getDownloadUrl().addOnSuccessListener(uri -> {
                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .child("profilePic").setValue(uri.toString());
                Toast.makeText(Profile.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
            });

        }
    }

}