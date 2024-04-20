package com.example.newchatchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.newchatchat.Adapter.ChatAdapter;
import com.example.newchatchat.Models.MessageModel;
import com.example.newchatchat.databinding.ActivityChatRoomBinding;
import com.example.newchatchat.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatRoomActivity extends AppCompatActivity {

// notification
private static final String CHANNEL_ID="My Channel";
private static final int NOTIFICATION_ID=100;

    ActivityChatRoomBinding binding;

    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderID = auth.getUid();
        String recieveID = getIntent().getStringExtra("userID");
        String username = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");
//        long timestamp=getIntent().getStringExtra()
        binding.txtName.setText(username);
        Picasso.get().load(profilePic).placeholder(R.drawable.baseline_person_24).into(binding.profileImage);
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this, recieveID);

        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        final String senderRoom = senderID + recieveID;
        final String recieverRoom = recieveID + senderID;

        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            MessageModel model = dataSnapshot.getValue(MessageModel.class);
                            model.setMessageID(dataSnapshot.getKey());
                            messageModels.add(model);

//                                            Toast.makeText(ChatRoomActivity.this, "hiii", Toast.LENGTH_SHORT).show();
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.chatMSG.getText().toString().isEmpty())
                {
                    return;
                }
                String message = binding.chatMSG.getText().toString();
                final MessageModel model = new MessageModel(senderID, message);
                model.setTimestamp(new Date().getTime());
                //Date today = Calendar.getInstance().getTime();
               // model.setNewTime(today);
                binding.chatMSG.setText("");

                database.getReference().child("chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        loadNotifications(message);
                        database.getReference("chats").child(recieverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });
    }
    void loadNotifications(String msg)
    {
        //notification
        Drawable drawable= ResourcesCompat.getDrawable(getResources(),R.drawable.chatvat,null);
        BitmapDrawable bitmapDrawable=(BitmapDrawable)drawable;
        Bitmap largeIcon=bitmapDrawable.getBitmap();
        Notification notification;
        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification=new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.chatvat)
                    .setContentText(msg)
                    .setSubText("New message from USC")
                    .setChannelId(CHANNEL_ID)
                    .build();
            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"new Channel",NotificationManager.IMPORTANCE_HIGH));
        }
        else {
            notification=new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.chatvat)
                    .setContentText("New Message")
                    .setSubText(msg)
                    .build();
        }
        notificationManager.notify(NOTIFICATION_ID,notification);

 //notification
    }

}