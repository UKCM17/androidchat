package com.example.newchatchat.Adapter;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newchatchat.Models.MessageModel;
import com.example.newchatchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModel> messageModels;
    Context context;
    String recID;
    int SENDER_VIEW_TYPE = 1;
    int RECIEVER_VIEW_TYPE = 2;


    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recID) {
        this.messageModels = messageModels;
        this.context = context;
        this.recID = recID;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECIEVER_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_layout, parent, false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModels.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure want to delete the message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                String senderRoom=FirebaseAuth.getInstance().getUid()+recID;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(messageModel.getMessageID())
                                        .setValue(null);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                 dialogInterface.dismiss();
                            }
                        }).show();
                return false;
            }
        });

         if(holder.getClass()==SenderViewHolder.class)
         {
             ((SenderViewHolder)holder).senderMessage.setText(messageModel.getMessage());
         }
         else
         {
             ((RecieverViewHolder)holder).recieverMessage.setText(messageModel.getMessage());
         }

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }  // return all chat

    public class RecieverViewHolder extends RecyclerView.ViewHolder {
        TextView recieverMessage, recieverTime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMessage = itemView.findViewById(R.id.recieverMsg);
            recieverTime = itemView.findViewById(R.id.recieverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessage, senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.txtSender);
            senderTime = itemView.findViewById(R.id.senderTime);

        }
    }
}