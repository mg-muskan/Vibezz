package com.vibezz.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vibezz.Activities.Chats;
import com.vibezz.Models.Users;
import com.vibezz.R;
import com.vibezz.databinding.UsersViewBinding;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<com.vibezz.Adapter.UserAdapter.UserViewHolder> {

    Context context;
    ArrayList<Users> users;

    public UserAdapter(Context context, ArrayList<Users> users) {
        this.context = context;
        this.users = users;
    }

    //Links the class with the sample view of recyclerView
    @NonNull
    @Override
    /*A ViewHolder describes an item view and metadata about its place within the RecyclerView*/
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_view, parent, false);
        return new UserViewHolder(view);
    }

    //Getting the position and image update of the user
    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {

        //Taking the order in which the user name and id should be shown
        final Users user = users.get(position);
        Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.user_black_circle).into(holder.profile);
        holder.binding.UserName.setText(user.getUser());

        //Showing the last message
        String senderId = FirebaseAuth.getInstance().getUid();
        String senderRoom = senderId + user.getUserId();
        FirebaseDatabase.getInstance().getReference()
                .child("chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()) {
                            String lastMessage = snapshot.child("lastMessage").getValue(String.class);
                            long time = snapshot.child("lastMessageTime").getValue(Long.class);
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");

                            holder.binding.tvMessage.setText(lastMessage);
                            holder.binding.tvTime.setText(df.format(new Date(time)));
                        }
                        else
                        {
                            holder.binding.tvMessage.setText("Tap to chat");
                            holder.binding.tvTime.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //When clicked on any user
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chats.class);
                intent.putExtra("Name", user.getUser());
                intent.putExtra("Profile", user.getProfilePic());
                intent.putExtra("uId", user.getUserId());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                if (context instanceof Activity) {
//                    ((Activity) context).overridePendingTransition(R.layout.fade_in, R.layout.fade_out);
//                } THis method can also be done instead of that.
            }
        });

    }

    //Set the number of users into recyclerView
    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        UsersViewBinding binding;
        ImageView profile;
        TextView userName, message;

        //Taking the data from the user and putting it in the view/list
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.Profile);
            userName = itemView.findViewById(R.id.UserName);
            message = itemView.findViewById(R.id.tvMessage);
            binding = UsersViewBinding.bind(itemView);
        }
    }

}
