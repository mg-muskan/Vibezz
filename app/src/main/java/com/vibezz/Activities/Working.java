package com.vibezz.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.vibezz.Adapter.UserAdapter;
import com.vibezz.Models.Users;
import com.vibezz.R;
import com.vibezz.databinding.ActivityWorkingBinding;

import java.util.ArrayList;
import java.util.Objects;

public class Working extends AppCompatActivity {

    ActivityWorkingBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    UserAdapter userAdapter;
    ArrayList<Users> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        users = new ArrayList<>();

        userAdapter = new UserAdapter(this, users);
        //Linking RecyclerView
        binding.recycler.setAdapter(userAdapter);

        //User Data Update
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Users user = snapshot1.getValue(Users.class);
                    Objects.requireNonNull(user).setUserId(snapshot1.getKey());
                    if(!user.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
                        users.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /*Linking Menu with the Activity*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.working, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.app_bar_search:
                break;
            case R.id.Profile:
                Intent intent = new Intent(Working.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_out, R.anim.static_anim);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}