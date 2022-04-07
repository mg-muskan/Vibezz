package com.vibezz.Extra;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.vibezz.R;

import java.util.Objects;

public class ProfileBottomSheet extends BottomSheetDialogFragment {

    public ProfileBottomSheet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Attaching it with status_bottom_sheet xml file
        View view = inflater.inflate(R.layout.profile_bottom_sheet, container, false);

        //Remove the already uploaded photo
        ImageView remove = view.findViewById(R.id.remove);
        remove.setOnClickListener(view13 -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                    .child("profilePic").setValue(null);
            Toast.makeText(getContext(), "Removed.", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        //Taking the image from camera
        ImageView camera = view.findViewById(R.id.i_cam);
        camera.setOnClickListener(view12 -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 33);

            dismiss();
        });

        //Taking the image from the user and redirecting the user to phone interface
        ImageView gallery = view.findViewById(R.id.i_gallery);
        gallery.setOnClickListener(view1 -> {

            //Access to gallery
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*"); // */*
            startActivityForResult(intent, 33);
            dismiss();

        });

        return view;
    }

}
