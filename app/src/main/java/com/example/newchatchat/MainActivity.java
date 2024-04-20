package com.example.newchatchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.newchatchat.Models.User;
import com.example.newchatchat.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    ActivityMainBinding binding;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Account Creation Activity Start");
        progressDialog.setMessage("Account creation process is under process....");


        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.username.getText().toString().isEmpty())
                {
                    binding.username.setError("Enter Username");
                    return;
                }
                if(binding.email.getText().toString().isEmpty())
                {
                    binding.email.setError("Enter Email ID");
                    return;
                }
                if(binding.password.getText().toString().isEmpty())
                {
                    binding.password.setError("Enter Password");
                    return;
                }
                progressDialog.show();
                auth.createUserWithEmailAndPassword
                        (binding.email.getText().toString(), binding.password.getText().toString()).addOnCompleteListener
                        (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            User newuser=new User(binding.username.getText().toString(),binding.email.getText().toString(),binding.password.getText().toString());
                            String id= Objects.requireNonNull(task.getResult().getUser()).getUid();
                            database.getReference().child("User").child(id).setValue(newuser);
                            Toast.makeText(MainActivity.this, "Account created successfully....", Toast.LENGTH_SHORT).show();
                            gotoSignIn(view);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void gotoSignIn(View view) {
        Intent intent=new Intent(MainActivity.this,SignInActivity.class);
        startActivity(intent);
    }


}