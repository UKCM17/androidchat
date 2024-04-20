package com.example.newchatchat.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newchatchat.Adapter.UserAdapter;
import com.example.newchatchat.Models.User;
import com.example.newchatchat.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatFragment extends Fragment {

    FragmentChatBinding binding;
    ArrayList<User> list = new ArrayList<>();
    FirebaseDatabase database;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();

        UserAdapter userAdapter = new UserAdapter(list, getContext());
        binding.chatRecycler.setAdapter(userAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecycler.setLayoutManager(layoutManager);

        database.getReference().child("User").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setUserID(dataSnapshot.getKey());
                    if(!user.getUserID().equals(FirebaseAuth.getInstance().getUid()))
                        list.add(user);
                    else {
                        user.setUserName(user.getUserName() + "(You)");
                        list.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}