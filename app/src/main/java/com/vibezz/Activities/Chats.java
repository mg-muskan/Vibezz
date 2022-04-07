package com.vibezz.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vibezz.Adapter.MessageAdapter;
import com.vibezz.Models.Message;
import com.vibezz.R;
import com.vibezz.databinding.ActivityChatsBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Chats extends AppCompatActivity {

    ActivityChatsBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<Message> messages;
    MessageAdapter adapter;

    String senderRoom, receiverRoom;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String TAG = "Permsission : ";
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            //Define the path you want
            File mFolder = new File(Environment.getExternalStorageDirectory(), "Vibezz");
            if (!mFolder.exists()) {
                boolean b =  mFolder.mkdirs();

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

//        File mydir = new File(getApplicationContext().getExternalFilesDir("VIbezz").getAbsolutePath());
//        if (!mydir.exists())
//        {
//            boolean b  = mydir.mkdirs();
//            Toast.makeText(getApplicationContext(),"Directory Created",Toast.LENGTH_LONG).show();
//        }
//
//        String TAG = "Permsission : ";
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted");
////                return true;
//            } else {
//
//                Log.v(TAG,"Permission is revoked");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
////                return false;
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG, "Permission is granted");
////            return true;
//        }

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        messages = new ArrayList<>();
        adapter = new MessageAdapter(this, messages);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        //binding.recyclerView.smoothScrollToPosition(binding.recyclerView.getBottom());
        //Setting the adapter layout to print the message
        binding.recyclerView.setAdapter(adapter);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
            }
        }, 200);

//        int position = Objects.requireNonNull(adapter).getItemCount();
//        binding.recyclerView.smoothScrollToPosition(position);

//        // Scroll to bottom on new messages
//        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                binding.recyclerView.smoothScrollToPosition(adapter.getItemCount());
//            }
//        });

        //Taking the receiver data
        String name = getIntent().getStringExtra("Name");
        final String receiverUId = getIntent().getStringExtra("uId");
        String profile = getIntent().getStringExtra("Profile");
        final String senderUId = FirebaseAuth.getInstance().getUid();

//        TextView tvDay = findViewById(R.id.tvDay);
//        final String messageText = binding.TypeM.getText().toString();
//
//        //final Date date = new Date();
//        final Message message = new Message(senderUId, messageText);
//
//        long firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
//        if (getDateFromFirebaseTime(messages[firstVisiblePosition].timestamp.toString().toLong()).isNotEmpty()) {
//            tvDay.setVisibility(View.VISIBLE);
//            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//            tvDay.setText(df.format(new Date(message.getTimestamp())));
//        } else {
//            tvDay.setVisibility(View.GONE);
//        }

        //Setting up the toolbar
        Picasso.get().load(profile).placeholder(R.drawable.user_black_circle).into(binding.userImage);
        binding.tvName.setText(name);

        //Going to userProfile activity
        binding.userImage.setOnClickListener(v -> {
            Intent intent = new Intent(Chats.this, UserProfile.class);
            intent.putExtra("Name", name);
            intent.putExtra("uId", receiverUId);
            intent.putExtra("Profile", profile);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        });

        //Going back to working activity
        binding.backArrow.setOnClickListener(v -> {
            Intent intent  = new Intent(Chats.this, Working.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            finish();
        });

        //sender and receiver rooms/side
        senderRoom = senderUId + receiverUId;
        receiverRoom = receiverUId + senderUId;

        //Showing the texts
        database.getReference().child("chats")
                .child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message message = snapshot1.getValue(Message.class);
                            messages.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //On Click on the send button
        binding.Send.setOnClickListener(view -> {
            //Take the text
            final String messageText = binding.TypeM.getText().toString();

            //final Date date = new Date();
            final Message message = new Message(senderUId, messageText);
            //final Message message = new Message(messageText, senderUId, date.getTime());
            message.setTimestamp(new Date().getTime());

            //Setting up the message
            binding.TypeM.setText("");

            //Update the last message
            HashMap<String, Object> lastMessage = new HashMap<>();
            lastMessage.put("lastMessage", message.getMessage());
            lastMessage.put("lastMessageTime", message.getTimestamp());

            database.getReference().child("chats").child(senderRoom)
                    .updateChildren(lastMessage);
            database.getReference().child("chats").child(receiverRoom)
                    .updateChildren(lastMessage);

            //Push() generates unique key every-time a new child is added to the specifies Firebase reference
            database.getReference().child("chats")
                    .child(senderRoom).child("messages").push().setValue(message)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    database.getReference().child("chats")
                            .child(receiverRoom).child("messages").push().setValue(message)
                            .addOnSuccessListener(aVoid1 -> {

                            });

                }
            });


            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                }
            }, 200);
            adapter.notifyDataSetChanged();
        });

    }

}