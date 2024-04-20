package com.example.newchatchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.newchatchat.Adapter.ChatAdapter;
import com.example.newchatchat.Models.MessageModel;
import com.example.newchatchat.databinding.ActivityChatRoomBinding;
import com.example.newchatchat.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.txtName.setText("Friends Group Chat");


        final ChatAdapter adapter = new ChatAdapter(messageModels, this);
        binding.chatRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);


//        database.getReference().child("Group Chat")
//                        .addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                messageModels.clear();
//                                for ( DataSnapshot dataSnapshot : snapshot.getChildren()){
//                                    MessageModel model=dataSnapshot.getValue(MessageModel.class);
//                                    messageModels.add(model);
//                                }
//                                adapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });


        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.chatMSG.getText().toString().isEmpty())
                {
                    return;
                }
                final String message  =binding.chatMSG.getText().toString();
                final MessageModel model=new MessageModel(senderId,message);
                model.setTimestamp(new Date().getTime());

                binding.chatMSG.setText("");

                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });            }
        });
    }


}