package com.vibezz.Extra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vibezz.Activities.Profile;
import com.vibezz.Models.Users;
import com.vibezz.R;
import com.vibezz.databinding.ActivityFullscreenBinding;

import java.util.Objects;

public class Fullscreen extends AppCompatActivity {

    ActivityFullscreenBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.zoom_in, R.anim.static_anim);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        //Get the image of the user
        database.getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get().load(Objects.requireNonNull(users).getProfilePic())
                                .placeholder(R.drawable.user_blue_fullcircle).into(binding.imFull);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //On click of backImArrow
        binding.backImArrow.setOnClickListener(view -> {
            Intent intent = new Intent(Fullscreen.this, Profile.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.zoom_out, R.anim.static_anim);
        });

    }
}