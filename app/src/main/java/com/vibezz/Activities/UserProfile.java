package com.vibezz.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.vibezz.Adapter.FragmentsAdapter;
import com.vibezz.R;
import com.vibezz.databinding.ActivityUserProfileBinding;

import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    ActivityUserProfileBinding binding;
    TabLayout tab;
    ViewPager2 vp;
    FragmentsAdapter fragsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        tab = findViewById(R.id.tabL);
        vp = findViewById(R.id.vp);

        FragmentManager fm = getSupportFragmentManager();
        fragsAdapter = new FragmentsAdapter(fm, getLifecycle());
        vp.setAdapter(fragsAdapter);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tab.selectTab(tab.getTabAt(position));
            }
        });

        //Taking the user data
        String name = getIntent().getStringExtra("Name");
        String receiverUId = getIntent().getStringExtra("uId");
        String profile = getIntent().getStringExtra("Profile");

        //Show the user name
        Objects.requireNonNull(binding.tvPName).setText(name);

        //Showing the user image
        Picasso.get().load(profile).placeholder(R.drawable.user_blue_circle).into(binding.userPImage);

        //On click of the back arrow
        binding.leftPArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, Chats.class);
                intent.putExtra("Name", name);
                intent.putExtra("Profile", profile);
                intent.putExtra("uId", receiverUId);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
            }
        });

        //Follow button
        binding.bFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}