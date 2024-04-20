package com.example.newchatchat.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newchatchat.ChatRoomActivity;
import com.example.newchatchat.Models.User;
import com.example.newchatchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    ArrayList<User> userlist;
    Context context;

    public UserAdapter(ArrayList<User> userlist, Context context) {
        this.userlist = userlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userlist.get(position);
        Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.baseline_person_24).into(holder.imageView);
        holder.name.setText(user.getUserName());



        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid() + user.getUserID())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                holder.lastmsg.setText(dataSnapshot.child("message").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatRoomActivity.class);
                intent.putExtra("userID", user.getUserID());
                intent.putExtra("profilePic", user.getProfilePic());
                intent.putExtra("userName", user.getUserName());


                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, lastmsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.name);
            lastmsg = itemView.findViewById(R.id.lastmsg);


        }
    }
}
