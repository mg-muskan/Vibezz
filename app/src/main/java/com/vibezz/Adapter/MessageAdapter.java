package com.vibezz.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.vibezz.Models.Message;
import com.vibezz.R;
import com.vibezz.databinding.ItemReceiverBinding;
import com.vibezz.databinding.ItemSenderBinding;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

//Since here are two adapter, we do not give it ViewHolder in extends
public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messages;

    //If ItemViewType is 1 that means its the sender and when 2, its the receiver
    int ITEM_SENT = 1;
    int ITEM_RECEIVE = 2;

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sender, parent, false);
            return new SentViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receiver, parent, false);
            return new ReceiveViewHolder(view);
        }
    }

    //getItemViewType returns the view type of the item at position for the purposes of view recycling
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int getItemViewType(int position) {
        //Get the message based on the position
        //Message message = messages.get(position);
        //If userId matches the senderId
        //if(message.getSenderId().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))) {
        if(messages.get(position).getMessageId().equals(FirebaseAuth.getInstance().getUid())) {
            return ITEM_SENT;
        }
        else
            return ITEM_RECEIVE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //To set the user and receiver message side
        Message message = messages.get(position);
        if(holder.getClass() == SentViewHolder.class) {
            SentViewHolder viewHolder = (SentViewHolder)holder;
            viewHolder.binding.SenderM.setText(message.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            viewHolder.binding.STime.setText(df.format(new Date(message.getTimestamp())));
        }
        else {
            ReceiveViewHolder viewHolder = (ReceiveViewHolder)holder;
            viewHolder.binding.ReceiverM.setText(message.getMessage());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            viewHolder.binding.RTime.setText(df.format(new Date(message.getTimestamp())));
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        ItemSenderBinding binding;
        TextView senderM;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            senderM = itemView.findViewById(R.id.SenderM);
            binding = ItemSenderBinding.bind(itemView);
        }
    }

    public class ReceiveViewHolder extends RecyclerView.ViewHolder {

        ItemReceiverBinding binding;
        TextView receiverM;
        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverM = itemView.findViewById(R.id.ReceiverM);
            binding = ItemReceiverBinding.bind(itemView);
        }
    }
}
