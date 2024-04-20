package com.example.newchatchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.newchatchat.Models.User;
import com.example.newchatchat.databinding.ActivitySettingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });


        binding.btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.username.getText().toString();
                String status = binding.tvstatus.getText().toString();

                HashMap<String, Object> obj = new HashMap<>();
                obj.put("userName", username);
                obj.put("status", status);
                database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(obj);
                Toast.makeText(SettingActivity.this, "User Name and Status Updated Successfully....", Toast.LENGTH_SHORT).show();

            }
        });

        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User users = snapshot.getValue(User.class);
                        Picasso.get()
                                .load(users.getProfilePic())
                                .placeholder(R.drawable.baseline_person_24)
                                .into(binding.profileimage);
                        binding.username.setText(users.getUserName());
                        binding.tvstatus.setText(users.getStatus());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.addprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            Uri sFile = data.getData();
            binding.profileimage.setImageURI(sFile);

            final StorageReference reference = storage.getReference().child("profilePic")
                    .child(FirebaseAuth.getInstance().getUid());
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(SettingActivity.this, "Upload successfully", Toast.LENGTH_SHORT).show();
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                                    .child("profilePic").setValue(uri.toString());
                            Toast.makeText(SettingActivity.this, "Profile Picture Updated Successfully....", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}