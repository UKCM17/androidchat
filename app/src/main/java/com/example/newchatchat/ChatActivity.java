package com.example.newchatchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.newchatchat.Adapter.FragmentAdapter;
import com.example.newchatchat.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Chat Chat");

        // binding complete view at once
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // create instance for Firebase Authentication
        // just demo use purpose
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("message");
        myref.setValue("Hello everyone");



        auth = FirebaseAuth.getInstance();

        binding.viewpager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tablayout.setupWithViewPager(binding.viewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:

                Intent i = new Intent(ChatActivity.this, SettingActivity.class);
                startActivity(i);
                break;

            case R.id.logout:
                auth.signOut();
                Intent intent = new Intent(ChatActivity.this, SignInActivity.class);
                startActivity(intent);
                break;

            case R.id.groupChat:

                Intent intentt = new Intent(ChatActivity.this, GroupChatActivity.class);
                startActivity(intentt);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}